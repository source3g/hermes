package com.source3g.hermes.message.api;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.message.MessageAutoSend;
import com.source3g.hermes.entity.message.MessageTemplate;
import com.source3g.hermes.message.service.MessageService;
import com.source3g.hermes.utils.Page;

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

	@RequestMapping(value = "/messageSend/{merchantId}", method = RequestMethod.POST)
	@ResponseBody
	public String messageSend(@PathVariable String merchantId, String[] ids, String customerPhones,String content) {
		try {
			messageService.messageGroupSend(new ObjectId(merchantId), ids, customerPhones,content);
		} catch (Exception e) {
			return e.getMessage();
		}
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/fastSend/{merchantId}", method = RequestMethod.POST)
	@ResponseBody
	public String fastSend(@PathVariable String merchantId, String customerPhones, String content) {
		String customerPhoneArray[] = customerPhones.split(";");
		try {
			messageService.sendMessages(new ObjectId(merchantId), customerPhoneArray, content);
		} catch (Exception e) {
			return e.getMessage();
		}
		
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/messageSendLog/{merchantId}/list", method = RequestMethod.GET)
	@ResponseBody
	public Page listMessageSendLog(@PathVariable String merchantId, String pageNo, Date startTime, Date endTime, String phone, String customerGroupName) {
		int pageNoInt = Integer.valueOf(pageNo);
		return messageService.list(pageNoInt, new ObjectId(merchantId), startTime, endTime, phone, customerGroupName);
	}

	@RequestMapping(value = "/autoSend/messageInfo/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public MessageAutoSend getMessageAutoSend(@PathVariable String merchantId) {
		return messageService.getMessageAutoSend(new ObjectId(merchantId));
	}
	
	@RequestMapping(value = "/autoSend/messageInfo", method = RequestMethod.POST)
	@ResponseBody
	public String autoSend(@RequestBody MessageAutoSend messageAutoSend) {
		messageService.saveMessageAutoSend(messageAutoSend);
		return ReturnConstants.SUCCESS;
	}
}
