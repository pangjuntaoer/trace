package com.zxq.iov.cloud.trace.demo.mq.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.zxq.iov.cloud.trace.demo.mq.iface.MessageServiceApi;

public class MessageServiceApiImpl implements MessageServiceApi {

	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Override
	public void send(Object message) {
		amqpTemplate.convertAndSend("foo.bar", message);
	}

}
