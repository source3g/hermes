package com.source3g.hermes.merchant.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.dto.customer.CustomerRemindDto;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.merchant.MerchantRemindTemplate;
import com.source3g.hermes.entity.merchant.MerchantResource;
import com.source3g.hermes.entity.merchant.Setting;
import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.LoginUtils;

@Controller
@RequestMapping(value = "merchant/account")
public class AccountController {

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/toSwitch", method = RequestMethod.GET)
	public ModelAndView toSwitch(HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("merchant", merchant);
		return new ModelAndView("merchant/accountCenter/switch", model);
	}

	@RequestMapping(value = "/switch", method = RequestMethod.POST)
	public ModelAndView Switch(Setting setting, HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "merchant/switch/" + merchant.getId() + "/";
		HttpEntity<Setting> entity = new HttpEntity<Setting>(setting);
		String result = restTemplate.postForObject(uri, entity, String.class);
		Map<String, Object> model = new HashMap<String, Object>();
		merchant.setSetting(setting);
		model.put("merchant", merchant);
		if (ReturnConstants.SUCCESS.equals(result)) {
			model.put(ReturnConstants.SUCCESS, ReturnConstants.SUCCESS);
			return new ModelAndView("merchant/accountCenter/switch", model);
		}
		return new ModelAndView("admin/error");

	}

	@RequestMapping(value = "/remindTemplate/get", method = RequestMethod.GET)
	@ResponseBody
	public MerchantRemindTemplate[] getMerchantRemindTemplates() throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant();
		String uri = ConfigParams.getBaseUrl() + "merchant/merchantRemindList/" + merchant.getId() + "/";
		MerchantRemindTemplate[] merchantRemindTemplates = restTemplate.getForObject(uri, MerchantRemindTemplate[].class);
		return merchantRemindTemplates;
	}

	@RequestMapping(value = "/remindSetting", method = RequestMethod.GET)
	public ModelAndView toRemindSetting(HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "merchant/merchantRemindList/" + merchant.getId() + "/";
		MerchantRemindTemplate[] merchantRemindTemplates = restTemplate.getForObject(uri, MerchantRemindTemplate[].class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("merchantRemindTemplates", merchantRemindTemplates);
		return new ModelAndView("merchant/accountCenter/remindSetting", model);
	}

	@RequestMapping(value = "/remindSetting/json", method = RequestMethod.GET)
	@ResponseBody
	public MerchantRemindTemplate[] getRemindSettingJson(HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "merchant/merchantRemindList/" + merchant.getId() + "/";
		MerchantRemindTemplate[] merchantRemindTemplates = restTemplate.getForObject(uri, MerchantRemindTemplate[].class);

		return merchantRemindTemplates;
	}

	@RequestMapping(value = "/remindSave", method = RequestMethod.POST)
	public ModelAndView remindSave(MerchantRemindTemplate merchantRemindTemplate, HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "merchant/remindSave/" + merchant.getId() + "/";
		HttpEntity<MerchantRemindTemplate> entity = new HttpEntity<MerchantRemindTemplate>(merchantRemindTemplate);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return new ModelAndView("redirect:/merchant/account/remindSetting");
		}
		return new ModelAndView("admin/error");
	}

	@RequestMapping(value = "/toPasswordChange", method = RequestMethod.GET)
	public ModelAndView toPasswordChange() throws Exception {
		return new ModelAndView("merchant/accountCenter/passwordChange");

	}

	@RequestMapping(value = "/passwordChange/{password}/{newPassword}", method = RequestMethod.GET)
	public ModelAndView passwordChange(@PathVariable String password, @PathVariable String newPassword, HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "merchant/passwordChange/" + password + "/" + newPassword + "/" + merchant.getId() + "/";
		String result = restTemplate.getForObject(uri, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return new ModelAndView("merchant/accountCenter/passwordChange");
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("error", result);
		return new ModelAndView("merchant/accountCenter/passwordChange", model);
	}

	@RequestMapping(value = "remind/toList", method = RequestMethod.GET)
	public ModelAndView toRemindList() throws Exception {
		return new ModelAndView("merchant/accountCenter/remindList");
	}

	@RequestMapping(value = "remind/list", method = RequestMethod.GET)
	@ResponseBody
	public CustomerRemindDto[] remindList() throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant();
		String uri = ConfigParams.getBaseUrl() + "customer/todayReminds/" + merchant.getId() + "/";
		CustomerRemindDto[] result = restTemplate.getForObject(uri, CustomerRemindDto[].class);
		return result;
	}

	@RequestMapping(value = "sendMessages/{title}", method = RequestMethod.GET)
	public ModelAndView sendMessages(@PathVariable String title, HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "shortMessage/remindSend/" + title + "/" + merchant.getId() + "/";
		String result = restTemplate.getForObject(uri, String.class);
		Map<String, Object> model = new HashMap<String, Object>();
		if (ReturnConstants.SUCCESS.equals(result)) {
			model.put("success", result);
			return new ModelAndView("merchant/accountCenter/remindList", model);
		} else {
			model.put("error", result);
			return new ModelAndView("merchant/accountCenter/remindList", model);
		}
	}

	@RequestMapping(value = "ignoreSendMessages/{title}", method = RequestMethod.GET)
	public ModelAndView ignoreSendMessages(@PathVariable String title, HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "shortMessage/ignoreSendMessages/" + title + "/" + merchant.getId() + "/";
		@SuppressWarnings("unused")
		String result = restTemplate.getForObject(uri, String.class);
		return new ModelAndView("merchant/accountCenter/remindList");
	}

	@RequestMapping(value = "toResourceSetting", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toResourceSetting() {
		return new ModelAndView("/merchant/accountCenter/resourceSetting");
	}

	@RequestMapping(value = "/addMerchantResource", method = RequestMethod.GET)
	public ModelAndView addMerchantResource(String name, RedirectAttributes redirectAttributes) throws Exception {
		if (StringUtils.isEmpty(name)) {
			return new ModelAndView("/merchant/accountCenter/resourceSetting/");
		}
		Merchant merchant = LoginUtils.getLoginMerchant();
		String uri = ConfigParams.getBaseUrl() + "merchant/addMerchantResource/" + merchant.getId() + "/" + name + "/";
		String result = restTemplate.getForObject(uri, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return new ModelAndView("redirect:/merchant/account/toResourceSetting/");
		} else {
			redirectAttributes.addFlashAttribute("error", result);
			return new ModelAndView("redirect:/merchant/account/toResourceSetting/");
		}
	}

	@RequestMapping(value = "/merchantResource", method = RequestMethod.GET)
	@ResponseBody
	public MerchantResource getMerchantResourceList(HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "merchant/merchantResource/" + merchant.getId() + "/";
		MerchantResource result = restTemplate.getForObject(uri, MerchantResource.class);
		return result;
	}

	@RequestMapping(value = "/deletemerchantResource/{name}", method = RequestMethod.GET)
	public ModelAndView deletemerchantResource(@PathVariable String name) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant();
		String uri = ConfigParams.getBaseUrl() + "merchant/deletemerchantResource/" + merchant.getId() + "/" + name + "/";
		String result = restTemplate.getForObject(uri, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return new ModelAndView("redirect:/merchant/account/toResourceSetting/");
		}
		return new ModelAndView("/merchant/accountCenter/resourceSetting/");
	}

	@RequestMapping(value = "/updateMerchantResource", method = RequestMethod.GET)
	public ModelAndView updateMerchantResource(String messageContent) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant();
		String uri = ConfigParams.getBaseUrl() + "merchant/updateMerchantResource/" + merchant.getId() + "/?messageContent="+messageContent;
		Merchant result = restTemplate.getForObject(uri, Merchant.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("Merchant", result);
		return new ModelAndView("/merchant/accountCenter/resourceSetting/", model);
	}

}
