package com.zxq.iov.cloud.trace.demo.mq.iface;

public interface MessageServiceApi {

	public void send(Object message);

	public void send(String routingKey, Object message);

}
