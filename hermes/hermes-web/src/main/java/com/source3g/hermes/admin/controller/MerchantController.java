package com.source3g.hermes.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.Device;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.merchant.MerchantGroup;
import com.source3g.hermes.entity.merchant.MerchantRemindTemplate;
import com.source3g.hermes.entity.merchant.RemindTemplate;
import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.LoginUtils;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping("/admin/merchant")
public class MerchantController {
	private static final Logger logger = LoggerFactory.getLogger(MerchantController.class);

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public ModelAndView toAdd() {
		return new ModelAndView("admin/merchant/add");
	}

	// 验证商户账号是否存在
	@RequestMapping(value = "accountValidate", method = RequestMethod.GET)
	@ResponseBody
	public Boolean accountValidate(String account,String oldAccount) {
		if(StringUtils.isNotEmpty(account)&&account.equals(oldAccount)){
			return true;
		}
		String uri = ConfigParams.getBaseUrl() + "merchant/accountValidate/" + account + "/";
		Boolean result = restTemplate.getForObject(uri, Boolean.class);
		return result;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView add(@Valid Merchant merchant, BindingResult errorResult) {
		Map<String, Object> model = new HashMap<String, Object>();
		if (errorResult.hasErrors()) {
			model.put("errors", errorResult.getAllErrors());
			return new ModelAndView("admin/merchant/add", model);
		}
		String uri = ConfigParams.getBaseUrl() + "merchant/add/";
		HttpEntity<Merchant> entity = new HttpEntity<Merchant>(merchant);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			model.put("success", "success");
			return new ModelAndView("admin/merchant/add", model);
		} else {
			model.put("error", result);
			return new ModelAndView("admin/merchant/add", model);
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(Merchant merchant, String pageNo) {
		logger.debug("list.......");
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		String uri = ConfigParams.getBaseUrl() + "merchant/list/?pageNo=" + pageNo;
		if (StringUtils.isNotEmpty(merchant.getName())) {
			uri += "&name=" + merchant.getName();
		}
		Page page = restTemplate.getForObject(uri, Page.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("page", page);
		return new ModelAndView("admin/merchant/list", model);
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "merchant/cancel/" + id + "/";
		restTemplate.getForObject(uri, String.class);
		return new ModelAndView("redirect:/admin/merchant/list/");
	}

	@RequestMapping(value = "/recover/{id}", method = RequestMethod.GET)
	public ModelAndView recover(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "merchant/recover/" + id + "/";
		restTemplate.getForObject(uri, String.class);
		return new ModelAndView("redirect:/admin/merchant/list/");
	}
	
	@RequestMapping(value = "/toSetDictionary/{merchantId}", method = RequestMethod.GET)
	public ModelAndView setDictionary(@PathVariable String merchantId) {
		String uri = ConfigParams.getBaseUrl() + "merchant/merchantRemindList/" + merchantId + "/";
		MerchantRemindTemplate[] merchantRemindTemplates = restTemplate.getForObject(uri, MerchantRemindTemplate[].class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("merchantRemindTemplates", merchantRemindTemplates);
		model.put("id", merchantId);
		return new ModelAndView("/admin/merchant/set", model);
	}

	@RequestMapping(value = "/dataDictionaryList", method = RequestMethod.GET)
	@ResponseBody
	public RemindTemplate[] dataDictionaryList() {
		String uri = ConfigParams.getBaseUrl() + "merchant/remindSetting/";
		RemindTemplate[] remindTemplate = restTemplate.getForObject(uri, RemindTemplate[].class);
		return remindTemplate;
	}

	@RequestMapping(value = "/remindAdd/{merchantId}/{templateId}", method = RequestMethod.GET)
	public ModelAndView remindAdd(@PathVariable String merchantId, @PathVariable String templateId) {
		String uri = ConfigParams.getBaseUrl() + "merchant/remindAdd/" + merchantId + "/" + templateId + "/";
		MerchantRemindTemplate[] merchantRemindTemplate = restTemplate.getForObject(uri, MerchantRemindTemplate[].class);
		Map<String, Object> model = new HashMap<String, Object>();
		if (merchantRemindTemplate != null) {
			model.put("merchantId", merchantId);
			model.put("merchantRemindTemplates", merchantRemindTemplate);
			return new ModelAndView("/admin/merchant/set", model);
		}
		return new ModelAndView("admin/error");
	}

	@RequestMapping(value = "/remindDelete/{merchantId}/{templateId}", method = RequestMethod.GET)
	public ModelAndView remindDelete(@PathVariable String merchantId, @PathVariable String templateId) {
		String uri = ConfigParams.getBaseUrl() + "merchant/remindDelete/" + merchantId + "/" + templateId + "/";
		MerchantRemindTemplate[] merchantRemindTemplate = restTemplate.getForObject(uri, MerchantRemindTemplate[].class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("merchantRemindTemplates", merchantRemindTemplate);
		return new ModelAndView("/admin/merchant/set", model);
	}

	@RequestMapping(value = "/toModify/{id}", method = RequestMethod.GET)
	public ModelAndView toModify(@PathVariable String id) {
		Map<String, Object> model = new HashMap<String, Object>();
		String uri = ConfigParams.getBaseUrl() + "merchant/" + id + "/";
		Merchant merchant = restTemplate.getForObject(uri, Merchant.class);
		if (merchant.getMerchantGroupId() != null) {
			String uriGroup = ConfigParams.getBaseUrl() + "/merchantGroup/" + merchant.getMerchantGroupId() + "/";
			MerchantGroup merchantGroup = restTemplate.getForObject(uriGroup, MerchantGroup.class);
			model.put("merchantGroup", merchantGroup);
		}
		if (merchant.getDeviceIds() != null && merchant.getDeviceIds().size() > 0) {
			StringBuffer deviceIds = new StringBuffer();
			for (Object deviceId : merchant.getDeviceIds()) {
				deviceIds.append(deviceId.toString());
				deviceIds.append(",");
			}
			deviceIds.delete(deviceIds.length() - 1, deviceIds.length());

			String uriDevice = ConfigParams.getBaseUrl() + "/device/" + deviceIds.toString() + "/";
			Device[] devices = restTemplate.getForObject(uriDevice, Device[].class);
			model.put("devices", devices);
		}
		model.put("merchant", merchant);
		model.put("update", true);
		return new ModelAndView("admin/merchant/add", model);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(@Valid Merchant merchant, BindingResult errorResult) {
		String uri = ConfigParams.getBaseUrl() + "merchant/update/";
		HttpEntity<Merchant> entity = new HttpEntity<Merchant>(merchant);
		String result = restTemplate.postForObject(uri, entity, String.class);

		if (ReturnConstants.SUCCESS.equals(result)) {

			return new ModelAndView("redirect:/admin/merchant/list/");
		} else {
			return new ModelAndView("admin/error");
		}
	}

	@RequestMapping(value = "/messageInfo/list", method = RequestMethod.GET)
	// msgMinutes短信记录
	public ModelAndView msgMinutes(Merchant merchant, String pageNo) {
		logger.debug("list.......");
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		String uri = ConfigParams.getBaseUrl() + "merchant/list/?pageNo=" + pageNo;
		if (StringUtils.isNotEmpty(merchant.getName())) {
			uri += "&name=" + merchant.getName();
		}
		Page page = restTemplate.getForObject(uri, Page.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("page", page);
		return new ModelAndView("admin/shortMessage/shortMessageInfo", model);
	}

	/**
	 * reservedMsg 预存短信
	 * 
	 * @param id
	 *            商户Id
	 * @return 商户短信预存界面
	 */
	@RequestMapping(value = "/toReservedMsg/{id}", method = RequestMethod.GET)
	public ModelAndView toReservedMsg(@PathVariable String id) {
		Map<String, Object> model = new HashMap<String, Object>();
		String uri = ConfigParams.getBaseUrl() + "merchant/" + id + "/";
		Merchant merchant = restTemplate.getForObject(uri, Merchant.class);
		model.put("merchant", merchant);
		return new ModelAndView("admin/shortMessage/reservedMessage", model);
	}

	@RequestMapping(value = "/reservedMsg/{id}", method = RequestMethod.POST)
	public ModelAndView chargeMsg(@PathVariable String id, String type, String count) {
		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
		formData.add("type", type);
		formData.add("count", count);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, httpHeaders);
		String uri = ConfigParams.getBaseUrl() + "merchant/chargeMsg/" + id + "/";
		String result = restTemplate.postForObject(uri, requestEntity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return new ModelAndView("redirect:/admin/merchant/messageInfo/list/");
		}
		return new ModelAndView("admin/error");
	}

	@RequestMapping(value = "/reservedMsgLog/{id}", method = RequestMethod.GET)
	public ModelAndView reservedMsgLog(@PathVariable String id, String pageNo) {
		Map<String, Object> model = new HashMap<String, Object>();
		String uri = ConfigParams.getBaseUrl() + "merchant/" + id + "/";
		Merchant merchant = restTemplate.getForObject(uri, Merchant.class);
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		String uriMsgLog = ConfigParams.getBaseUrl() + "merchant/msgLogList/?pageNo=" + pageNo+"&merchantId="+merchant.getId();
		Page page = restTemplate.getForObject(uriMsgLog, Page.class);
		model.put("page", page);
		model.put("merchant", merchant);
		return new ModelAndView("admin/shortMessage/reservedMessageLog", model);
	}

	@RequestMapping(value = "/toUpdateQuota/{id}", method = RequestMethod.GET)
	public ModelAndView reservedMsgLog(@PathVariable String id) {
		Map<String, Object> model = new HashMap<String, Object>();
		String uri = ConfigParams.getBaseUrl() + "merchant/" + id + "/";
		Merchant merchant = restTemplate.getForObject(uri, Merchant.class);
		model.put("merchant", merchant);
		return new ModelAndView("admin/shortMessage/updateQuota", model);
	}

	@RequestMapping(value = "/UpdateQuota/{id}", method = RequestMethod.POST)
	public ModelAndView UpdateQuota(@PathVariable String id, String type, String count) {
		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
		formData.add("type", type);
		formData.add("count", count);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, httpHeaders);
		String uri = ConfigParams.getBaseUrl() + "merchant/UpdateQuota/" + id + "/";
		String result = restTemplate.postForObject(uri, requestEntity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return new ModelAndView("redirect:/admin/merchant/messageInfo/list/");
		}
		return new ModelAndView("admin/error");
	}
	
	@RequestMapping(value = "/addMerchantResource", method = RequestMethod.GET)
	public ModelAndView addMerchantResource( String name,RedirectAttributes redirectAttributes) throws Exception {
		if(StringUtils.isEmpty(name)){
			return new ModelAndView("admin/merchant/accountCenter/resourceSetting/");
		}
		Merchant merchant=LoginUtils.getLoginMerchant();
		String uri = ConfigParams.getBaseUrl() + "merchant/addMerchantResource/" +merchant.getId()+"/"+name+"/";
		String result = restTemplate.getForObject(uri, String.class); 
		if(ReturnConstants.SUCCESS.equals(result)){
			return new ModelAndView("redirect:/merchant/account/toResourceSetting/");
		}else{
			redirectAttributes.addFlashAttribute("error",result);
			return new ModelAndView("redirect:/merchant/account/toResourceSetting/");
		}
	}
	
	@RequestMapping(value = "/merchantResourceList", method = RequestMethod.GET)
	@ResponseBody
	public Merchant MerchantResourceList(HttpServletRequest req) throws Exception {
		Merchant merchant=LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "merchant/"+merchant.getId()+"/";
		Merchant result = restTemplate.getForObject(uri, Merchant.class); 
		return result;
	}
	
	@RequestMapping(value = "/deletemerchantResource/{name}", method = RequestMethod.GET)
	public ModelAndView deletemerchantResource(@PathVariable String name) throws Exception {
		Merchant merchant=LoginUtils.getLoginMerchant();
		String uri = ConfigParams.getBaseUrl() + "merchant/deletemerchantResource/"+merchant.getId()+"/"+name+"/";
		String  result = restTemplate.getForObject(uri, String.class); 
		if(ReturnConstants.SUCCESS.equals(result)){
			return new ModelAndView("redirect:/merchant/account/toResourceSetting/");
		}
		return new ModelAndView("admin/merchant/accountCenter/resourceSetting/");
	}
	
	@RequestMapping(value = "/updateMerchantResource", method = RequestMethod.GET)
	public ModelAndView updateMerchantResource(String suffix ,String prefix ) throws Exception {
		Merchant merchant=LoginUtils.getLoginMerchant();
		String uri = ConfigParams.getBaseUrl() + "merchant/updateMerchantResource/"+merchant.getId()+"/?1=1";
		if(StringUtils.isNotEmpty(suffix)){
			uri+="&suffix="+suffix;
		}
		if(StringUtils.isNotEmpty(prefix)){
			uri+="&prefix="+prefix;
		}
		Merchant  result = restTemplate.getForObject(uri, Merchant.class); 
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("Merchant", result);
		return new ModelAndView("admin/merchant/accountCenter/resourceSetting/",model);
	}
}
