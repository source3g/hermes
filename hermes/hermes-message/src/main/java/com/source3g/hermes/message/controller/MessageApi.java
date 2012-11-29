package com.source3g.hermes.message.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.message.MessageTemplate;
import com.source3g.hermes.message.service.MessageService;

@Controller
@RequestMapping(value = "/shortMessage")
public class MessageApi {

	@Autowired
	private MessageService messageService;
	
	

	@RequestMapping(value = "template", method = RequestMethod.POST)
	public String addTemplate(MessageTemplate messageTemplate) {
		messageService.add(messageTemplate);
		return ReturnConstants.SUCCESS;
	}

}
