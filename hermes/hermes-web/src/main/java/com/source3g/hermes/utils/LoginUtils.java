package com.source3g.hermes.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.source3g.hermes.admin.security.ShiroDbRealm.ShiroUser;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.exception.NotLoginException;

public class LoginUtils {

	public static Merchant getLoginMerchant(HttpServletRequest req) throws Exception {
		return getLoginMerchant();
	}
	
	public static Merchant getLoginMerchant() throws Exception {
		Subject currentUser = SecurityUtils.getSubject();
		ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipal();
		Merchant merchant = (Merchant) shiroUser.getMerchant();
		if (merchant == null) {
			throw new NotLoginException("请重新登录");
		} else {
			return merchant;
		}
	}
	
	

}
