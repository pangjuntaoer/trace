package com.zxq.iov.cloud.trace.demo.dubbo.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.zxq.iov.cloud.trace.demo.dubbo.dao.EmployeeDao;
import com.zxq.iov.cloud.trace.demo.dubbo.model.Employee;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {
	
	@Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;
	
	public Employee findEmployeeById(Integer id) {
		return (Employee) sessionFactory.getCurrentSession().get(Employee.class, id);
	}

}
