package com.source3g.wcb.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FrontController {
	@RequestMapping(value="/admin/index",method=RequestMethod.GET)
	public String toAdmin(){
		return "admin/index";
	}
	
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String toMerchant(){
		return "merchant/index";
	}
	
	
}
