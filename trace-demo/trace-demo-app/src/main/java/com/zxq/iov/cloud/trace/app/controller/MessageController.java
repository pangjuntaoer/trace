package com.zxq.iov.cloud.trace.app.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxq.iov.cloud.trace.demo.mq.iface.MessageServiceApi;
import com.zxq.iov.cloud.trace.dto.OTAMessage;

@Controller
@RequestMapping("/message")
public class MessageController {
	
	public MessageController() {
		System.out.println("aaaaaaa");
	}

	@Autowired
	MessageServiceApi messageServiceApi;

	@RequestMapping(value="/send", method=RequestMethod.POST)
	public String sendMessage(HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
		String messages = request.getParameter("message");
		OTAMessage message = new ObjectMapper().readValue(messages, OTAMessage.class);
		messageServiceApi.send(message);
		return "success";
	}

}
