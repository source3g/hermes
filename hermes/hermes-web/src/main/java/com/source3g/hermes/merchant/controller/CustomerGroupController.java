package com.source3g.hermes.merchant.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import com.source3g.hermes.entity.customer.CustomerGroup;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.LoginUtils;

@Controller
@RequestMapping("/merchant/customerGroup")
public class CustomerGroupController {
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView toIndex(HttpServletRequest request) throws Exception {
		Merchant merchant = (Merchant) LoginUtils.getLoginMerchant(request);
		Map<String, Object> model =toIndex( merchant);
		return new ModelAndView("/merchant/customerGroup/index", model);
	}

	public Map<String, Object> toIndex(Merchant merchant){
		String uri = ConfigParams.getBaseUrl() + "customerGroup/listAll/" + merchant.getId() + "/";
		CustomerGroup[] customerGroups = restTemplate.getForObject(uri, CustomerGroup[].class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("customerGroups", customerGroups);
		return model;
	}
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public ModelAndView add(@Valid CustomerGroup customerGroup, BindingResult errorResult, HttpServletRequest req) {
		Merchant merchant = (Merchant) req.getSession().getAttribute("loginUser");
		if (errorResult.hasErrors()) {
			Map<String, Object> model=new HashMap<String, Object>();
			model.put("errors", errorResult.getAllErrors());
			return new ModelAndView("redirect:/merchant/customerGroup/", model);
		}
		String uri = ConfigParams.getBaseUrl() + "customerGroup/add";
		customerGroup.setMerchantId(merchant.getId());
		HttpEntity<CustomerGroup> entity = new HttpEntity<CustomerGroup>(customerGroup);
		String result = restTemplate.postForObject(uri, entity, String.class);
		Map<String, Object> model = toIndex( merchant);
		if (ReturnConstants.SUCCESS.equals(result)) {
			model.put(ReturnConstants.SUCCESS, ReturnConstants.SUCCESS);
			return new ModelAndView("/merchant/customerGroup/index", model);
		} else {
			model.put("error",result);
			return new ModelAndView("/merchant/customerGroup/index", model);
		}
	}
	
	//验证顾客组名称是否存在
		@RequestMapping(value = "nameValidate", method = RequestMethod.GET)
		@ResponseBody
		public Boolean nameValidate(String name,HttpServletRequest req) {
			Merchant merchant = (Merchant) req.getSession().getAttribute("loginUser");
			String uri=ConfigParams.getBaseUrl() + "customerGroup/nameValidate/"+merchant.getId()+"/"+name+"/";
			Boolean result = restTemplate.getForObject(uri, Boolean.class);
			return result;
		}
		
	@RequestMapping(value = "/listAllJson", method = RequestMethod.GET)
	@ResponseBody
	public List<CustomerGroup> listAll(HttpServletRequest req) throws Exception {
		Merchant merchant = (Merchant) LoginUtils.getLoginMerchant();
		String uri = ConfigParams.getBaseUrl() + "customerGroup/listAll/" + merchant.getId() + "/";
		CustomerGroup[] customerGroups = restTemplate.getForObject(uri, CustomerGroup[].class);
		return Arrays.asList(customerGroups);
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "customerGroup/delete/" + id + "/";
		restTemplate.getForObject(uri, String.class);
		return new ModelAndView("redirect:/merchant/customerGroup/");
	}
	
	@RequestMapping(value = "/update/{customerGroupId}/{selectorId}", method = RequestMethod.GET)
	public ModelAndView updateCustomerGroup(@PathVariable String customerGroupId,@PathVariable String selectorId,HttpServletRequest req) throws Exception {
		Merchant merchant=LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "customerGroup/updateCustomerGroup/" + customerGroupId + "/"+selectorId+"/"+merchant.getId()+"/";
		restTemplate.getForObject(uri, String.class);
		return new ModelAndView("redirect:/merchant/customerGroup/");
	}

}
