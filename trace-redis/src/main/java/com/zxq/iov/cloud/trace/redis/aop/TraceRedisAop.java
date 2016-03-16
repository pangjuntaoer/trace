package com.zxq.iov.cloud.trace.redis.aop;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;

import com.zxq.iov.cloud.trace.Annotation;
import com.zxq.iov.cloud.trace.AnnotationType;
import com.zxq.iov.cloud.trace.Span;
import com.zxq.iov.cloud.trace.TraceContext;
import com.zxq.iov.cloud.trace.Tracer;

//@Component
//@Aspect
//@Order(0)
public class TraceRedisAop {
	
//	private static final String PC_REDIS = "execution(public* com.zxq.iov.cloud.trace.utils.JedisClusterUtils.*(..))";
//
//	@Around(value = PC_REDIS)
	public Object around(ProceedingJoinPoint point) throws Throwable {
		Object result = null;
		Tracer tracer = Tracer.getTracer();
		TraceContext context = tracer.getTraceContext();

		if (context == null) {
			return point.proceed();
		}
		
		boolean isSample = context.getIsSample();

		Span span = null;
		Map<String, String[]> parasMap = null;
		if (isSample) {
			String currentSpanId = tracer.genCurrentSpanId(context.getParentSpanId());
			span = new Span(context.getTraceId(), currentSpanId);
			span.setSignature(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());
			span.addAnnotation(
					new Annotation(AnnotationType.CS.name(), System.currentTimeMillis(), context.getIp(), parasMap));
		}
		try {
			result = point.proceed();
		} finally {
			if (isSample) {
				span.addAnnotation(new Annotation(AnnotationType.CR.name(), System.currentTimeMillis(),
						context.getIp(), parasMap));
				tracer.sendSpan(span);
			}
		}
		return result;
	}

}
