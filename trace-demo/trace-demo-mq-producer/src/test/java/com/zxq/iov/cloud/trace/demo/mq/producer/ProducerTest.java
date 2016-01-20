package com.zxq.iov.cloud.trace.demo.mq.producer;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:applicationContext-dubbo.xml",
		"classpath:applicationContext-mq.xml" })
public class ProducerTest {

	@Test
	public void producer() throws IOException {
		System.in.read();
	}

}
