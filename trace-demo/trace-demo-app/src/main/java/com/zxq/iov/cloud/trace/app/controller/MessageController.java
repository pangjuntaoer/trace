package com.zxq.iov.cloud.trace.app.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxq.iov.cloud.trace.demo.mq.dto.OTAMessageDemo;
import com.zxq.iov.cloud.trace.demo.mq.iface.MessageServiceApi;

@Controller
@RequestMapping("/message")
public class MessageController {
	
	public MessageController() {
		System.out.println("aaaaaaa");
	}

	@Autowired
	MessageServiceApi messageServiceApi;

	@RequestMapping(value="/send", method=RequestMethod.POST)
	@ResponseBody
	public String sendMessage(HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
		String messages = request.getParameter("message");
		OTAMessageDemo message = new ObjectMapper().readValue(messages, OTAMessageDemo.class);
		messageServiceApi.send(message);
		return "success";
	}
	
	@RequestMapping(value="/send2", method=RequestMethod.POST)
	@ResponseBody
	public String sendMessage2(HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
		String messages = request.getParameter("message");
		OTAMessageDemo message = new ObjectMapper().readValue(messages, OTAMessageDemo.class);
		messageServiceApi.send("trace.in.mq", message);
		return "success";
	}

}
