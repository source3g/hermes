package com.source3g.hermes.merchant.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.message.MessageTemplate;
import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.LoginUtils;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping(value = "/merchant/message")
public class MessageController {

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/template", method = RequestMethod.GET)
	public ModelAndView template(HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "shortMessage/template/list/" + merchant.getId() + "/";
		MessageTemplate[] templates = restTemplate.getForObject(uri, MessageTemplate[].class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("templates", templates);
		return new ModelAndView("/merchant/shortMessage/messageTemplate", model);
	}

	@RequestMapping(value = "/template/add", method = RequestMethod.POST)
	public ModelAndView addTemplate(HttpServletRequest req, @Valid MessageTemplate messageTemplate, BindingResult errorResult) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		if (errorResult.hasErrors()) {
			model.put("errors", errorResult.getAllErrors());
			return new ModelAndView("/merchant/shortMessage/messageTemplate", model);
		}
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		messageTemplate.setMerchantId(merchant.getId());
		String uri = ConfigParams.getBaseUrl() + "shortMessage/template/add/";
		messageTemplate.setId("");
		HttpEntity<MessageTemplate> entity = new HttpEntity<MessageTemplate>(messageTemplate);
		restTemplate.postForObject(uri, entity, String.class);
		model.put(ReturnConstants.SUCCESS, ReturnConstants.SUCCESS);
		return new ModelAndView("/merchant/shortMessage/messageTemplate", model);
	}

	@RequestMapping(value = "/template/save", method = RequestMethod.POST)
	public ModelAndView saveTemplate(HttpServletRequest req, @Valid MessageTemplate messageTemplate, BindingResult errorResult) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		if (errorResult.hasErrors()) {
			model.put("errors", errorResult.getAllErrors());
			return new ModelAndView("/merchant/shortMessage/messageTemplate", model);
		}
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		messageTemplate.setMerchantId(merchant.getId());
		String uri = ConfigParams.getBaseUrl() + "shortMessage/template/save/";
		HttpEntity<MessageTemplate> entity = new HttpEntity<MessageTemplate>(messageTemplate);
		restTemplate.postForObject(uri, entity, String.class);
		model.put(ReturnConstants.SUCCESS, ReturnConstants.SUCCESS);
		return new ModelAndView("/merchant/shortMessage/messageTemplate", model);
	}

	@RequestMapping(value = "/template/listJson", method = RequestMethod.GET)
	@ResponseBody
	public MessageTemplate[] listTemplate(HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "shortMessage/template/list/" + merchant.getId() + "/";
		MessageTemplate[] templates = restTemplate.getForObject(uri, MessageTemplate[].class);
		return templates;
	}

	@RequestMapping(value = "/template/delete/{id}", method = RequestMethod.GET)
	public ModelAndView deleteTemplate(@PathVariable String id) throws Exception {
		String uri = ConfigParams.getBaseUrl() + "shortMessage/template/delete/" + id + "/";
		String result = restTemplate.getForObject(uri, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return new ModelAndView("redirect:/merchant/message/template/");
		}
		return new ModelAndView("error");
	}

	@RequestMapping(value = "/reservedMsgLog", method = RequestMethod.GET)
	public ModelAndView reservedMsgLog(HttpServletRequest req, String pageNo) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		Map<String, Object> model = new HashMap<String, Object>();
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		String uriMsgLog = ConfigParams.getBaseUrl() + "merchant/msgLogList/?pageNo=" + pageNo;
		Page page = restTemplate.getForObject(uriMsgLog, Page.class);
		model.put("page", page);
		model.put("merchant", merchant);
		return new ModelAndView("admin/shortMessage/reservedMessageLog", model);
	}
}
