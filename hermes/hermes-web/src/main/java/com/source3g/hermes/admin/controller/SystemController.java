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

import com.source3g.hermes.constants.ReturnConstants;
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
		String uri=ConfigParams.getBaseUrl()+"monitor/failedJms/?pageNo="+pageNo;
		Page page=restTemplate.getForObject(uri, Page.class);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("page", page);
		return new ModelAndView("/admin/system/failedJmsList",map);
	}
	
	@RequestMapping(value="/operateLog")
	public ModelAndView findOperatelog(){
		return new ModelAndView("/admin/system/operateLogList");
	}
	
	@RequestMapping(value="/failedJms/resend/{id}", method = RequestMethod.GET)
	public ModelAndView resendfailedJms(@PathVariable String id){
		String uri=ConfigParams.getBaseUrl()+"monitor/failedJms/resend/"+id+"/";
		String result=restTemplate.getForObject(uri, String.class);
		if(ReturnConstants.SUCCESS.equals(result)){
			return new ModelAndView("redirect:/admin/system/monitor/failedJms");
		}
		return new ModelAndView("redirect:/admin/system/monitor/failedJms");
	}
	
	@RequestMapping(value="/failedJms/groupResend", method = RequestMethod.POST)
	public ModelAndView groupResendfailedJms(String ids){
		String uri=ConfigParams.getBaseUrl()+"monitor/failedJms/gorupResend/"+ids+"/";
		String result=restTemplate.getForObject(uri, String.class);
		if(ReturnConstants.SUCCESS.equals(result)){
			return new ModelAndView("redirect:/admin/system/monitor/failedJms");
		}
		return new ModelAndView("redirect:/admin/system/monitor/failedJms");
	}
}
