package com.source3g.hermes.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.Device;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.merchant.MerchantGroup;
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

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView add(@Valid Merchant merchant, BindingResult errorResult) {
		if (errorResult.hasErrors()) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("errors", errorResult.getAllErrors());
			return new ModelAndView("admin/merchant/add", model);
		}
		String uri = ConfigParams.getBaseUrl() + "merchant/add/";
		HttpEntity<Merchant> entity = new HttpEntity<Merchant>(merchant);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("success", "success");
			return new ModelAndView("admin/merchant/add", model);
		} else {
			return new ModelAndView("admin/error");
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
}
