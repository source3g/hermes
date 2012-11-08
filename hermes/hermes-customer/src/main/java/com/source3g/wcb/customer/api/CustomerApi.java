package com.source3g.wcb.customer.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.source3g.wcb.customer.service.CustomerService;
import com.source3g.wcb.entity.customer.Customer;

@Controller
@RequestMapping("/customer")
public class CustomerApi {

	@Autowired
	private CustomerService customerService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody Customer customer) {
		customerService.add(customer);
		return "success";
	}
	
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	@ResponseBody
	public List<Customer> listAll() {
		List<Customer> list= customerService.listAll();
		return list;
	}
	
}
