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

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.customer.CustomerGroup;
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
		if (errorResult.hasErrors()) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("errors", errorResult.getAllErrors());
			return new ModelAndView("/merchant/shortMessage/messageTemplate", model);
		}
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		messageTemplate.setMerchantId(merchant.getId());
		String uri = ConfigParams.getBaseUrl() + "shortMessage/template/add/";
		HttpEntity<MessageTemplate> entity = new HttpEntity<MessageTemplate>(messageTemplate);
		restTemplate.postForObject(uri, entity, String.class);
		return new ModelAndView("/merchant/shortMessage/messageTemplate");
	}

	@RequestMapping(value = "/template/list", method = RequestMethod.GET)
	public ModelAndView listTemplate(HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "shortMessage/template/list/" + merchant.getId() + "/";
		MessageTemplate[] templates = restTemplate.getForObject(uri, MessageTemplate[].class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("templates", templates);
		return new ModelAndView("/merchant/shortMessage/messageTemplate", model);
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
			return new ModelAndView("merchant/shortMessage/reservedMessageLog",model);
	}

	@RequestMapping(value = "/toMessageSend", method = RequestMethod.GET)
	public ModelAndView toMessageSend(HttpServletRequest req)throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();	
			Merchant merchant=LoginUtils.getLoginMerchant(req);
			String uriCustomerGroup = ConfigParams.getBaseUrl() + "customerGroup/listAll/" + merchant.getId() + "/";
			CustomerGroup[] customerGroups = restTemplate.getForObject(uriCustomerGroup, CustomerGroup[].class);
			String uri = ConfigParams.getBaseUrl() + "merchant/" + merchant.getId() + "/";
			Merchant merchant1=restTemplate.getForObject(uri, Merchant.class);
			model.put("merchant",merchant1);
			model.put("customerGroups",customerGroups);
		return new ModelAndView("merchant/shortMessage/messageSend",model); 
	}
	@RequestMapping(value = "/messageSend", method = RequestMethod.POST)
	public ModelAndView messageSend(String[] ids ,String messageInfo )throws Exception {
		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
		for (String id:ids){
			formData.add("ids", id);
		}
		formData.add("messageInfo",messageInfo);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		 httpHeaders.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		//httpHeaders.set("charset", "UTF-8");
	
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, httpHeaders);
	  	String uri=ConfigParams.getBaseUrl() +"shortMessage/messageSend/";
	  	String result = restTemplate.postForObject(uri,requestEntity,String.class);
	   if(ReturnConstants.SUCCESS.equals(result)){
		   return new ModelAndView("merchant/shortMessage/messageSend"); 
	   }
		return new ModelAndView("admin/error"); 
	   
	}
	@RequestMapping(value = "/fastSend", method = RequestMethod.GET)
	public ModelAndView fastSend()throws Exception {
	 return new ModelAndView("merchant/shortMessage/fastSend");
	}
}
