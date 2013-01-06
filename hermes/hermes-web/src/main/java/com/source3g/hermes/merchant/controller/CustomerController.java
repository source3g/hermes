package com.source3g.hermes.merchant.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.CustomerImportItem;
import com.source3g.hermes.entity.customer.Remind;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.enums.TypeEnum.CustomerType;
import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.LoginUtils;
import com.source3g.hermes.utils.Page;
import com.source3g.hermes.vo.CallInStatistics;
import com.source3g.hermes.vo.CallInStatisticsCount;

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
	public ModelAndView add(Customer customer, BindingResult errorResult, HttpServletRequest req) throws Exception {
		if (errorResult.hasErrors()) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("errors", errorResult.getAllErrors());
			return new ModelAndView("merchant/customer/add",model);
		}
		handleCustomer(customer);
		Merchant merchant = (Merchant) LoginUtils.getLoginMerchant(req);
		customer.setMerchantId(merchant.getId());
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
	
	@RequestMapping(value="/get/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Customer get(@PathVariable String id){
		String uri=ConfigParams.getBaseUrl()+"customer/"+id+"/";
		Customer customer=restTemplate.getForObject(uri, Customer.class);
		return customer;
	}

	// 添加顾客验证电话号码去重
	@RequestMapping(value = "/phoneValidate", method = RequestMethod.GET)
	@ResponseBody
	public Boolean phoneValidate(String phone,HttpServletRequest req) throws Exception {
		Merchant merchant=LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "customer/phoneValidate/" + phone +"/"+merchant.getId()+ "/";
		Boolean result = restTemplate.getForObject(uri, Boolean.class);
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(Customer customer, String property, String sortType, String phoneSortType, String pageNo, String type, HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		StringBuffer uriBuffer = new StringBuffer();
		uriBuffer.append(ConfigParams.getBaseUrl() + "customer/list/");
		uriBuffer.append(merchant.getId());
		uriBuffer.append("/?pageNo=" + pageNo);

		if (StringUtils.isNotEmpty(customer.getName())) {
			uriBuffer.append("&name=" + customer.getName());
		}
		if (StringUtils.isNotEmpty(customer.getPhone())) {
			uriBuffer.append("&phone=" + customer.getPhone());
		}
		if (StringUtils.isNotEmpty(sortType)) {
			uriBuffer.append("&sortType=" + sortType);
		}
		if (StringUtils.isNotEmpty(property)) {
			uriBuffer.append("&property=" + property);
		}
		if (StringUtils.isNotEmpty(property)) {
			uriBuffer.append("&phoneSortType=" + phoneSortType);
		}
		if (StringUtils.isEmpty(type)) {
			type = CustomerType.allCustomer.toString();
		}
		uriBuffer.append("&type=" + type);
		Page page = restTemplate.getForObject(uriBuffer.toString(), Page.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("page", page);
		model.put("sortType", sortType);
		model.put("property", property);
		model.put("phoneSortType", phoneSortType);
		model.put("type", type);
		return new ModelAndView("/merchant/customer/list", model);
	}
	
	@RequestMapping(value = "/customerListBycustomerGroupId/{customerGroupId}", method = RequestMethod.GET)
	@ResponseBody
	public Customer[] customerListBycustomerGroupId(@PathVariable String customerGroupId) {
		String uri = ConfigParams.getBaseUrl() + "customer/customerListBycustomerGroupId/" + customerGroupId + "/";
		Customer[] customers = restTemplate.getForObject(uri, Customer[].class);
		return customers;
	}
	
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	@ResponseBody
	public String export(Customer customer, HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		StringBuffer uriBuffer = new StringBuffer();
		uriBuffer.append(ConfigParams.getBaseUrl() + "customer/export/");//
		uriBuffer.append(merchant.getId());
		uriBuffer.append("/?pageNo=1");

		if (StringUtils.isNotEmpty(customer.getName())) {
			uriBuffer.append("&name=" + customer.getName());
		}
		if (StringUtils.isNotEmpty(customer.getPhone())) {
			uriBuffer.append("&phone=" + customer.getPhone());
		}
		String result = restTemplate.getForObject(uriBuffer.toString(), String.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("result", result);
		return result;
	}

	@RequestMapping(value = "/toUpdate/{id}", method = RequestMethod.GET)
	public ModelAndView toUpdate(@PathVariable String id, boolean isNewCustomer) {
		Map<String, Object> model = new HashMap<String, Object>();
		String uri = ConfigParams.getBaseUrl() + "customer/" + id + "/";
		Customer customer = restTemplate.getForObject(uri, Customer.class);
		model.put("customer", customer);
		model.put(ReturnConstants.UPDATE, true);
		if (isNewCustomer == true) {
			model.put("action", "/merchant/customer/updateNew/");
		} else {
			model.put("action", "/merchant/customer/update/");
		}
		return new ModelAndView("/merchant/customer/add", model);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(Customer customer) {
		String result = updateCustomer(customer);
		if (ReturnConstants.SUCCESS.equals(result)) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put(ReturnConstants.SUCCESS, ReturnConstants.SUCCESS);
			return new ModelAndView("redirect:/merchant/customer/list/", model);
		} else {
			return new ModelAndView("merchant/error");
		}
	}

	@RequestMapping(value = "/updateNew", method = RequestMethod.POST)
	public ModelAndView updateNew(Customer customer) {
		String result = updateCustomer(customer);
		if (ReturnConstants.SUCCESS.equals(result)) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put(ReturnConstants.SUCCESS, ReturnConstants.SUCCESS);
			return new ModelAndView("redirect:/merchant/customer/newCustomerList/", model);
		} else {
			return new ModelAndView("merchant/error");
		}
	}

	private String updateCustomer(Customer customer) {
		handleCustomer(customer);
		String uri = ConfigParams.getBaseUrl() + "customer/update/";
		HttpEntity<Customer> httpEntity = new HttpEntity<Customer>(customer);
		String result = restTemplate.postForObject(uri, httpEntity, String.class);
		return result;
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "customer/delete/" + id + "/";
		String result = restTemplate.getForObject(uri, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return new ModelAndView("redirect:/merchant/customer/list/");
		} else {
			return new ModelAndView("/merchant/error");
		}
	}

	@RequestMapping(value = "/newCustomerList", method = RequestMethod.GET)
	public ModelAndView newCustomerList(Customer customer, String pageNo, HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		StringBuffer uriBuffer = new StringBuffer();
		uriBuffer.append(ConfigParams.getBaseUrl() + "customer/newCustomerList/");//
		uriBuffer.append(merchant.getId());
		uriBuffer.append("/?pageNo=" + pageNo);
		if (StringUtils.isNotEmpty(customer.getPhone())) {
			uriBuffer.append("&phone=" + customer.getPhone());
		}
		Page page = restTemplate.getForObject(uriBuffer.toString(), Page.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("page", page);
		return new ModelAndView("/merchant/customer/newCustomerList", model);
	}

	@RequestMapping(value = "/import", method = RequestMethod.GET)
	public ModelAndView toImport() {
		return new ModelAndView("/merchant/customer/import");
	}

	@RequestMapping(value = "/import", method = RequestMethod.POST)
	@ResponseBody
	public String importCustomer(@RequestParam("file") MultipartFile file, HttpServletRequest req) {
		File fileToCopy = new File("/temp/file/" + new Date().getTime());
		try {
			Merchant merchant = LoginUtils.getLoginMerchant(req);
			String uri = ConfigParams.getBaseUrl() + "customer/import/" + merchant.getId() + "/";
			FileUtils.copyInputStreamToFile(file.getInputStream(), fileToCopy);
			Resource resource = new FileSystemResource(fileToCopy);
			MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
			formData.add("file", resource);
			formData.add("oldName", new String(file.getOriginalFilename()));

			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, requestHeaders);
			String result = restTemplate.postForObject(uri, requestEntity, String.class);
			System.out.println(result);
			// ResponseEntity<String> response = restTemplate.exchange(uri,
			// HttpMethod.POST, requestEntity, String.class);
			// System.out.println(response.getBody());
		} catch (Exception e) {
			return "上传失败";
		} finally {
			fileToCopy.delete();
		}
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/importLog", method = RequestMethod.GET)
	public ModelAndView importLog(HttpServletRequest req, String pageNo, String startTime, String endTime) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		String uri = ConfigParams.getBaseUrl() + "customer/importLog/merchant/" + merchant.getId() + "/?pageNo=" + pageNo;
		if (StringUtils.isNotEmpty(startTime)) {
			uri += "&startTime=" + startTime;
		}
		if (StringUtils.isNotEmpty(endTime)) {
			uri += "&endTime=" + endTime;
		}
		Page page = restTemplate.getForObject(uri, Page.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("page", page);
		model.put("startTime", startTime);
		model.put("endTime", endTime);
		return new ModelAndView("/merchant/customer/importLog", model);
	}

	@RequestMapping(value = "/importLog/items/{id}", method = RequestMethod.GET)
	public ModelAndView importLogMerchantInfo(@PathVariable String id) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		String uri = ConfigParams.getBaseUrl() + "customer/importLog/items/" + id + "/";
		@SuppressWarnings("unchecked")
		List<CustomerImportItem> customerImportItem = restTemplate.getForObject(uri, List.class);
		model.put("customerImportItem", customerImportItem);
		return new ModelAndView("/merchant/customer/importItems", model);
	}

	@RequestMapping(value = "/callInList", method = RequestMethod.GET)
	public ModelAndView callInList(Customer customer, String pageNo, String customerType, HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		StringBuffer uriBuffer = new StringBuffer();
		uriBuffer.append(ConfigParams.getBaseUrl() + "customer/callInList/all/");//
		uriBuffer.append(merchant.getId());
		uriBuffer.append("/?pageNo=" + pageNo);
		if (StringUtils.isNotEmpty(customer.getPhone())) {
			uriBuffer.append("&phone=" + customer.getPhone());
		}
		if (StringUtils.isEmpty(customerType)) {
			customerType = "allCustomer";
		}
		uriBuffer.append("&customerType=" + customerType);
		Page page = restTemplate.getForObject(uriBuffer.toString(), Page.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("page", page);
		model.put("customerType", customerType);
		return new ModelAndView("/merchant/customer/callInList", model);
	}

	@RequestMapping(value = "/callInStatistics", method = RequestMethod.GET)
	public ModelAndView callInStatistics(HttpServletRequest req, String startTime, String endTime) throws Exception {
		CallInStatistics result = findCallInStatistics(req, startTime, endTime);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("callInStatistics", result);
		return new ModelAndView("/merchant/customer/callInStatistics", model);
	}

	@RequestMapping(value = "/callInStatistics/today", method = RequestMethod.GET)
	@ResponseBody
	public CallInStatisticsCount todayCallInList(HttpServletRequest req, String startTime, String endTime) throws Exception {
		Merchant merchant = (Merchant) LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "customer/callInStatistics/today/" + merchant.getId() + "/?a=1";
		if (StringUtils.isNotEmpty(endTime)) {
			uri += "&endTime=" + endTime;
		}
		if (StringUtils.isNotEmpty(startTime)) {
			uri += "&startTime=" + startTime;
		}
		CallInStatisticsCount callInStatisticsToday = restTemplate.getForObject(uri, CallInStatisticsCount.class);
		return callInStatisticsToday;
	}

	@RequestMapping(value = "/callInStatisticsJson", method = RequestMethod.GET)
	@ResponseBody
	public CallInStatistics callInStatisticsJson(HttpServletRequest req, String startTime, String endTime) throws Exception {
		CallInStatistics result = findCallInStatistics(req, startTime, endTime);
		return result;
	}

	private CallInStatistics findCallInStatistics(HttpServletRequest req, String startTime, String endTime) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "customer/callInStatistics/" + merchant.getId() + "/?a=1";
		if (StringUtils.isNotEmpty(endTime)) {
			uri += "&endTime=" + endTime;
		}
		if (StringUtils.isNotEmpty(startTime)) {
			uri += "&startTime=" + startTime;
		}
		CallInStatistics callInStatistics = restTemplate.getForObject(uri, CallInStatistics.class);
		return callInStatistics;
	}

	private void handleCustomer(Customer customer) {
		List<Remind> reminds = customer.getReminds();
		if (reminds != null) {
			for (int i = reminds.size() - 1; i >= 0; i--) {
				if (reminds.get(i).getRemindTime() == null && reminds.get(i).getMerchantRemindTemplate().getId()!=null) {
					reminds.remove(i);
				}
			}
		}
	}
}
