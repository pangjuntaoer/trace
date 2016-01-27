package com.zxq.iov.cloud.trace.tbox.gw.aop;

import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;

import com.saicmotor.telematics.framework.core.utils.IpUtil;
import com.zxq.iov.cloud.trace.Annotation;
import com.zxq.iov.cloud.trace.AnnotationType;
import com.zxq.iov.cloud.trace.Span;
import com.zxq.iov.cloud.trace.TraceContext;
import com.zxq.iov.cloud.trace.Tracer;

//@Component
//@Aspect
//@Order(10)
public class TraceTboxGwAop {
	
//	private static final String PC_TBOX = "execution(public* com.zxq.iov.cloud.gw.tbox.udpserver.filter.EncryptionFilter.messageReceived(..))";
//
//	@Around(value = PC_TBOX)
	public Object around(ProceedingJoinPoint point) throws Throwable {
		Tracer tracer = Tracer.getTracer();
		boolean isSample = tracer.isSample();
		Object result = null;

		String parentSpanId = Tracer.DEFAULT_PARENT_SPAN_ID;
		String traceId = UUID.randomUUID().toString();
		TraceContext context = new TraceContext();
		context.setTraceId(traceId);
		context.setIsSample(isSample);
		context.setParentSpanId(parentSpanId);
		context.setIp(IpUtil.getNetworkIp());
		tracer.setTraceContext(context);

		Span rootSpan = null;
		if (isSample) {
			rootSpan = new Span(traceId, tracer.genCurrentSpanId(parentSpanId));
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

}
