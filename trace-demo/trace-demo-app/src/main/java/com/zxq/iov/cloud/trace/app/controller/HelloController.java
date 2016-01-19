package com.zxq.iov.cloud.trace.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zxq.iov.cloud.trace.app.dto.SystemResponse;
import com.zxq.iov.cloud.trace.demo.dubbo.iface.HelloServiceApi;

@Controller
@RequestMapping("/hello")
public class HelloController {

	@Autowired
	HelloServiceApi helloServiceApi;

	@RequestMapping("/{name}")
	@ResponseBody
	public SystemResponse hello(@PathVariable String name) {
		String hello = helloServiceApi.sayHello(name);
		return new SystemResponse("hello", hello);
	}

}
