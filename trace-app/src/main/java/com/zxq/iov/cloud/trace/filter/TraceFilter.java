package com.zxq.iov.cloud.trace.filter;

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

import com.zxq.iov.cloud.trace.Annotation;
import com.zxq.iov.cloud.trace.AnnotationType;
import com.zxq.iov.cloud.trace.Span;
import com.zxq.iov.cloud.trace.TraceContext;
import com.zxq.iov.cloud.trace.Tracer;

public class TraceFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Tracer tracer = Tracer.getTracer();
		boolean isSample = tracer.isSample();
		String traceId = UUID.randomUUID().toString();
		String parentSpanId = Tracer.DEFAULT_PARENT_SPAN_ID;

		HttpServletRequest req = (HttpServletRequest) request;
		String url = req.getRequestURL().toString();
		TraceContext tc = tracer.getTraceContext();
		if (tc == null) {
			tc = new TraceContext();
			tc.setTraceId(traceId);
			tc.setIsSample(isSample);
			tc.setParentSpanId(parentSpanId);
			tc.setCurrentSpanId(parentSpanId);
			tracer.setTraceContext(tc);
		}
		Span rootSpan = null;
		String ip = null;
		Map<String, String[]> parasMap = null;
		if (isSample) {
			ip = request.getLocalAddr();
			tc.setIp(ip);

			parasMap = new HashMap<String, String[]>();
			parasMap.putAll(request.getParameterMap());
			parasMap.put("url", new String[] { url });

			rootSpan = new Span(traceId, parentSpanId);
			rootSpan.addAnnotation(new Annotation(AnnotationType.SR.name(), System.currentTimeMillis(), ip, parasMap));
		}
		try {
			chain.doFilter(request, response);
		} finally {
			if (isSample) {
				rootSpan.addAnnotation(
						new Annotation(AnnotationType.SS.name(), System.currentTimeMillis(), ip, parasMap));
				tracer.sendSpan(rootSpan);
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
