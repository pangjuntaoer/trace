package com.zxq.iov.cloud.trace.filter.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.saicmotor.telematics.framework.core.utils.IpUtil;
import com.zxq.iov.cloud.trace.Annotation;
import com.zxq.iov.cloud.trace.AnnotationType;
import com.zxq.iov.cloud.trace.BinaryAnnotation;
import com.zxq.iov.cloud.trace.Span;
import com.zxq.iov.cloud.trace.TraceConstant;
import com.zxq.iov.cloud.trace.TraceContext;
import com.zxq.iov.cloud.trace.Tracer;

@Activate(group = { Constants.PROVIDER })
public class TraceDubboProviderFilter implements Filter {

	private Tracer tracer = Tracer.getTracer();

	// 调用过程拦截
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		Span span = null;
		RpcContext rc = RpcContext.getContext();

		boolean isSample = Boolean.valueOf(rc.getAttachment(TraceConstant.IS_SAMPLE));
		String traceId = rc.getAttachment(TraceConstant.TRACE_ID);
		String parentSpanId = rc.getAttachment(TraceConstant.PARENT_SPAN_ID);
		TraceContext context = tracer.getTraceContext();
		if (context == null) {
			context = new TraceContext();
			context.setTraceId(traceId);
			context.setIsSample(isSample);
			context.setParentSpanId(parentSpanId);
			context.setIp(IpUtil.getNetworkIp());
			tracer.setTraceContext(context);
		}

		if (isSample) {
			String currentSpanId = tracer.genCurrentSpanId(parentSpanId);
			span = new Span(traceId, currentSpanId);
			span.setSignature(invoker.getUrl().toFullString());
			span.addAnnotation(
					new Annotation(AnnotationType.SR.name(), System.currentTimeMillis(), context.getIp(), null));
		}
		try {
			Result result = invoker.invoke(invocation);
			if (isSample && result.getException() != null) {
				span.addBinaryAnnotation(new BinaryAnnotation(TraceConstant.EXCEPTION, System.currentTimeMillis(),
						IpUtil.getNetworkIp(), null));
			}
			return result;
		} finally {
			if (isSample) {
				span.setSignature(invoker.getUrl().toFullString());
				span.addAnnotation(
						new Annotation(AnnotationType.SS.name(), System.currentTimeMillis(), context.getIp(), null));
				tracer.sendSpan(span);
			}
			tracer.removeTraceContext();
		}
	}

}