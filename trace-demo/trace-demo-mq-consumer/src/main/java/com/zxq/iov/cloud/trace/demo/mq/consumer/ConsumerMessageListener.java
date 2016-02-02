package com.zxq.iov.cloud.trace.demo.mq.consumer;

import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zxq.iov.cloud.trace.dto.OTAMessageDemo;
import com.zxq.iov.cloud.trace.utils.ObjectTransferUtil;

public class ConsumerMessageListener {
	
	public void onMessage(byte[] message) throws UnsupportedEncodingException, JsonProcessingException {
		OTAMessageDemo m = ObjectTransferUtil.getObjFromJson(new String(message, JsonEncoding.UTF8.name()), "message", OTAMessageDemo.class);
		onMessage(m);
	}
	
	private void onMessage(OTAMessageDemo message) throws JsonProcessingException, UnsupportedEncodingException {
		System.out.println(new String(message.getAppData(), JsonEncoding.UTF8.name()));
	}

}
