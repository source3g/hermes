package com.source3g.hermes.merchant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "merchant/account")
public class AccountController {

	@RequestMapping(value = "/switch",method=RequestMethod.GET)
	public ModelAndView toSwitch() {
		return new ModelAndView("merchant/accountCenter/switch");
	}
	@RequestMapping(value = "/remindSetting",method=RequestMethod.GET)
	public ModelAndView toRemindSetting() {
		return new ModelAndView("merchant/accountCenter/remindSetting");
	}
	
	

}
