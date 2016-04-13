package com.zxq.iov.cloud.trace.mq.aop;

import org.aspectj.lang.ProceedingJoinPoint;

import com.saicmotor.telematics.framework.core.trace.MQMsgDto;
import com.zxq.iov.cloud.trace.Annotation;
import com.zxq.iov.cloud.trace.AnnotationType;
import com.zxq.iov.cloud.trace.Span;
import com.zxq.iov.cloud.trace.TraceContext;
import com.zxq.iov.cloud.trace.Tracer;

public class TraceMqSendAop {

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
			MQMsgDto dto = args.length == 1 ? (MQMsgDto) args[0] : (MQMsgDto) args[1];
			dto.setTraceId(traceId);
			dto.setSample(isSample);
			dto.setParentSpanId(context.getCurrentSpanId());
			return point.proceed();
		} finally {
			if (isSample) {
				span.addAnnotation(
						new Annotation(AnnotationType.CR.name(), System.currentTimeMillis(), context.getIp(), null));
				tracer.sendSpan(span);
			}
		}
	}

}
