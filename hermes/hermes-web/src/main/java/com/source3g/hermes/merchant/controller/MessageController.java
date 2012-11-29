package com.source3g.hermes.merchant.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.message.MessageTemplate;
import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.LoginUtils;

import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.LoginUtils;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping(value = "/merchant/message")
public class MessageController {
	
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value = "/template", method = RequestMethod.GET)
	public ModelAndView template() {
		
		return new ModelAndView("/merchant/shortMessage/messageTemplate");
	}
	
	
	@RequestMapping(value = "/template/add", method = RequestMethod.POST)
	public ModelAndView addTemplate(HttpServletRequest req,MessageTemplate messageTemplate) throws Exception {
		Merchant merchant=LoginUtils.getLoginMerchant(req);
		messageTemplate.setMerchantId(merchant.getId());
		String uri=ConfigParams.getBaseUrl()+"shortMessage/template/add";
		
		return new ModelAndView("/merchant/shortMessage/messageTemplate");
	}
	
	@RequestMapping(value = "/reservedMsgLog", method = RequestMethod.GET)
	public ModelAndView reservedMsgLog(HttpServletRequest req,String pageNo) throws Exception {
			Merchant merchant = LoginUtils.getLoginMerchant(req);	
			Map<String, Object> model = new HashMap<String, Object>();	
			if (StringUtils.isEmpty(pageNo)) {
				pageNo = "1";
			}
			String uriMsgLog = ConfigParams.getBaseUrl() + "merchant/msgLogList/?pageNo=" + pageNo;
			Page page = restTemplate.getForObject(uriMsgLog, Page.class);
			model.put("page", page);
			model.put("merchant", merchant);
			return new ModelAndView("admin/shortMessage/reservedMessageLog",model);
	}

}
