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
import com.source3g.hermes.entity.customer.CustomerImportLog;
import com.source3g.hermes.entity.customer.Remind;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.LoginUtils;
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
	public ModelAndView add(Customer customer, BindingResult errorResult, HttpServletRequest req) throws Exception {
		if (errorResult.hasErrors()) {
			return new ModelAndView("merchant/customer/add");
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

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(Customer customer, String pageNo, HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
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
	public ModelAndView importLog(HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "customer/importLog/merchant/" + merchant.getId() + "/";
		CustomerImportLog[] logs = restTemplate.getForObject(uri, CustomerImportLog[].class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("importLogs", logs);
		return new ModelAndView("/merchant/customer/importLog", model);
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
}
