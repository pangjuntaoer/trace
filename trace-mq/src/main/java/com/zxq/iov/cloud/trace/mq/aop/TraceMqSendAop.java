package com.zxq.iov.cloud.trace.mq.aop;

import org.aspectj.lang.ProceedingJoinPoint;

import com.zxq.iov.cloud.trace.Annotation;
import com.zxq.iov.cloud.trace.AnnotationType;
import com.zxq.iov.cloud.trace.Span;
import com.zxq.iov.cloud.trace.TraceContext;
import com.zxq.iov.cloud.trace.Tracer;
import com.zxq.iov.cloud.trace.dto.MsgWrapperDto;

//@Component
//@Aspect
//@Order(0)
public class TraceMqSendAop {
	
//	private static final String PC_AMQP_S_1 = "execution(public* org..AmqpTemplate.convertAndSend(Object))";
//
//	private static final String PC_AMQP_S_2 = "execution(public* org..AmqpTemplate.convertAndSend(String, Object))";
//
//	@Around(value = PC_AMQP_S_1 + " or " + PC_AMQP_S_2)
	public Object around(ProceedingJoinPoint point) throws Throwable {
		Object[] args = point.getArgs();
		Tracer tracer = Tracer.getTracer();
		TraceContext context = tracer.getTraceContext();
		
		if (context == null) {
			return point.proceed();
		}
		
		Boolean isSample = context.getIsSample();
		String traceId = context.getTraceId();
		Span span = null;
		if (isSample) {
			String currentSpanId = tracer.genCurrentSpanId(context.getParentSpanId());
			span = new Span(traceId, currentSpanId);
			span.setSignature(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());
			span.addAnnotation(
					new Annotation(AnnotationType.CS.name(), System.currentTimeMillis(), context.getIp(), null));
			context.putSpan(span);
		}
		try {
			if (args != null && args.length == 1) {
				args[0] = new MsgWrapperDto(traceId, isSample, context.getCurrentSpanId(), args[0]);
			} else {
				args[1] = new MsgWrapperDto(traceId, isSample, context.getCurrentSpanId(), args[1]);
			}
			return point.proceed(args);
		} finally {
			if (isSample) {
				span.addAnnotation(
						new Annotation(AnnotationType.CR.name(), System.currentTimeMillis(), context.getIp(), null));
				tracer.sendSpan(span);
			}
		}
	}

}
