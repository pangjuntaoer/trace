package com.zxq.iov.cloud.trace.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zxq.iov.cloud.trace.app.dto.SystemResponse;
import com.zxq.iov.cloud.trace.demo.mongo.iface.UserServiceApi;
import com.zxq.iov.cloud.trace.demo.mongo.model.User;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserServiceApi userServiceApi;

	@RequestMapping("/{name}")
	@ResponseBody
	public SystemResponse findUserByName(@PathVariable String name) {
		User user = userServiceApi.findUserByName(name);
		return new SystemResponse("user", user);
	}

}
