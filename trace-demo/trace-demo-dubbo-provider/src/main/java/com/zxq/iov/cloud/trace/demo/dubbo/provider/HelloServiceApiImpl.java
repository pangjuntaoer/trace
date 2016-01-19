package com.zxq.iov.cloud.trace.demo.dubbo.provider;

import com.zxq.iov.cloud.trace.demo.dubbo.iface.HelloServiceApi;

public class HelloServiceApiImpl implements HelloServiceApi {

	public String sayHello(String name) {
		return "Hello " + name;
	}

}
