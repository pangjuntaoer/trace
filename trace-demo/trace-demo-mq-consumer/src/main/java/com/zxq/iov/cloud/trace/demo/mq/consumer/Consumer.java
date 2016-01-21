package com.zxq.iov.cloud.trace.demo.mq.consumer;

import java.io.UnsupportedEncodingException;

public class Consumer {

	public void onMessage(String message) {
		System.out.println("string received message: " + message);
	}
	
	public void onMessage(byte[] message) throws UnsupportedEncodingException {
		System.out.println(message);
		System.out.println("byte received message: " + new String(message, "utf-8"));
	}

}
