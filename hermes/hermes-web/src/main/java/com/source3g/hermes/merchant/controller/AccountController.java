package com.source3g.hermes.merchant.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.merchant.RemindTemplate;
import com.source3g.hermes.entity.merchant.Setting;
import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.LoginUtils;

@Controller
@RequestMapping(value = "merchant/account")
public class AccountController {
	

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/toSwitch",method=RequestMethod.GET)
	public ModelAndView toSwitch(HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("merchant", merchant);
		return new ModelAndView("merchant/accountCenter/switch",model);
	}
	@RequestMapping(value = "/switch",method=RequestMethod.POST)
	public ModelAndView Switch( Setting setting,HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		merchant.setSetting(setting);
		String uri = ConfigParams.getBaseUrl() + "merchant/switch/";
		HttpEntity<Merchant> entity = new HttpEntity<Merchant>(merchant);
		String result = restTemplate.postForObject(uri, entity, String.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("merchant", merchant);
		if (ReturnConstants.SUCCESS.equals(result)) {	
			return new ModelAndView("merchant/accountCenter/switch",model);
	}
			return new ModelAndView("admin/error");
		
	}

	@RequestMapping(value = "/remindSetting",method=RequestMethod.GET)
	public ModelAndView toRemindSetting() {
		String uri=ConfigParams.getBaseUrl() + "merchant/remindSetting/";
		RemindTemplate[] remindTemplate = restTemplate.getForObject(uri, RemindTemplate[].class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("remindTemplate", remindTemplate);
		return new ModelAndView("merchant/accountCenter/remindSetting",model);
	}
	@RequestMapping(value = "/remindSetting/json",method=RequestMethod.GET)
	@ResponseBody
	public RemindTemplate[] toRemindSettingJson() {
		String uri=ConfigParams.getBaseUrl() + "merchant/remindSetting/";
		RemindTemplate[] remindTemplate = restTemplate.getForObject(uri, RemindTemplate[].class);
		return remindTemplate;
	}
	@RequestMapping(value = "/remindAdd",method=RequestMethod.POST)
	public ModelAndView remindAdd(RemindTemplate remindTemplate,HttpServletRequest req) throws Exception {
		String uri=ConfigParams.getBaseUrl() + "merchant/remindAdd/";
		HttpEntity<RemindTemplate> entity = new HttpEntity<RemindTemplate>(remindTemplate);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {	
		return new ModelAndView("redirect:/merchant/account/remindSetting");
		}
		return new ModelAndView("admin/error");
		
	}
	@RequestMapping(value = "/remindSave",method=RequestMethod.POST)
	public ModelAndView remindSave(RemindTemplate remindTemplate) throws Exception {
		String uri=ConfigParams.getBaseUrl() + "merchant/remindSave/";
		HttpEntity<RemindTemplate> entity = new HttpEntity<RemindTemplate>(remindTemplate);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {	
		return new ModelAndView("redirect:/merchant/account/remindSetting");
		}
		return new ModelAndView("admin/error");
		
	}
	@RequestMapping(value = "/remindDelete/{id}",method=RequestMethod.GET)
	public ModelAndView remindDelete(@PathVariable ObjectId id) throws Exception {
		String uri=ConfigParams.getBaseUrl() + "merchant/remindDelete/"+id+"";
		String result = restTemplate.getForObject(uri, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {	
		return new ModelAndView("redirect:/merchant/account/remindSetting");
		}
		return new ModelAndView("admin/error");
		
	}
}
	

