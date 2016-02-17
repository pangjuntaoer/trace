package com.zxq.iov.cloud.trace.mq.aop;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;

import com.saicmotor.telematics.framework.core.utils.IpUtil;
import com.zxq.iov.cloud.trace.Annotation;
import com.zxq.iov.cloud.trace.AnnotationType;
import com.zxq.iov.cloud.trace.Span;
import com.zxq.iov.cloud.trace.TraceContext;
import com.zxq.iov.cloud.trace.Tracer;
import com.zxq.iov.cloud.trace.dto.MsgWrapperDto;
import com.zxq.iov.cloud.trace.utils.ObjectTransferUtil;

//@Component
//@Aspect
//@Order(0)
public class TraceMqReceiveAop {

//	private static final String PC_AMQP_R = "execution(public* com.zxq.iov.cloud..*MessageListener.onMessage(com.zxq.iov.cloud.trace.dto.MsgWrapperDto))";
//
//	@Around(value = PC_AMQP_R)
	public Object around(ProceedingJoinPoint point) throws Throwable {
		Object result = null;
		Object[] args = point.getArgs();
		Tracer tracer = Tracer.getTracer();
		
		MsgWrapperDto dto = ObjectTransferUtil.getObjFromJson(new String((byte[])args[0], "utf-8"), MsgWrapperDto.class);

		boolean isSample = dto.getIsSample();

		TraceContext context = new TraceContext();
		context.setTraceId(dto.getTraceId());
		context.setIsSample(isSample);
		context.setParentSpanId(dto.getParentSpanId() + ".1");
		context.setIp(IpUtil.getNetworkIp());
		tracer.setTraceContext(context);
		
		Span rootSpan = null;
		Map<String, String[]> parasMap = null;
		if (isSample) {
			rootSpan = new Span(context.getTraceId(), tracer.genCurrentSpanId(context.getParentSpanId()));
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
