package com.zxq.iov.cloud.trace.demo.dubbo.dao;

import com.zxq.iov.cloud.trace.demo.dubbo.model.Employee;

public interface EmployeeDao {
	
	public Employee findEmployeeById(Integer id);

}
