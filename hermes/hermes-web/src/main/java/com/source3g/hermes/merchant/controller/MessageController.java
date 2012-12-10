package com.source3g.hermes.merchant.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.customer.CustomerGroup;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.message.MessageAutoSend;
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
			return new ModelAndView("/merchant/shortMessage/messageTemplate");
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
		return new ModelAndView("merchant/shortMessage/reservedMessageLog", model);
	}

	@RequestMapping(value = "/toMessageSend", method = RequestMethod.GET)
	public ModelAndView toMessageSend(HttpServletRequest req) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uriCustomerGroup = ConfigParams.getBaseUrl() + "customerGroup/listAll/" + merchant.getId() + "/";
		CustomerGroup[] customerGroups = restTemplate.getForObject(uriCustomerGroup, CustomerGroup[].class);
		String uri = ConfigParams.getBaseUrl() + "merchant/" + merchant.getId() + "/";
		Merchant merchant1 = restTemplate.getForObject(uri, Merchant.class);
		model.put("merchant", merchant1);
		model.put("customerGroups", customerGroups);
		return new ModelAndView("merchant/shortMessage/messageSend", model);
	}

	@RequestMapping(value = "/messageSend", method = RequestMethod.POST)
	public ModelAndView messageSend(HttpServletRequest req, String[] ids, String content) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		Map<String, Object> model = new HashMap<String, Object>();
		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
		for (String id : ids) {
			formData.add("ids", id);
		}
		formData.add("content", content);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		// httpHeaders.set("charset", "UTF-8");

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, httpHeaders);
		String uri = ConfigParams.getBaseUrl() + "shortMessage/messageSend/" + merchant.getId() + "/";
		String result = restTemplate.postForObject(uri, requestEntity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			model.put("success", "success");
			return new ModelAndView("redirect:/merchant/message/toMessageSend/", model);
		}
		return new ModelAndView("admin/error");
	}

	@RequestMapping(value = "/toFastSend", method = RequestMethod.GET)
	public ModelAndView fastSend(HttpServletRequest req) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		model.put("merchant", merchant);
		return new ModelAndView("merchant/shortMessage/fastSend", model);
	}

	@RequestMapping(value = "/fastSend", method = RequestMethod.POST)
	public ModelAndView fastSend(HttpServletRequest req, String type, String customerPhones, String content) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
		formData.add("type", type);
		formData.add("customerPhones", customerPhones);
		formData.add("content", content);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, httpHeaders);
		String uri = ConfigParams.getBaseUrl() + "shortMessage/fastSend/" + merchant.getId() + "/";
		String result = restTemplate.postForObject(uri, requestEntity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return new ModelAndView("redirect:/merchant/message/toFastSend/");
		}
		return new ModelAndView("admin/error");
	}

	@RequestMapping(value = "/toMessageList", method = RequestMethod.GET)
	public ModelAndView toMessageList(HttpServletRequest req, String pageNo, String startTime, String endTime, String phone, String customerGroupName) throws Exception {
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		Map<String, Object> model = new HashMap<String, Object>();
		Merchant merchant = LoginUtils.getLoginMerchant(req);

		String uri = ConfigParams.getBaseUrl() + "shortMessage/messageSendLog/" + merchant.getId() + "/list/?pageNo=" + pageNo;
		if (StringUtils.isNotEmpty(startTime)) {
			uri += "&startTime=" + startTime;
		}
		if (StringUtils.isNotEmpty(endTime)) {
			uri += "&endTime=" + endTime;
		}
		if (StringUtils.isNotEmpty(phone)) {
			uri += "&phone=" + phone;
		}
		if (StringUtils.isNotEmpty(customerGroupName)) {
			uri += "&customerGroupName=" + customerGroupName;
		}

		Page page = restTemplate.getForObject(uri, Page.class);
		model.put("page", page);
		return new ModelAndView("merchant/shortMessage/MessageList", model);
	}

	@RequestMapping(value = "/toAutoSend", method = RequestMethod.GET)
	public ModelAndView toAutoSend(HttpServletRequest req) throws Exception {
		Merchant merchant=LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "/shortMessage/autoSend/messageInfo/"+merchant.getId()+"/";
		MessageAutoSend  messageAutoSend = restTemplate.getForObject(uri, MessageAutoSend.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("messageAutoSend", messageAutoSend);
		return new ModelAndView("merchant/shortMessage/autoSend",model);
	}

	@RequestMapping(value = "/autoSend", method = RequestMethod.POST)
	public ModelAndView autoSend(HttpServletRequest req,MessageAutoSend messageAutoSend) throws Exception {
		Merchant merchant=LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "/shortMessage/autoSend/messageInfo/";
		messageAutoSend.setMerchantId(merchant.getId());
		HttpEntity<MessageAutoSend> entity = new HttpEntity<MessageAutoSend>(messageAutoSend);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("success", "success");
			return new ModelAndView("merchant/shortMessage/autoSend");
		} else {
			return new ModelAndView("admin/error");
		}

	}
}
