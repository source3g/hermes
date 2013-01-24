package com.source3g.hermes.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/message")
public class MessageAdminController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value="/failed/list")
	public ModelAndView toFailedList(){
		return new ModelAndView("/admin/shortMessage/failedMessageList");
	}

}
