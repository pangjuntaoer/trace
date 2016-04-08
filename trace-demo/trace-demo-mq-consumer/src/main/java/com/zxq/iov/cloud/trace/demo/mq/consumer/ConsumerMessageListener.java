package com.zxq.iov.cloud.trace.demo.mq.consumer;

import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zxq.iov.cloud.trace.demo.mq.dto.OTAMessageDemo;

public class ConsumerMessageListener {
	
	public void onMessage(OTAMessageDemo message) throws JsonProcessingException, UnsupportedEncodingException {
		System.out.println(new String(message.getAppData(), JsonEncoding.UTF8.name()));
	}

}
