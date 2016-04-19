package com.zxq.iov.cloud.trace;

import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saicmotor.telematics.framework.core.common.SpringContext;

public class KafkaProducer {

	private Producer<String, String> producer;

	private static ObjectMapper mapper = new ObjectMapper();

	private String topic;

	private static KafkaProducer kafkaProducer = new KafkaProducer();

	public static KafkaProducer getInstance() {
		return kafkaProducer;
	}

	private KafkaProducer() {
		SpringContext sc = SpringContext.getInstance();
		topic = sc.getProperty("topic");

		Properties prop = new Properties();

		prop.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,
				sc.getProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG));
		
		prop.put(ProducerConfig.ACKS_CONFIG, sc.getProperty(ProducerConfig.ACKS_CONFIG));
		
		prop.put(ProducerConfig.RETRIES_CONFIG, sc.getProperty(ProducerConfig.RETRIES_CONFIG));
		
		prop.put(ProducerConfig.BATCH_SIZE_CONFIG, sc.getProperty(ProducerConfig.BATCH_SIZE_CONFIG));
		
		prop.put(ProducerConfig.LINGER_MS_CONFIG, sc.getProperty(ProducerConfig.LINGER_MS_CONFIG));
		
		prop.put(ProducerConfig.BUFFER_MEMORY_CONFIG, sc.getProperty(ProducerConfig.BUFFER_MEMORY_CONFIG));
		
		prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				sc.getProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG));
		
		prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				sc.getProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG));

		producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(prop);
	}

	public void send(Span span) throws JsonProcessingException {
		producer.send(new ProducerRecord<String, String>(topic, span.getTraceId(), mapper.writeValueAsString(span)));
	}

	public void send(String message) {
		producer.send(
				new ProducerRecord<String, String>(topic, UUID.randomUUID().toString().substring(0, 10), message));
	}

}
