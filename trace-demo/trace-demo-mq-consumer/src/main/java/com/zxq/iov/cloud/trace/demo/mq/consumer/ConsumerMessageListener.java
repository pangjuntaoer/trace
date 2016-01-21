package com.zxq.iov.cloud.trace.demo.mq.consumer;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.zxq.iov.cloud.trace.dto.MsgWrapperDto;
import com.zxq.iov.cloud.trace.dto.OTAMessage;
import com.zxq.iov.cloud.trace.utils.ObjectTransferUtil;

public class ConsumerMessageListener {

	public void onMessage(String message) {
		System.out.println("string received message: " + message);
	}
	
	public void onMessage(byte[] message) throws UnsupportedEncodingException {
		System.out.println(message);
		System.out.println("byte received message: " + new String(message, "utf-8"));
	}
	
	@SuppressWarnings("unchecked")
	public void onMessage(MsgWrapperDto dto) throws Exception {
    	OTAMessage message = ObjectTransferUtil.convertMap(OTAMessage.class, (Map<String,Object>)(dto.getMessage()));
    	onMessage(message);
    }
	
	private void onMessage(OTAMessage message) {
		System.out.println(message);
	}
    
    

}
