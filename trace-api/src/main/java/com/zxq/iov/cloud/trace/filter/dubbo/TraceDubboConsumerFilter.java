package com.zxq.iov.cloud.trace.filter.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.zxq.iov.cloud.trace.Annotation;
import com.zxq.iov.cloud.trace.AnnotationType;
import com.zxq.iov.cloud.trace.BinaryAnnotation;
import com.zxq.iov.cloud.trace.Span;
import com.zxq.iov.cloud.trace.TraceContext;
import com.zxq.iov.cloud.trace.Tracer;

@Activate(group = { Constants.CONSUMER })
public class TraceDubboConsumerFilter implements Filter {

	private Tracer tracer = Tracer.getTracer();

	// 调用过程拦截
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		TraceContext context = tracer.getTraceContext();
		boolean isSample = context.getIsSample();

		Span span = null;
		if (isSample) {
			String currentSpanId = tracer.genCurrentSpanId(context.getParentSpanId());
			String traceId = context.getTraceId();
			span = new Span(traceId, currentSpanId);
			span.setSignature(invoker.getUrl().toFullString());
			span.addAnnotation(
					new Annotation(AnnotationType.CS.name(), System.currentTimeMillis(), context.getIp(), null));
			context.putSpan(span);
		}
		try {
			RpcInvocation invocation1 = (RpcInvocation) invocation;
			invocation1.setAttachment("traceId", context.getTraceId());
			invocation1.setAttachment("isSample", String.valueOf(isSample));
			invocation1.setAttachment("parentSpanId", context.getCurrentSpanId());
			Result result = invoker.invoke(invocation);
			if (isSample && result.getException() != null) {
				span.addBinaryAnnotation(
						new BinaryAnnotation("exception", System.currentTimeMillis(), context.getIp(), null));
			}
			return result;
		} finally {
			if (isSample) {
				span.setSignature(invoker.getUrl().toFullString());
				span.addAnnotation(new Annotation(AnnotationType.CR.name(), System.currentTimeMillis(),
						context.getIp(), null));
				tracer.sendSpan(span);
			}
		}
	}

}