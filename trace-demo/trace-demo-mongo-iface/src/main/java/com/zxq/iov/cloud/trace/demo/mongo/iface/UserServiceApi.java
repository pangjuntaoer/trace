package com.zxq.iov.cloud.trace.demo.mongo.iface;

import com.zxq.iov.cloud.trace.demo.mongo.model.User;

public interface UserServiceApi {
	
	public User findUserByName(String name);

}
