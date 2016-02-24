package com.zxq.iov.cloud.trace;

import com.fasterxml.jackson.core.JsonProcessingException;

public class KafkaProducerTest {
	
	public static void main(String[] args) throws JsonProcessingException {
		KafkaProducer.getInstance().send("send message to logsT queue.");
	}

}
