package com.zxq.iov.cloud.trace.demo.mq.consumer;

import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zxq.iov.cloud.trace.demo.mq.dto.OTAMessageDemo;

public class ConsumerMessageListener {

	public void onMessage(OTAMessageDemo message) throws JsonProcessingException, UnsupportedEncodingException {
		long end = System.currentTimeMillis();
		long start = message.getEventCreationTime();
		long seconds = (end - start) / 1000;
		System.out.println(new String(message.getAppData(), JsonEncoding.UTF8.name()) + ", This message is sended at "
				+ seconds + " before.");
	}

}
