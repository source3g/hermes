package com.source3g.hermes.admin.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FrontController {

	@RequestMapping(value = "/admin/login", method = RequestMethod.POST)
	public ModelAndView adminLogin(String username, String password, boolean rememberMe) {
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		Subject currentUser = SecurityUtils.getSubject();
		// if(!currentUser.isAuthenticated()){
		// System.out.println("未授权");
		// }else {
		// //重复登录

		// }
		currentUser.login(token);
		return new ModelAndView("redirect:/admin/index/");
	}

	@RequestMapping(value = "/admin/index", method = RequestMethod.GET)
	public String toAdmin() {
		Subject currentUser = SecurityUtils.getSubject();
		if (!currentUser.isAuthenticated()) {
			System.out.println("未授权");
		}
		return "admin/index";
	}


}
