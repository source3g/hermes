package com.source3g.hermes.merchant.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
	public ModelAndView template() {

		return new ModelAndView("/merchant/shortMessage/messageTemplate");
	}

	@RequestMapping(value = "/template/add", method = RequestMethod.POST)
	public ModelAndView addTemplate(HttpServletRequest req, MessageTemplate messageTemplate) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		messageTemplate.setMerchantId(merchant.getId());
		//String uri = ConfigParams.getBaseUrl() + "shortMessage/template/add";
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
	public ModelAndView messageSend(String name ,String messageInfo )throws Exception {
		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
		formData.add("merchantGroupName", name);
		formData.add("messageInfo",messageInfo);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, httpHeaders);
	  	String uri=ConfigParams.getBaseUrl() +"shortMessage/messageSend/";
	  	String result = restTemplate.postForObject(uri,requestEntity,String.class);
	   if(ReturnConstants.SUCCESS.equals(result)){
		   return new ModelAndView("merchant/shortMessage/messageSend"); 
	   }
		return new ModelAndView("admin/error"); 
	   
	}
}
