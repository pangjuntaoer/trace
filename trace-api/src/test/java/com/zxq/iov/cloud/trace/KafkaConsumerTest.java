package com.zxq.iov.cloud.trace;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

public class KafkaConsumerTest {

	private final ConsumerConnector consumer;
	
	private String topic;

	private KafkaConsumerTest() {
		Properties props = new Properties();

		try (InputStream is = KafkaProducer.class.getResourceAsStream("/kafka-consumer-config.properties")) {
			props.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		topic = props.getProperty("topic");
		
		Properties properties = new Properties();
		
		properties.put("zookeeper.connect", props.getProperty("zookeeper_connect"));
		properties.put("group.id", props.getProperty("group_id"));
		properties.put("zookeeper.session.timeout.ms", props.getProperty("zookeeper_session_timeout_ms"));
		properties.put("zookeeper.sync.time.ms", props.getProperty("zookeeper_sync_time_ms"));
		properties.put("auto.commit.interval.ms", props.getProperty("auto_commit_interval_ms"));
		properties.put("auto.offset.reset", props.getProperty("auto_offset_reset"));
		properties.put("serializer.class", props.getProperty("serializer_class"));
		

		ConsumerConfig config = new ConsumerConfig(properties);

		consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);
	}

	private void consume() {
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, new Integer(1));

		StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
		StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());

		Map<String, List<KafkaStream<String, String>>> consumerMap = consumer.createMessageStreams(topicCountMap,
				keyDecoder, valueDecoder);
		KafkaStream<String, String> stream = consumerMap.get(topic).get(0);
		ConsumerIterator<String, String> it = stream.iterator();
		while (it.hasNext()) {
			System.out.println("recived messages: " + it.next().message());
		}
	}

	public static void main(String[] args) {
		new KafkaConsumerTest().consume();
	}
}