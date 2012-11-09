package com.source3g.hermes.admin.controller;

import java.util.HashMap;
import java.util.Map;

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
import com.source3g.hermes.entity.merchant.MerchantGroup;
import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping(value = "/admin/merchantGroup")
public class MerchantGroupController {
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public ModelAndView toAdd() {
		return new ModelAndView("admin/merchantGroup/add");
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	public ModelAndView add(@Valid MerchantGroup merchantGroup, BindingResult errorResult) {
		if (errorResult.hasErrors()) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("errors", errorResult.getAllErrors());
			return new ModelAndView("admin/merchantGroup/add", model);
		}
		
		String uri = ConfigParams.getBaseUrl() + "merchantGroup/add";
		HttpEntity<MerchantGroup> entity = new HttpEntity<MerchantGroup>(merchantGroup);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS_WIDTH_QUOT.equals(result)) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put(ReturnConstants.SUCCESS, ReturnConstants.SUCCESS);
			return new ModelAndView("admin/merchantGroup/add", model);
		} else {
			return new ModelAndView("admin/error");
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(MerchantGroup merchantGroup,String pageNo) {
		if(StringUtils.isEmpty(pageNo)){
			pageNo="1";
		}
		String uri = ConfigParams.getBaseUrl() + "merchantGroup/list/?pageNo="+pageNo;
		if(StringUtils.isNotEmpty(merchantGroup.getName())){
			uri+="&name="+merchantGroup.getName();
		}
		Page page = restTemplate.getForObject(uri, Page.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("page", page);
		return new ModelAndView("admin/merchantGroup/list", model);
	}
	
	
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	@ResponseBody
	public MerchantGroup[] listAll(MerchantGroup merchantGroup) {
		String uri = ConfigParams.getBaseUrl() + "merchantGroup/listAll";
		if(StringUtils.isNotEmpty(merchantGroup.getName())){
			uri+="?name="+merchantGroup.getName();
		}
		MerchantGroup[] groups = restTemplate.getForObject(uri,MerchantGroup[].class);
		return groups;
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "merchantGroup/delete/" + id + "/";
		String result = restTemplate.getForObject(uri, String.class);
		if (ReturnConstants.SUCCESS_WIDTH_QUOT.equals(result)) {
			return new ModelAndView("redirect:/admin/merchantGroup/list/");
		} else {
			return new ModelAndView("admin/error");
		}

	}

	@RequestMapping(value = "/toUpdate/{id}", method = RequestMethod.GET)
	public ModelAndView toModify(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "merchantGroup/" + id + "/";
		MerchantGroup merchantGroup = restTemplate.getForObject(uri, MerchantGroup.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("merchantGroup", merchantGroup);
		model.put("update", true);
		return new ModelAndView("admin/merchantGroup/add", model);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView modify(@Valid MerchantGroup merchantGroup, BindingResult errorResult) {
		if (errorResult.hasErrors()) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("errors", errorResult.getAllErrors());
			model.put("update", true);
			return new ModelAndView("admin/merchantGroup/add", model);
		}
		String uri = ConfigParams.getBaseUrl() + "merchantGroup/update";
		HttpEntity<MerchantGroup> entity = new HttpEntity<MerchantGroup>(merchantGroup);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS_WIDTH_QUOT.equals(result)) {
			return new ModelAndView("redirect:/admin/merchantGroup/list/");
		} else {
			return new ModelAndView("admin/error");
		}
	}
	
	

}
