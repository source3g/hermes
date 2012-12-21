package com.source3g.hermes.admin.controller;

import java.util.HashMap;
import java.util.Map;

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

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.Device;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.merchant.MerchantGroup;
import com.source3g.hermes.entity.merchant.MerchantRemindTemplate;
import com.source3g.hermes.entity.merchant.RemindTemplate;
import com.source3g.hermes.utils.ConfigParams;
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
	//验证商户账号是否存在
	@RequestMapping(value = "accountValidate", method = RequestMethod.GET)
	@ResponseBody
	public Boolean accountValidate(String account) {
		String uri=ConfigParams.getBaseUrl() + "merchant/accountValidate/"+account+"/";
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
			return new ModelAndView("admin/merchant/add",model);
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

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "merchant/delete/" + id + "/";
		restTemplate.getForObject(uri, String.class);
		return new ModelAndView("redirect:/admin/merchant/list/");
	}
	@RequestMapping(value = "/toSetDictionary/{merchantId}", method = RequestMethod.GET)
	public ModelAndView setDictionary(@PathVariable String merchantId) {
		String uri = ConfigParams.getBaseUrl() + "merchant/merchantRemindList/"+merchantId+"/";
		MerchantRemindTemplate[] merchantRemindTemplates = restTemplate.getForObject(uri, MerchantRemindTemplate[].class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("merchantRemindTemplates", merchantRemindTemplates);
		model.put("id", merchantId);
		return new ModelAndView("/admin/merchant/set",model);
	}
	@RequestMapping(value = "/dataDictionaryList", method = RequestMethod.GET)
	@ResponseBody
	public RemindTemplate[] dataDictionaryList() {
		String uri = ConfigParams.getBaseUrl() + "merchant/remindSetting/";
		RemindTemplate[] remindTemplate = restTemplate.getForObject(uri, RemindTemplate[].class);
		return remindTemplate;
	}
	
	@RequestMapping(value = "/remindAdd/{merchantId}/{templateId}", method = RequestMethod.GET)
	public ModelAndView remindAdd(@PathVariable String merchantId,@PathVariable String templateId ) {
		String uri = ConfigParams.getBaseUrl() + "merchant/remindAdd/"+merchantId+"/"+templateId+"/";
		MerchantRemindTemplate[] merchantRemindTemplate = restTemplate.getForObject(uri, MerchantRemindTemplate[].class);
		Map<String, Object> model = new HashMap<String, Object>();
		if(merchantRemindTemplate!=null){
			model.put("merchantId", merchantId);
			model.put("merchantRemindTemplates", merchantRemindTemplate);
		return new ModelAndView("/admin/merchant/set",model);
		}
		return  new ModelAndView("admin/error");
	}
	
	@RequestMapping(value = "/remindDelete/{merchantId}/{templateId}", method = RequestMethod.GET)
	public ModelAndView remindDelete(@PathVariable String merchantId,@PathVariable String templateId ) {
		String uri = ConfigParams.getBaseUrl() + "merchant/remindDelete/"+merchantId+"/"+templateId+"/";
		MerchantRemindTemplate[] merchantRemindTemplate = restTemplate.getForObject(uri, MerchantRemindTemplate[].class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("merchantRemindTemplates", merchantRemindTemplate);
		return new ModelAndView("/admin/merchant/set",model);
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
	public ModelAndView reservedMsgLog(@PathVariable String id,String pageNo){
		Map<String, Object> model = new HashMap<String, Object>();
		String uri = ConfigParams.getBaseUrl() + "merchant/" + id + "/";
		Merchant merchant=restTemplate.getForObject(uri, Merchant.class);
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		String uriMsgLog = ConfigParams.getBaseUrl() + "merchant/msgLogList/?pageNo=" + pageNo;
		Page page = restTemplate.getForObject(uriMsgLog, Page.class);
		model.put("page", page);
		model.put("merchant", merchant);
		return new ModelAndView("admin/shortMessage/reservedMessageLog",model);
	}
	@RequestMapping(value="/toUpdateQuota/{id}", method=RequestMethod.GET)
	public ModelAndView reservedMsgLog(@PathVariable String id){
		Map<String, Object> model = new HashMap<String, Object>();
		String uri = ConfigParams.getBaseUrl() + "merchant/" + id + "/";
		Merchant merchant=restTemplate.getForObject(uri, Merchant.class);
		model.put("merchant", merchant);
		return new ModelAndView("admin/shortMessage/updateQuota",model);
	}
	@RequestMapping(value="/UpdateQuota/{id}", method=RequestMethod.POST)
	public ModelAndView UpdateQuota(@PathVariable String id,String type,String count){
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
}
