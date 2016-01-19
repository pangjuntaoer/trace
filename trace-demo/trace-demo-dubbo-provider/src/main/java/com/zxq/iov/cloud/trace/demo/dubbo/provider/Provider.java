package com.zxq.iov.cloud.trace.demo.dubbo.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Provider {

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("provider.xml");
		context.start();

		System.in.read(); // 为保证服务一直开着，利用输入流的阻塞来模拟
		context.close();
	}

}
