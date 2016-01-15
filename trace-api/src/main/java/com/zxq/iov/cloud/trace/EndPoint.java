package com.zxq.iov.cloud.trace;

public class EndPoint {
	
	public EndPoint() {
		
	}
	
	public EndPoint(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	private String ip;
	
	private int port;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
