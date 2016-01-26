package com.zxq.iov.cloud.trace.demo.dubbo.provider;

import org.springframework.beans.factory.annotation.Autowired;

import com.zxq.iov.cloud.trace.demo.dubbo.iface.EmployeeServiceApi;
import com.zxq.iov.cloud.trace.demo.dubbo.model.Employee;
import com.zxq.iov.cloud.trace.demo.dubbo.service.EmployeeService;

public class EmployeeServiceApiImpl implements EmployeeServiceApi {
	
	@Autowired
	EmployeeService employeeService;

	@Override
	public Employee findEmployeeById(Integer id) {
		return employeeService.findEmployeeById(id);
	}

}
