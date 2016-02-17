package com.zxq.iov.cloud.trace.demo.mongo.repositories;

import com.zxq.iov.cloud.trace.demo.mongo.model.User;

public interface UserRepository extends BaseRepository<User, String> {
	User findByName(String name);
}
