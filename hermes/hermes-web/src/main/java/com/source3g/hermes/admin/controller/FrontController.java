package com.source3g.hermes.admin.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.admin.security.ShiroDbRealm.ShiroUser;
import com.source3g.hermes.enums.TypeEnum.LoginType;

@Controller
public class FrontController {

	@RequestMapping(value = "/admin/login", method = RequestMethod.POST)
	public ModelAndView login(String username, String password, boolean rememberMe) {
		Subject currentUser = SecurityUtils.getSubject();
		boolean result = false;
		if (!currentUser.isAuthenticated()) {
			result = login(currentUser, username, password, rememberMe);
		} else {// 重复登录
			ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipal();
			if (shiroUser.getMerchant() != null && shiroUser.getMerchant().getAccount().equalsIgnoreCase(username))// 如果登录名不同
				currentUser.logout();
			result = login(currentUser, username, password, rememberMe);
		}
		if (result) {
			return new ModelAndView("redirect:/admin/index/");
		} else {
			return new ModelAndView("redirect:/admin/login/");
		}
	}

	@RequestMapping(value = "/admin/login")
	public ModelAndView toLogin() {
		return new ModelAndView("/admin/login");
	}

	@RequestMapping(value = "/admin/logout", method = RequestMethod.GET)
	public ModelAndView logout() {
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();
		return new ModelAndView("redirect:/login");
	}

	private boolean login(Subject currentUser, String username, String password, boolean rememberMe) {
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		token.setRememberMe(rememberMe);
		try {
			currentUser.getSession().setAttribute("loginType", LoginType.admin);
			currentUser.login(token);
		} catch (Exception e) {
			return false;
		}
		return true;
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
