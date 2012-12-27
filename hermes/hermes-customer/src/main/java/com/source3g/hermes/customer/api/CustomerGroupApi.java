package com.source3g.hermes.customer.api;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.customer.service.CustomerGroupService;
import com.source3g.hermes.entity.Device;
import com.source3g.hermes.entity.customer.CustomerGroup;
import com.source3g.hermes.entity.merchant.Merchant;

@Controller
@RequestMapping("/customerGroup")
public class CustomerGroupApi {

	private Logger logger = LoggerFactory.getLogger(CustomerApi.class);

	@Autowired
	private CustomerGroupService customerGroupService;
	@Autowired
	private MongoTemplate mongoTemplate;

	@RequestMapping(value = "/listAll/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public List<CustomerGroup> listAll(@PathVariable String merchantId) {
		logger.info("list all customerGroup");
		return customerGroupService.listAll(merchantId);
	}

	@RequestMapping(value = "/listAll/sn/{sn}", method = RequestMethod.GET)
	@ResponseBody
	public List<CustomerGroup> listAllBySn(@PathVariable String sn) {
		logger.info("list all customerGroup");
		Device device = mongoTemplate.findOne(new Query(Criteria.where("sn").is(sn)), Device.class);
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("deviceIds").is(device.getId())), Merchant.class);
		return customerGroupService.listAll(merchant.getId().toString());
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody CustomerGroup customerGroup) {
		try {
			customerGroupService.add(customerGroup);
		} catch (Exception e) {
		return e.getMessage();
		}
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/nameValidate/{id}/{name}", method = RequestMethod.GET)
	@ResponseBody
	public boolean nameValidate(@PathVariable ObjectId id,@PathVariable String name) {	
		return customerGroupService.nameValidate(id,name);
	}
	
	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable String id) {
		customerGroupService.deleteById(id, CustomerGroup.class);
		return ReturnConstants.SUCCESS;
	}
	
	@RequestMapping(value = "updateCustomerGroup/{customerGroupId}/{selectorId}/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public String updateCustomerGroup(@PathVariable String customerGroupId,@PathVariable String selectorId,@PathVariable String merchantId) {
		customerGroupService.updateCustomerGroup(customerGroupId,selectorId,merchantId);
		return ReturnConstants.SUCCESS;
	}

}
