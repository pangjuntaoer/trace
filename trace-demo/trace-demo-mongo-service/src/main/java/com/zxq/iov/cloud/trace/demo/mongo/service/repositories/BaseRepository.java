package com.zxq.iov.cloud.trace.demo.mongo.service.repositories;

import java.io.Serializable;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
interface BaseRepository<T, ID extends Serializable> extends Repository<T, ID> {

	T findOne(ID id);

	T save(T entity);
	
	void delete(T entity);
	
	Iterable<T> findAll();
}
