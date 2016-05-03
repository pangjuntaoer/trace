package com.zxq.iov.cloud.trace.demo.dubbo.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.saicmotor.telematics.framework.core.redis.JedisClusterUtils;
import com.zxq.iov.cloud.trace.demo.dubbo.iface.HelloServiceApi;

@Component
public class HelloServiceApiImpl implements HelloServiceApi {

	@Autowired
	JedisClusterUtils jedisClusterUtils;
	
	public HelloServiceApiImpl(){}

	public String sayHello(String name) {
		jedisClusterUtils.set("username", name);
		return "Hello " + jedisClusterUtils.get("username");
	}

}
