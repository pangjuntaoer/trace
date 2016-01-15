package com.zxq.iov.cloud.trace.web.controller;

import org.dubbo.iface.DubboTestServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zxq.iov.cloud.trace.web.dto.SystemResponse;

@Controller
@RequestMapping("/test")
public class TestController {

	@Autowired
	DubboTestServiceApi dubboTestServiceApi;

	@RequestMapping("/{name}")
	@ResponseBody
	public SystemResponse hello(@PathVariable String name) {
		String hello = dubboTestServiceApi.sayHello(name);
		return new SystemResponse("hello", hello);
	}

}
