package com.zxq.iov.cloud.trace.demo.dubbo.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zxq.iov.cloud.trace.demo.dubbo.iface.HelloServiceApi;
import com.zxq.iov.cloud.trace.utils.JedisClusterUtils;

@Component
public class HelloServiceApiImpl implements HelloServiceApi {

	@Autowired
	JedisClusterUtils jedisClusterUtils;

	public String sayHello(String name) {
		System.out.println("------------use redis---------");
		jedisClusterUtils.set("username", name);
		return "Hello " + jedisClusterUtils.get("username");
	}

}
