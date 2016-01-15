package com.zxq.iov.cloud.trace.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MsgWrapperDto implements Serializable {

	public MsgWrapperDto() {
	}

	public MsgWrapperDto(String traceId, boolean isSample, String parentSpanId, Object message) {
		this.isSample = isSample;
		this.traceId = traceId;
		this.parentSpanId = parentSpanId;
		this.message = message;
	}

	private String traceId;

	private boolean isSample;

	private String parentSpanId;

	private Object message;

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public boolean getIsSample() {
		return isSample;
	}

	public void setIsSample(boolean isSample) {
		this.isSample = isSample;
	}

	public String getParentSpanId() {
		return parentSpanId;
	}

	public void setParentSpanId(String parentSpanId) {
		this.parentSpanId = parentSpanId;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

}
