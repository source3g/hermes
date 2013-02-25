package com.source3g.hermes.merchant.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.LoginUtils;

@Controller
@RequestMapping(value = "/merchant")
public class MainController {
	
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public ModelAndView toMain() {
		return new ModelAndView("merchant/main");
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/statistics",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> statistics() throws Exception{
		Merchant merchant=LoginUtils.getLoginMerchant();
		String merchantUrl=ConfigParams.getBaseUrl()+"/customer/statistics/"+merchant.getId()+"/";
		Map<String,Object> customerStatisticsDto[]=restTemplate.getForObject(merchantUrl, Map[].class);
		String messageUrl=ConfigParams.getBaseUrl()+"/shortMessage/statistics/"+merchant.getId()+"/";
		Map<String,Object> messageStatisticsDto[]=restTemplate.getForObject(messageUrl, Map[].class);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("customerStatistics", customerStatisticsDto);
		map.put("messageStatistics", messageStatisticsDto);
		return map;
	}
	
}
