package com.zxq.iov.cloud.trace.demo.mongo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zxq.iov.cloud.trace.demo.mongo.iface.UserServiceApi;
import com.zxq.iov.cloud.trace.demo.mongo.model.User;
import com.zxq.iov.cloud.trace.demo.mongo.service.repositories.UserRepository;

@Component
public class UserServiceApiImpl implements UserServiceApi {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User findUserByName(String name) {
		return userRepository.findByName(name);
	}

}
