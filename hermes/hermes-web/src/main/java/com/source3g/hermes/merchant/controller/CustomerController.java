package com.source3g.hermes.merchant.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.Remind;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping("/merchant/customer")
public class CustomerController {
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView toAdd() {
		return new ModelAndView("/merchant/customer/add");
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView add(Customer customer, BindingResult errorResult) {
		if (errorResult.hasErrors()) {
			return new ModelAndView("merchant/customer/add");
		}
		handleCustomer(customer);
		String uri = ConfigParams.getBaseUrl() + "customer/add/";
		HttpEntity<Customer> httpEntity = new HttpEntity<Customer>(customer);
		String result = restTemplate.postForObject(uri, httpEntity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put(ReturnConstants.SUCCESS, ReturnConstants.SUCCESS);
			return new ModelAndView("merchant/customer/add", model);
		} else {
			return new ModelAndView("merchant/error");
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(Customer customer, String pageNo, HttpServletRequest req) {
		Merchant merchant = (Merchant) WebUtils.getSessionAttribute(req, "merchant");
		if (merchant == null) {
			return new ModelAndView("/merchant/error");
		}
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		StringBuffer uriBuffer = new StringBuffer();
		uriBuffer.append(ConfigParams.getBaseUrl() + "customer/list/");//
		uriBuffer.append(merchant.getId());
		uriBuffer.append("/?pageNo=" + pageNo);

		if (StringUtils.isNotEmpty(customer.getName())) {
			uriBuffer.append("&name=" + customer.getName());
		}
		if (StringUtils.isNotEmpty(customer.getPhone())) {
			uriBuffer.append("&phone=" + customer.getPhone());
		}
		Page page = restTemplate.getForObject(uriBuffer.toString(), Page.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("page", page);

		return new ModelAndView("/merchant/customer/list", model);
	}

	@RequestMapping(value = "/toUpdate/{id}", method = RequestMethod.GET)
	public ModelAndView toUpdate(@PathVariable String id) {
		Map<String, Object> model = new HashMap<String, Object>();
		String uri = ConfigParams.getBaseUrl() + "customer/" + id + "/";
		Customer customer = restTemplate.getForObject(uri, Customer.class);
		model.put("customer", customer);
		model.put(ReturnConstants.UPDATE, true);
		return new ModelAndView("/merchant/customer/add", model);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(Customer customer) {
		handleCustomer(customer);
		String uri = ConfigParams.getBaseUrl() + "customer/update/";
		HttpEntity<Customer> httpEntity = new HttpEntity<Customer>(customer);
		String result = restTemplate.postForObject(uri, httpEntity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put(ReturnConstants.SUCCESS, ReturnConstants.SUCCESS);
			return new ModelAndView("redirect:/merchant/customer/list/", model);
		} else {
			return new ModelAndView("merchant/error");
		}
	}

	private void handleCustomer(Customer customer) {
		List<Remind> reminds = customer.getReminds();
		if (reminds != null) {
			for (int i = reminds.size() - 1; i >= 0; i--) {
				if (reminds.get(i).getRemindTime() == null && StringUtils.isEmpty(reminds.get(i).getName())) {
					reminds.remove(i);
				}
			}
		}
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

}
