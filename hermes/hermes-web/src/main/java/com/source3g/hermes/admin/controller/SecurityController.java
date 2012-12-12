package com.source3g.hermes.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.security.entity.Account;
import com.source3g.hermes.security.service.SecurityService;

@Controller
@RequestMapping(value = "/admin/security")
public class SecurityController {
	
	@Autowired
	private SecurityService securityService;

	@RequestMapping(value="/account/add",method=RequestMethod.GET)
	public ModelAndView toAddAccount() {
		return new ModelAndView("/admin/security/accountAdd");
	}
	
	@RequestMapping(value="/account/add",method=RequestMethod.POST)
	public ModelAndView addAccount(Account account) {
		securityService.addAccount(account);
		return new ModelAndView("/admin/security/accountAdd");
	}

}
