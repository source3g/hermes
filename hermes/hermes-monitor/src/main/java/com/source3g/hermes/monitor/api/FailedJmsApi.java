package com.source3g.hermes.monitor.api;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.source3g.hermes.monitor.service.FailedJmsService;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping("/monitor")
public class FailedJmsApi {
	@Autowired
	private FailedJmsService failedJmsService;
	
	
	@RequestMapping(value="failedJms")
	@ResponseBody
	public Page findFailedJms(String pageNo){
		Page page= failedJmsService.findAll(pageNo);
		return page;
	}
	
	@RequestMapping(value="failedJms/resend/{id}")
	@ResponseBody
	public String resendfailedJms(@PathVariable String id){
		return failedJmsService.resendfailedJms(new ObjectId(id));
	}
	
	@RequestMapping(value="failedJms/gorupResend", method = RequestMethod.GET)
	@ResponseBody
	public String groupResendfailedJms(){
		return failedJmsService.groupResendfailedJms();
	}
}
