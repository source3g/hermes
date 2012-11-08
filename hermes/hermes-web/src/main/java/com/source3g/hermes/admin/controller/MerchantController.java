package com.source3g.hermes.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

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
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.utils.ConfigParams;


@Controller
@RequestMapping("/admin/merchant")
public class MerchantController {
	private static final Logger logger=LoggerFactory.getLogger(MerchantController.class);
	
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public ModelAndView toAdd() {
		return new ModelAndView("admin/merchant/add");
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	public ModelAndView add(@Valid Merchant merchant, BindingResult errorResult) {
		if (errorResult.hasErrors()) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("errors", errorResult.getAllErrors());
			return new ModelAndView("admin/merchant/add", model);
		}
		String uri = ConfigParams.getBaseUrl()+"merchant/add";
		HttpEntity<Merchant> entity = new HttpEntity<Merchant>(merchant);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if ("\"success\"".equals(result)) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("success", "success");
			return new ModelAndView("admin/merchant/add", model);
		} else {
			return new ModelAndView("admin/error");
		}
	}

	@RequestMapping( value="list", method = RequestMethod.GET)
	public ModelAndView list() {
		logger.debug("list.......");
		String uri = ConfigParams.getBaseUrl()+"merchant/list/";
		Merchant[] merchants = restTemplate.getForObject(uri, Merchant[].class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("merchants", merchants);
		return new ModelAndView("admin/merchant/list", model);
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable String id) {
		String uri =  ConfigParams.getBaseUrl()+"merchant/delete/" + id+"/";
		restTemplate.getForObject(uri, String.class);
		return new ModelAndView("redirect:/admin/merchant/list/");
	}
	@RequestMapping(value="/toModify/{id}" ,method = RequestMethod.GET)
	public ModelAndView toModify(@PathVariable String id){
		String uri = ConfigParams.getBaseUrl()+"merchant/"+id+"/";
		Merchant merchant=restTemplate.getForObject(uri, Merchant.class);
		Map<String,Object> model=new HashMap<String,Object>();
		model.put("merchant", merchant);
		return new ModelAndView("admin/merchant/add",model);
	}
	@RequestMapping(value="/update" ,method =RequestMethod.POST)
	public ModelAndView update(@Valid Merchant merchant, BindingResult errorResult){
		String uri =  ConfigParams.getBaseUrl()+"merchant/update/";
		
		HttpEntity<Merchant> entity=new HttpEntity<Merchant>(merchant);
		String result = restTemplate.postForObject(uri, entity, String.class);
		
		if (ReturnConstants.SUCCESS_WIDTH_QUOT.equals(result)) {
			return new ModelAndView("redirect:/admin/merchant/list/");
		}else{
			return new ModelAndView("admin/error");
		}
		
	}
}
