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
import com.zxq.iov.cloud.trace.TraceContext;
import com.zxq.iov.cloud.trace.Tracer;

@Activate(group = { Constants.PROVIDER })
public class TraceDubboProviderFilter implements Filter {

	private Tracer tracer = Tracer.getTracer();

	// 调用过程拦截
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

		System.out.println("--------------TraceDubboProviderFilter start------------------------"
				+ System.currentTimeMillis());
		Span span = null;
		RpcContext rc = RpcContext.getContext();

		boolean isSample = Boolean.valueOf(rc.getAttachment("isSample"));
		String traceId = rc.getAttachment("traceId");
		String parentSpanId = rc.getAttachment("parentSpanId");
		System.out.println("traceId: " + traceId);
		System.out.println("isSample: " + isSample);
		System.out.println("parentSpanId: " + parentSpanId);
		try {
			TraceContext tc = tracer.getTraceContext();
			if (tc == null) {
				tc = new TraceContext();
				tc.setTraceId(traceId);
				tc.setIsSample(isSample);
				tc.setParentSpanId(parentSpanId);
				tc.setCurrentSpanId(parentSpanId);
				tracer.setTraceContext(tc);
			}

			if (isSample) {
				span = new Span(traceId, parentSpanId);
				span.setSignature(invoker.getUrl().toFullString());
				span.addAnnotation(new Annotation(AnnotationType.SR.name(), System.currentTimeMillis(),
						IpUtil.getNetworkIp(), null));
			}

			Result result = invoker.invoke(invocation);
			if (isSample && result.getException() != null) {
				span.addBinaryAnnotation(
						new BinaryAnnotation("exception", System.currentTimeMillis(), IpUtil.getNetworkIp(), null));
			}
			return result;
		} finally {
			if (isSample) {
				span.setSignature(invoker.getUrl().toFullString());
				span.addAnnotation(new Annotation(AnnotationType.SS.name(), System.currentTimeMillis(),
						IpUtil.getNetworkIp(), null));
				tracer.sendSpan(span);
			}
			tracer.removeTraceContext();
			System.out.println(
					"-------------TraceDubboProviderFilter end-----------------------" + System.currentTimeMillis());
		}
	}

}