package com.zxq.iov.cloud.trace.tbox.app.aop;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.saicmotor.telematics.framework.core.utils.IpUtil;
import com.zxq.iov.cloud.trace.Annotation;
import com.zxq.iov.cloud.trace.AnnotationType;
import com.zxq.iov.cloud.trace.Span;
import com.zxq.iov.cloud.trace.TraceContext;
import com.zxq.iov.cloud.trace.Tracer;
import com.zxq.iov.cloud.trace.dto.MsgWrapperDto;

@Component
@Aspect
@Order(0)
public class TraceTboxAppAop {

	private static final String PC_AMQP_R = "execution(public* com.zxq.iov.cloud.app.tbox.GatewayEntryPoint.onMessage(com.zxq.iov.cloud.trace.dto.MsgWrapperDto))"
			 + " or " +
			 "execution(public* com.zxq.iov.cloud.app.tbox.ServiceEntryPoint.onMessage(com.zxq.iov.cloud.trace.dto.MsgWrapperDto))";

	@Around(value = PC_AMQP_R)
	public Object aroundTboxApp(ProceedingJoinPoint point) throws Throwable {
		Object result = null;
		Object[] args = point.getArgs();
		Tracer tracer = Tracer.getTracer();

		MsgWrapperDto dto = (MsgWrapperDto) args[0];

		boolean isSample = dto.getIsSample();

		TraceContext context = new TraceContext();
		context.setTraceId(dto.getTraceId());
		context.setIsSample(isSample);
		context.setParentSpanId(dto.getParentSpanId());
		context.setIp(IpUtil.getNetworkIp());
		tracer.setTraceContext(context);

		Span rootSpan = null;
		Map<String, String[]> parasMap = null;
		if (isSample) {
			rootSpan = new Span(context.getTraceId(),tracer.genCurrentSpanId(dto.getParentSpanId()));
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

}
