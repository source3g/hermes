package com.source3g.hermes.customer.api;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/user")
@Controller
public class UserController {
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private Destination customerDestination;
	
	public UserController() {
	}

	@RequestMapping(value = "/find", method = {RequestMethod.GET})
	@ResponseBody
	public String findUser(){
		System.out.println("prepare to send.."+System.currentTimeMillis());
		jmsTemplate.send(customerDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("send message ");  
			}
		});
		return "OK";
	}
}
