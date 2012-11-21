package com.source3g.hermes.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.WebUtils;

import com.source3g.hermes.entity.merchant.Merchant;

public class LoginUtils {

	public static Merchant getLoginMerchant(HttpServletRequest req) throws Exception {
		Merchant merchant = (Merchant) WebUtils.getSessionAttribute(req, "loginUser");
		if (merchant == null) {
			throw new Exception("请重新登录");
		} else {
			return merchant;
		}
	}

}
