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
	@RequestMapping(value = "/template/save", method = RequestMethod.POST)
	@ResponseBody
	public String saveTemplate(@RequestBody MessageTemplate messageTemplate) {
		messageService.save(messageTemplate);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/template/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String saveTemplate(@PathVariable String id) {
		messageService.deleteById(id, MessageTemplate.class);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/template/list/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public List<MessageTemplate> listTemplate(@PathVariable String merchantId) {
		return messageService.listAll(merchantId);
	}
	@RequestMapping(value = "/messageSend", method = RequestMethod.POST)
	@ResponseBody
	public String messageSend(String[] ids ,String content) {
		messageService.messageSend(ids, content);
		return ReturnConstants.SUCCESS;
	}
	@RequestMapping(value = "/fastSend", method = RequestMethod.POST)
	@ResponseBody
	public String fastSend(String type,String customerPhones,String content) {
		if(type.equals("enter")){
			String customerPhoneArray[]=customerPhones.split("\n");
			messageService.fastSend( type, customerPhoneArray, content);
		}else if(type.equals("semicolon")){
			String customerPhoneArray[]=customerPhones.split(";");
			messageService.fastSend( type, customerPhoneArray, content);	
		}
		return ReturnConstants.SUCCESS;
	}
}
