package com.source3g.hermes.customer.api;

import java.util.List;

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
import com.source3g.hermes.customer.service.CustomerGroupService;
import com.source3g.hermes.entity.customer.CustomerGroup;

@Controller
@RequestMapping("/customerGroup")
public class CustomerGroupApi {

	private Logger logger = LoggerFactory.getLogger(CustomerApi.class);

	@Autowired
	private CustomerGroupService customerGroupService;

	@RequestMapping(value = "/listAll/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public List<CustomerGroup> listAll(@PathVariable String merchantId) {
		logger.info("list all customerGroup");
		return customerGroupService.listAll(merchantId);
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody CustomerGroup customerGroup) {
		customerGroupService.add(customerGroup);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable String id) {
		customerGroupService.deleteById(id);
		return ReturnConstants.SUCCESS;
	}

}
