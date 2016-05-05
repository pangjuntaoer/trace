package com.zxq.iov.cloud.trace;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.SslConfigs;

public class KafkaConsumerTest {

	public static void main(String[] args) {
		Properties prop = new Properties();

		try (InputStream is = KafkaConsumerTest.class.getResourceAsStream("/kafka-consumer-config.properties")) {
			prop.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String topic = prop.getProperty("topic");

		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, prop.getProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
		props.put(ConsumerConfig.GROUP_ID_CONFIG, prop.getProperty(ConsumerConfig.GROUP_ID_CONFIG));
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, prop.getProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG));
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,
				prop.getProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG));
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, prop.getProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG));
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				prop.getProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				prop.getProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG));

		String realPath = KafkaConsumerTest.class.getResource("/").getPath().substring(1);
		// SSL
		//Client authentication is not required in the broker, This is a minimal configuration
				props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,
						prop.getProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG));
				props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
						realPath + prop.getProperty(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG));
				props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,
						prop.getProperty(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG));
				
		//Client authentication is required
//		props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG,
//				realPath + prop.getProperty(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG));
//		props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, prop.getProperty(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG));
//		props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, prop.getProperty(SslConfigs.SSL_KEY_PASSWORD_CONFIG));

		

		@SuppressWarnings("resource")
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList(topic));

		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(5);
			for (ConsumerRecord<String, String> record : records)
				System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value() + "\n");
		}
	}

}
