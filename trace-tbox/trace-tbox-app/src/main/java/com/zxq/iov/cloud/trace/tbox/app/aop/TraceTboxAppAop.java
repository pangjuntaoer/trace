package com.zxq.iov.cloud.trace.tbox.app.aop;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saicmotor.telematics.framework.core.trace.MQMsgDto;
import com.saicmotor.telematics.framework.core.utils.IpUtil;
import com.saicmotor.telematics.framework.core.utils.JsonUtil;
import com.zxq.iov.cloud.trace.Annotation;
import com.zxq.iov.cloud.trace.AnnotationType;
import com.zxq.iov.cloud.trace.Span;
import com.zxq.iov.cloud.trace.TraceContext;
import com.zxq.iov.cloud.trace.Tracer;

public class TraceTboxAppAop {

	private static ObjectMapper mapper = new ObjectMapper();
	
	public Object aroundTboxApp(ProceedingJoinPoint point) throws Throwable {
		Object result = null;
		Object[] args = point.getArgs();
		Tracer tracer = Tracer.getTracer();

		String json = null;
		if (args[0] instanceof MQMsgDto) {
			json = mapper.writeValueAsString(args[0]);
		} else {
			json = new String((byte[]) args[0], JsonEncoding.UTF8.name());
		}
		
		String traceId = JsonUtil.getObjFromJson(json,"traceId", String.class);

		boolean isSample = JsonUtil.getObjFromJson(json,"sample", Boolean.class);
		String parentSpanId = JsonUtil.getObjFromJson(json,"parentSpanId", String.class);

		TraceContext context = new TraceContext();
		context.setTraceId(traceId);
		context.setIsSample(isSample);
		context.setParentSpanId(parentSpanId);
		context.setIp(IpUtil.getNetworkIp());
		tracer.setTraceContext(context);

		Span rootSpan = null;
		Map<String, String[]> parasMap = null;
		if (isSample) {
			rootSpan = new Span(context.getTraceId(),tracer.genCurrentSpanId(parentSpanId));
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
