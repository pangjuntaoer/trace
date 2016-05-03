package com.zxq.iov.cloud.trace.demo.dubbo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zxq.iov.cloud.trace.demo.dubbo.dao.EmployeeDao;
import com.zxq.iov.cloud.trace.demo.dubbo.model.Employee;
import com.zxq.iov.cloud.trace.demo.dubbo.service.EmployeeService;

@Service("employeeService")
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeDao employeeDao;

	@Override
	public Employee findEmployeeById(Integer id) {
		return employeeDao.findEmployeeById(id);
	}

}
