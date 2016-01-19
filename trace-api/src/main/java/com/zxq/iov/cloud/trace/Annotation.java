package com.zxq.iov.cloud.trace;

import java.util.Map;

public class Annotation {
	
	public Annotation() {
	}
	
	public Annotation(String type, long timestamp, String ip, Map<String, String[]> parasMap) {
		this.type = type;
		this.timestamp = timestamp;
		this.ip = ip;
		this.parasMap = parasMap;
	}
	
	private String type;
	
	private long timestamp;
	
	private String ip;
	
	private Map<String, String[]> parasMap;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Map<String, String[]> getParasMap() {
		return parasMap;
	}

	public void setParasMap(Map<String, String[]> parasMap) {
		this.parasMap = parasMap;
	}

}
