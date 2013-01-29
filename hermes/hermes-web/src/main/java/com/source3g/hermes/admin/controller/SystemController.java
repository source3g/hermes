package com.source3g.hermes.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.entity.log.FailedMessage;
import com.source3g.hermes.utils.ConfigParams;

@Controller
@RequestMapping(value="/admin/system")
public class SystemController {
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value="/monitor/failedJms", method = RequestMethod.GET)
	public ModelAndView findFailedJms(){
		String uri=ConfigParams.getBaseUrl()+"monitor/failedMessage/";
		FailedMessage[] failedMessages=restTemplate.getForObject(uri, FailedMessage[].class);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("failedMessages", failedMessages);
		return new ModelAndView("/admin/system/failedJmsList",map);
	}
	
	@RequestMapping(value="/operateLog")
	public ModelAndView findOperatelog(){
		return new ModelAndView("/admin/system/operateLogList");
	}
	
}
