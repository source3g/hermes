package com.source3g.hermes.customer.api;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.customer.service.CustomerImportService;
import com.source3g.hermes.customer.service.CustomerService;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.CustomerImportLog;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.enums.ImportStatus;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping("/customer")
public class CustomerApi {
	private Logger logger = LoggerFactory.getLogger(CustomerApi.class);

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerImportService customerImportService;

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
		customer.setMerchantId(new ObjectId(merchantId));
		customer.setPhone(phone);
		return customerService.list(pageNoInt, customer);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@RequestBody Customer customer) {
		logger.debug("update customer....");
		customerService.updateExcludeProperties(customer, "merchantId", "callRecords");
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable String id) {
		customerService.deleteById(id, Customer.class);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/callIn/{deviceSn}/{phone}/{time}/{duration}", method = RequestMethod.GET)
	@ResponseBody
	public String callIn(@PathVariable String deviceSn, @PathVariable String phone, @PathVariable String time, @PathVariable String duration) {
		try {
			customerService.callIn(deviceSn, phone, time, duration);
		} catch (Exception e) {
			return e.getMessage();
		}
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/newCustomerList/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public Page newCustomerList(String pageNo, String name, String phone, @PathVariable String merchantId) {
		int pageNoInt = Integer.valueOf(pageNo);
		Customer customer = new Customer();
		customer.setName(name);
		customer.setMerchantId(new ObjectId(merchantId));
		customer.setPhone(phone);
		return customerService.list(pageNoInt, customer, true);
	}

	@RequestMapping(value = "/importLog/merchant/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public List<CustomerImportLog> importLog(@PathVariable String merchantId) {
		return customerImportService.findImportLog(merchantId);
	}

	@RequestMapping(value = "/importA", method = RequestMethod.POST)
	@ResponseBody
	public String testImport(String a) throws Exception {
		System.out.println(a);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/import/{merchantId}", method = RequestMethod.POST)
	@ResponseBody
	public String importCustomer(@RequestParam("file") MultipartFile file, @RequestParam("oldName") String oldName, @PathVariable String merchantId) {// MultipartFile
		String dir = customerService.getTempDir() + file.getOriginalFilename();
		File fileToCopy = new File(dir);
		try {
			FileUtils.copyInputStreamToFile(file.getInputStream(), fileToCopy);
		} catch (IOException e) {
			return "拷贝失败";
		}
		CustomerImportLog importLog = new CustomerImportLog();
		importLog.setId(ObjectId.get());
		Merchant merchant = new Merchant();
		merchant.setId(new ObjectId(merchantId));
		importLog.setMerchant(merchant);
		importLog.setName(oldName);
		importLog.setNewName(file.getOriginalFilename());
		importLog.setStatus(ImportStatus.已接收准备导入.toString());
		importLog.setFilePath(fileToCopy.getAbsolutePath());
		try {
			customerService.addImportLog(importLog);
		} catch (Exception e) {
			return e.getMessage();
		}
		return ReturnConstants.SUCCESS;
	}
}
