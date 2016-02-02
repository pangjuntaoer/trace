package com.zxq.iov.cloud.trace.filter.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.saicmotor.telematics.framework.core.utils.IpUtil;
import com.zxq.iov.cloud.trace.Annotation;
import com.zxq.iov.cloud.trace.AnnotationType;
import com.zxq.iov.cloud.trace.Span;
import com.zxq.iov.cloud.trace.TraceContext;
import com.zxq.iov.cloud.trace.Tracer;

public class TraceAppFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Tracer tracer = Tracer.getTracer();
		boolean isSample = tracer.isSample();
		String traceId = UUID.randomUUID().toString();
		String parentSpanId = Tracer.DEFAULT_PARENT_SPAN_ID;

		HttpServletRequest req = (HttpServletRequest) request;
		String url = req.getRequestURL().toString();
		TraceContext context = tracer.getTraceContext();
		if (context == null) {
			context = new TraceContext();
			context.setTraceId(traceId);
			context.setIsSample(isSample);
			context.setParentSpanId(parentSpanId);
			context.setIp(IpUtil.getNetworkIp());
			tracer.setTraceContext(context);
		}
		Span span = null;
		Map<String, String[]> parasMap = null;
		if (isSample) {

			parasMap = new HashMap<String, String[]>();
			parasMap.putAll(request.getParameterMap());
			parasMap.put("url", new String[] { url });

			span = new Span(traceId, tracer.genCurrentSpanId(parentSpanId));
			span.setSignature(url);
			span.addAnnotation(new Annotation(AnnotationType.SR.name(), System.currentTimeMillis(), context.getIp(), parasMap));
		}
		try {
			chain.doFilter(request, response);
		} finally {
			if (isSample) {
				span.addAnnotation(
						new Annotation(AnnotationType.SS.name(), System.currentTimeMillis(), context.getIp(), parasMap));
				tracer.sendSpan(span);
			}
			tracer.removeTraceContext();
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}
