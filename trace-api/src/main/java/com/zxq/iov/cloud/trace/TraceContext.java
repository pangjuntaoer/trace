package com.zxq.iov.cloud.trace;

import java.util.HashMap;
import java.util.Map;

public class TraceContext {
	
	private String traceId;
	
	private String parentSpanId;
	
	private String currentSpanId;
	
	private Boolean isSample;
		
	private String ip;
	
	private Map<String, Span> spanMap = new HashMap<String, Span>();
	
	public void putSpan(Span span) {
		spanMap.put(span.getSpanId(), span);
	}
	
	public Span getSpan(String spanId) {
		return spanMap.get(spanId);
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getCurrentSpanId() {
		return currentSpanId;
	}

	public String getParentSpanId() {
		return parentSpanId;
	}

	public void setParentSpanId(String parentSpanId) {
		this.parentSpanId = parentSpanId;
	}

	public void setCurrentSpanId(String currentSpanId) {
		this.currentSpanId = currentSpanId;
	}

	public Boolean getIsSample() {
		return isSample;
	}

	public void setIsSample(Boolean isSample) {
		this.isSample = isSample;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Map<String, Span> getSpanMap() {
		return spanMap;
	}

	public void setSpanMap(Map<String, Span> spanMap) {
		this.spanMap = spanMap;
	}

}
