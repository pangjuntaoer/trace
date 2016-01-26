package com.zxq.iov.cloud.trace.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zxq.iov.cloud.trace.app.dto.SystemResponse;
import com.zxq.iov.cloud.trace.demo.dubbo.iface.EmployeeServiceApi;
import com.zxq.iov.cloud.trace.demo.dubbo.model.Employee;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	EmployeeServiceApi employeeServiceApi;

	@RequestMapping("/{id}")
	@ResponseBody
	public SystemResponse findUserByName(@PathVariable Integer id) {
		Employee employee = employeeServiceApi.findEmployeeById(id);
		return new SystemResponse("employee", employee);
	}

}
