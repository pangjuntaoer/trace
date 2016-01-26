package com.zxq.iov.cloud.trace.demo.dubbo.iface;

import com.zxq.iov.cloud.trace.demo.dubbo.model.Employee;

public interface EmployeeServiceApi {

	public Employee findEmployeeById(Integer id);
	
}
