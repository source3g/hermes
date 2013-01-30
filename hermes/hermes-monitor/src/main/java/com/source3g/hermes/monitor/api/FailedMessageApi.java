package com.source3g.hermes.monitor.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.source3g.hermes.monitor.service.FailedMessageService;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping("/monitor")
public class FailedMessageApi {
	@Autowired
	private FailedMessageService failedMessageService;
	
	
	@RequestMapping(value="failedMessage",method=RequestMethod.GET)
	@ResponseBody
	public Page findFailedMessage(String pageNo){
		Page page= failedMessageService.findAll(pageNo);
		return page;
	}
	

}
