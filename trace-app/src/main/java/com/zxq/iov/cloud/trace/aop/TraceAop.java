package com.zxq.iov.cloud.trace.aop;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;
import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.rpc.RpcContext;
import com.zxq.iov.cloud.trace.Annotation;
import com.zxq.iov.cloud.trace.AnnotationType;
import com.zxq.iov.cloud.trace.BinaryAnnotation;
import com.zxq.iov.cloud.trace.Span;
import com.zxq.iov.cloud.trace.TraceContext;
import com.zxq.iov.cloud.trace.Tracer;
import com.zxq.iov.cloud.trace.dto.MsgWrapperDto;

@Component
@Aspect
@Order(0)
public class TraceAop {

	private static final String pointStr = "execution(public* com.zxq.iov.cloud..*Api.*(..)) "
			+ " or execution(public* org.dubbo..*Api.*(..)) " 
			+ "or execution(public* org.dubbo..*ApiImpl.*(..)) ";

	private static final String PC_AMQP_S_1 = "execution(public* org.springframework.amqp.core.AmqpTemplate.convertAndSend(Object))";

	private static final String PC_AMQP_S_2 = "execution(public* org.springframework.amqp.core.AmqpTemplate.convertAndSend(String, Object))";

	private static final String PC_TBOX = "execution(public* com.zxq.iov.cloud.gw.tbox.udpserver.filter.EncryptionFilter.messageReceived(..))";

	private static final String PC_AMQP_R = "execution(public* com.zxq.iov.cloud.app.tbox.GatewayEntryPoint.onMessage(com.zxq.iov.cloud.trace.dto.MsgWrapperDto))";

	@Around(value = PC_AMQP_R)
	public Object aroundAmqpRecive(ProceedingJoinPoint point) throws Throwable {
		Object result = null;
		Object[] args = point.getArgs();
		Tracer tracer = Tracer.getTracer();

		MsgWrapperDto dto = (MsgWrapperDto) args[0];
		
		boolean isSample = dto.getIsSample();

		TraceContext context = new TraceContext();
		context.setTraceId(dto.getTraceId());
		context.setIsSample(isSample);
		context.setParentSpanId(dto.getParentSpanId());
		context.setCurrentSpanId(dto.getParentSpanId());
		context.setIp(getLocalIp());
		tracer.setTraceContext(context);

		Span rootSpan = null;
		Map<String, String[]> parasMap = null;
		if (isSample) {
			rootSpan = new Span(context.getTraceId(), dto.getParentSpanId());
			rootSpan.setSignature(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());
			rootSpan.addAnnotation(
					new Annotation(AnnotationType.SR.name(), System.currentTimeMillis(), context.getIp(), parasMap));
		}
		try {
			result = point.proceed();
		} finally {
			if (isSample) {
				rootSpan.addAnnotation(new Annotation(AnnotationType.SS.name(), System.currentTimeMillis(),
						context.getIp(), parasMap));
				tracer.sendSpan(rootSpan);
			}
			tracer.removeTraceContext();
		}
		return result;
	}

	@Around(value = PC_TBOX)
	public Object aroundTbox(ProceedingJoinPoint point) throws Throwable {
		Tracer tracer = Tracer.getTracer();
		boolean isSample = tracer.isSample();
		Object result = null;

		TraceContext context = tracer.getTraceContext();

		String parentSpanId = Tracer.DEFAULT_PARENT_SPAN_ID;
		String traceId = UUID.randomUUID().toString();
		context = new TraceContext();
		context.setTraceId(traceId);
		context.setIsSample(isSample);
		context.setParentSpanId(parentSpanId);
		context.setCurrentSpanId(parentSpanId);
		context.setIp(getLocalIp());
		tracer.setTraceContext(context);

		Span rootSpan = null;
		if (isSample) {
			rootSpan = new Span(traceId, parentSpanId);
			rootSpan.setSignature(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());
			rootSpan.addAnnotation(
					new Annotation(AnnotationType.SR.name(), System.currentTimeMillis(), context.getIp(), null));
			context.putSpan(rootSpan);
		}
		try {
			result = point.proceed();
		} finally {
			if (isSample) {
				rootSpan.addAnnotation(
						new Annotation(AnnotationType.SS.name(), System.currentTimeMillis(), context.getIp(), null));
				tracer.sendSpan(rootSpan);
			}
			tracer.removeTraceContext();
		}

		return result;
	}

	@Around(value = PC_AMQP_S_1 + " or " + PC_AMQP_S_2)
	public Object aroundAmqpSend(ProceedingJoinPoint point) throws Throwable {
		Object result = null;
		Object[] args = point.getArgs();
		Tracer tracer = Tracer.getTracer();
		TraceContext context = tracer.getTraceContext();
		Boolean isSample = context.getIsSample();
		String traceId = context.getTraceId();
		Span span = null;
		if (isSample) {
			String currentSpanId = tracer.genCurrentSpanId(context.getParentSpanId(), context.getCurrentSpanId());

			span = new Span(traceId, currentSpanId);
			span.setSignature(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());
			span.addAnnotation(
					new Annotation(AnnotationType.CS.name(), System.currentTimeMillis(), context.getIp(), null));
			context.putSpan(span);
			context.setCurrentSpanId(currentSpanId);
		}
		try {
			if (args != null && args.length == 1) {
				args[0] = new MsgWrapperDto(traceId, isSample, context.getCurrentSpanId(), args[0]);
			} else {
				args[1] = new MsgWrapperDto(traceId, isSample, context.getCurrentSpanId(), args[1]);
			}
			result = point.proceed(args);
		} finally {
			if (isSample) {
				span.addAnnotation(
						new Annotation(AnnotationType.CR.name(), System.currentTimeMillis(), context.getIp(), null));
				tracer.sendSpan(span);
			}
		}

		return result;
	}

	@AfterThrowing(value = pointStr)
	public void afterThrowing(JoinPoint point) {
		Tracer tracer = Tracer.getTracer();
		TraceContext context = tracer.getTraceContext();
		Boolean isSample = context.getIsSample();
		if (isSample) {
			Span span = context.getSpan(context.getCurrentSpanId());
			span.addBinaryAnnotation(
					new BinaryAnnotation("exception", System.currentTimeMillis(), context.getIp(), null));
			tracer.sendSpan(span);
		}
	}

	@Around(value = pointStr)
	public Object around(ProceedingJoinPoint point) throws Throwable {
		System.out.println("---------------around aop---------");
		Object result = null;
		RpcContext rpcContext = RpcContext.getContext();
		Tracer tracer = Tracer.getTracer();
		TraceContext context = tracer.getTraceContext();
		Boolean isSample;
		if (context == null) {// server
			isSample = Boolean.valueOf(rpcContext.getAttachment("isSample"));
			String parentSpanId = rpcContext.getAttachment("parentSpanId");
			String traceId = rpcContext.getAttachment("traceId");
			context = new TraceContext();
			context.setTraceId(traceId);
			context.setIsSample(isSample);
			context.setParentSpanId(parentSpanId);
			context.setCurrentSpanId(parentSpanId);
			context.setIp(getLocalIp());
			tracer.setTraceContext(context);
			Span span = null;
			if (isSample) {
				span = new Span(traceId, parentSpanId);
				span.setSignature(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());
				span.addAnnotation(
						new Annotation(AnnotationType.SR.name(), System.currentTimeMillis(), context.getIp(), null));
				context.putSpan(span);
			}
			try {
				result = point.proceed();
			} finally {
				if (isSample) {
					span.addAnnotation(new Annotation(AnnotationType.SS.name(), System.currentTimeMillis(),
							context.getIp(), null));
					tracer.sendSpan(span);
				}
				tracer.removeTraceContext();
			}

		} else {// client
			isSample = context.getIsSample();
			Span span = null;
			if (isSample) {
				String currentSpanId = tracer.genCurrentSpanId(context.getParentSpanId(), context.getCurrentSpanId());
				String traceId = context.getTraceId();
				span = new Span(traceId, currentSpanId);
				span.setSignature(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());
				span.addAnnotation(
						new Annotation(AnnotationType.CS.name(), System.currentTimeMillis(), context.getIp(), null));
				context.putSpan(span);
				context.setCurrentSpanId(currentSpanId);
			}
			rpcContext.setAttachment("traceId", context.getTraceId());
			rpcContext.setAttachment("isSample", String.valueOf(isSample));
			rpcContext.setAttachment("parentSpanId", context.getCurrentSpanId());
			try {
				result = point.proceed();
			} finally {
				if (isSample) {
					span.addAnnotation(new Annotation(AnnotationType.CR.name(), System.currentTimeMillis(),
							context.getIp(), null));
					tracer.sendSpan(span);
				}
			}
		}
		return result;
	}

	private String getLocalIp() throws SocketException {
		Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		String ipStr = null;
		boolean b = false;
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface netInterface = allNetInterfaces.nextElement();
			Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				ip = addresses.nextElement();
				if (ip != null && ip instanceof Inet4Address && !ip.isLoopbackAddress() && !ip.isMulticastAddress()
						&& !ip.isSiteLocalAddress()) {
					ipStr = ip.getHostAddress();
					b = true;
					break;
				}
			}
			if (b) {
				break;
			}
		}
		return ipStr;
	}

}
