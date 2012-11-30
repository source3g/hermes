package com.source3g.hermes.message.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.message.MessageTemplate;
import com.source3g.hermes.message.service.MessageService;

@Controller
@RequestMapping(value = "/shortMessage")
public class MessageApi {

	@Autowired
	private MessageService messageService;

	@RequestMapping(value = "/template/add", method = RequestMethod.POST)
	@ResponseBody
	public String addTemplate(@RequestBody MessageTemplate messageTemplate) {
		messageService.add(messageTemplate);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/template/list/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public List<MessageTemplate> listTemplate(@PathVariable String merchantId) {
		return messageService.listAll(merchantId);
	}
}
