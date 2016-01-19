package com.zxq.iov.cloud.trace.demo.dubbo.provider;

import java.io.IOException;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class ProviderTest extends AbstractJUnit4SpringContextTests {

	@Test
	public void testSongSearch() throws IOException {
		System.in.read(); // 为保证服务一直开着，利用输入流的阻塞来模拟
	}

}
