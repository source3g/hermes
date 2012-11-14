package com.source3g.hermes.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.WebUtils;

import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.utils.ConfigParams;

@Controller
public class FrontController {
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/admin/index", method = RequestMethod.GET)
	public String toAdmin() {
		return "admin/index";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String toMerchant(HttpServletRequest request) {
		initSession(request);
		return "merchant/index";
	}

	private void initSession(HttpServletRequest request) {
		String id = "50a1b46286c3d8c834f49723";
		String uri = ConfigParams.getBaseUrl() + "/merchant/" + id + "/";
		Merchant merchant = restTemplate.getForObject(uri, Merchant.class);
		if (merchant == null) {
			merchant = new Merchant();
			merchant.setId(new ObjectId("50a1b46286c3d8c834f49723"));
			merchant.setName("安联");
			merchant.setAddr("呼家楼");
			String uriAdd = ConfigParams.getBaseUrl() + "/merchant/add/";
			HttpEntity<Merchant> httpEntity = new HttpEntity<>(merchant);
			restTemplate.postForObject(uriAdd, httpEntity, String.class);
		}
		WebUtils.setSessionAttribute(request, "merchant", merchant);
	}

}
