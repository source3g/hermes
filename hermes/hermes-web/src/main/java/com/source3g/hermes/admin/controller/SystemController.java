package com.source3g.hermes.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping(value="/admin/system")
public class SystemController {
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value="/monitor/failedJms", method = RequestMethod.GET)
	public ModelAndView findFailedJms(String pageNo){
		if(StringUtils.isEmpty(pageNo)){
			pageNo="1";
		}
		String uri=ConfigParams.getBaseUrl()+"monitor/failedMessage/?pageNo="+pageNo;
		Page page=restTemplate.getForObject(uri, Page.class);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("page", page);
		return new ModelAndView("/admin/system/failedJmsList",map);
	}
	
	@RequestMapping(value="/operateLog")
	public ModelAndView findOperatelog(){
		return new ModelAndView("/admin/system/operateLogList");
	}
	
	@RequestMapping(value="/failedMessage/sendAgain/{id}", method = RequestMethod.GET)
	public ModelAndView filedMessageSendAgain(@PathVariable String id){
		
		return new ModelAndView("redirect:/admin/system/list/failedMessage/sendAgain/");
	}
}
