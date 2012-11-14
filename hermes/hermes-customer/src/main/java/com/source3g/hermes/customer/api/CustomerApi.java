package com.source3g.hermes.customer.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.customer.service.CustomerService;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping("/customer")
public class CustomerApi {
	private Logger logger = LoggerFactory.getLogger(CustomerApi.class);

	@Autowired
	private CustomerService customerService;

	@RequestMapping(value = "/add/", method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody Customer customer) {
		customerService.add(customer);
		return "success";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Customer get(@PathVariable String id) {
		return customerService.get(id);
	}

	@RequestMapping(value = "/list/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public Page list(String pageNo, String name, String phone, @PathVariable String merchantId) {
		logger.debug("list customer....");
		int pageNoInt = Integer.valueOf(pageNo);
		Customer customer = new Customer();
		customer.setName(name);
		customer.setMerchantId(merchantId);
		customer.setPhone(phone);
		return customerService.list(pageNoInt, customer);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@RequestBody Customer customer) {
		logger.debug("update customer....");
		customerService.update(customer);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/callIn/{sn}/{phone}/{time}/{duration}", method = RequestMethod.POST)
	@ResponseBody
	public String callIn(@PathVariable String deviceSn, @PathVariable String phone, @PathVariable String time, @PathVariable String duration) {
		try {
			customerService.callIn(deviceSn, phone, time, duration);
		} catch (Exception e) {
			return e.getMessage();
		}
		return ReturnConstants.SUCCESS;
	}

}
