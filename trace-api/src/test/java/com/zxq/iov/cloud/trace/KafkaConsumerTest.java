package com.zxq.iov.cloud.trace;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.saicmotor.telematics.framework.core.utils.JsonUtil;

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

	/**
	 * 
	 * @param appId 应用在resource\config\template.properties文件中配置的WEB_ENV_NAME的值
	 * @param path 本地存储消息的文件
	 */
	private void consume(String appId, String path) {
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, new Integer(1));

		StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
		StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());

		Map<String, List<KafkaStream<String, String>>> consumerMap = consumer.createMessageStreams(topicCountMap,
				keyDecoder, valueDecoder);
		KafkaStream<String, String> stream = consumerMap.get(topic).get(0);
		ConsumerIterator<String, String> it = stream.iterator();
		long cnt = 0;

		while (it.hasNext()) {
			Span span = JsonUtil.getObjFromJson(it.next().message(), Span.class);
			if (appId.equals(span.getAppId())) {
				String s = "recived " + ++cnt + " messages. appid: " + span.getAppId() + ", signaure: "
						+ span.getSignature();
				System.out.println(s);
				writeToTxtByOutputStream(new File(path), s + "\n");
			}
		}
	}

	public static void writeToTxtByOutputStream(File file, String content) {
		try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file, true));) {
			bufferedOutputStream.write(content.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new KafkaConsumerTest().consume("s_dubbo_provider", "d:\\log\\kafka-msg.txt");
	}
}