package com.zxq.iov.cloud.trace.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SystemResponse {

	@JsonProperty("req_id")
	private String reqId;

	private Object data;

	public SystemResponse(String reqId, Object data) {
		this.reqId = reqId;
		this.data = data;
	}

	public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
