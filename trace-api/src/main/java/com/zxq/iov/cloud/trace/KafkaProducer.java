package com.zxq.iov.cloud.trace;

import java.io.InputStream;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class KafkaProducer {

	private Producer<String, String> producer;
	
	private String topic;

	private static KafkaProducer kafkaProducer = new KafkaProducer();

	public static KafkaProducer getInstance() {
		return kafkaProducer;
	}

	private KafkaProducer() {
		Properties props = new Properties();

		try (InputStream is = KafkaProducer.class.getResourceAsStream("/kafka-producer-config.properties")) {
			props.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		topic = props.getProperty("topic");
		
		Properties properties = new Properties();
		
		properties.setProperty("metadata.broker.list", props.getProperty("metadata_broker_list"));
		properties.setProperty("serializer.class", props.getProperty("serializer_class"));
		properties.setProperty("key.serializer.class", props.getProperty("key_serializer_class"));
		properties.setProperty("request.required.acks", props.getProperty("request_required_acks"));
		
		producer = new Producer<String, String>(new ProducerConfig(properties));
	}

	public void send(Span span) throws JsonProcessingException {
		String value = new ObjectMapper().writeValueAsString(span);
		producer.send(new KeyedMessage<String, String>(topic, span.getTraceId(), value));
	}

}
