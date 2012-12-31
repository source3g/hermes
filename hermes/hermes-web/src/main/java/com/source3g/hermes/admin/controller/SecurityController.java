package com.source3g.hermes.admin.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.security.admin.Account;
import com.source3g.hermes.entity.security.admin.Resource;
import com.source3g.hermes.entity.security.admin.Role;
import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping(value = "/admin/security")
public class SecurityController {
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/account/add", method = RequestMethod.GET)
	public ModelAndView toAddAccount() {
		return new ModelAndView("/admin/security/accountAdd");
	}

	@RequestMapping(value = "/account/add", method = RequestMethod.POST)
	public ModelAndView addAccount(Account account) {
		Map<String, Object> model = new HashMap<String, Object>();
		String uri = ConfigParams.getBaseUrl() + "admin/security/account/add/";
		HttpEntity<Account> httpEntity = new HttpEntity<Account>(account);
		String result = restTemplate.postForObject(uri, httpEntity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			model.put("success", "success");
			return new ModelAndView("redirect:/admin/security/account/list/", model);
		} else {
			model.put("error", result);
			return new ModelAndView("/admin/security/accountAdd", model);
		}
	}

	// 验证账号是否存在
	@RequestMapping(value = "accountValidate", method = RequestMethod.GET)
	@ResponseBody
	public Boolean accountValidate(String account) {
		String uri = ConfigParams.getBaseUrl() + "admin/security/account/accountValidate/";
		uri += "?account=" + account;
		String resultStr = restTemplate.getForObject(uri, String.class);
		Boolean result = Boolean.valueOf(resultStr);
		return result;
	}

	@RequestMapping(value = "/account/delete/{accountId}", method = RequestMethod.GET)
	public ModelAndView deleteAccount(@PathVariable String accountId) {
		String uri = ConfigParams.getBaseUrl() + "admin/security/account/delete/" + accountId + "/";
		String result = restTemplate.getForObject(uri, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {

		}
		return new ModelAndView("redirect:/admin/security/account/list/");
	}

	@RequestMapping(value = "/account/list", method = RequestMethod.GET)
	public ModelAndView listAccount(String pageNo, String account) {
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		String uri = ConfigParams.getBaseUrl() + "admin/security/account/list/";
		uri += "?pageNo=" + pageNo;
		if (StringUtils.isNotEmpty(account)) {
			uri += "&account=" + account;
		}
		Page page = restTemplate.getForObject(uri, Page.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("page", page);
		model.put("pageNo", pageNo);
		model.put("account", account);
		return new ModelAndView("/admin/security/accountList", model);
	}

	@RequestMapping(value = "/resource/list", method = RequestMethod.GET)
	public ModelAndView listResouce(String pageNo, String name, String code) {
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		String uri = ConfigParams.getBaseUrl() + "admin/security/resource/list/";
		uri += "?pageNo=" + pageNo;
		if (StringUtils.isNotEmpty(name)) {
			uri += "&name=" + name;
		}
		if (StringUtils.isNotEmpty(code)) {
			uri += "&code=" + code;
		}
		Page page = restTemplate.getForObject(uri, Page.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("page", page);
		model.put("pageNo", pageNo);
		model.put("name", name);
		model.put("code", code);

		return new ModelAndView("/admin/security/resourceManage", model);
	}

	@RequestMapping(value = "/resource/add", method = RequestMethod.POST)
	public ModelAndView addResource(@Valid Resource resource, BindingResult errorResult) {
		Map<String, Object> model = new HashMap<String, Object>();
		if (errorResult.hasErrors()) {
			model.put("errors", errorResult.getAllErrors());
			return new ModelAndView("/admin/security/resourceManage", model);
		}
		String uri = ConfigParams.getBaseUrl() + "admin/security/resource/add/";
		HttpEntity<Resource> httpEntity = new HttpEntity<Resource>(resource);
		restTemplate.postForObject(uri, httpEntity, String.class);
		return new ModelAndView("redirect:/admin/security/resource/list/");
	}

	// 验证资源名称,代码是否存在
	@RequestMapping(value = "resourceValidate", method = RequestMethod.GET)
	@ResponseBody
	public Boolean resourceValidate(String name, String code) {
		String uri = ConfigParams.getBaseUrl() + "admin/security/resourceValidate/";
		uri += "?name=" + name;
		uri += "&code=" + code;
		String resultStr = restTemplate.getForObject(uri, String.class);
		Boolean result = Boolean.valueOf(resultStr);
		return result;
	}

	@RequestMapping(value = "/resource/delete/{id}", method = RequestMethod.GET)
	public ModelAndView deleteResource(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "admin/security/resource/delete/" + id + "/";
		restTemplate.getForObject(uri, String.class);
		return new ModelAndView("redirect:/admin/security/resource/list/");
	}

	@RequestMapping(value = "/role/add", method = RequestMethod.GET)
	public ModelAndView toAddRole() {
		return new ModelAndView("/admin/security/roleAdd");
	}

	@RequestMapping(value = "/role/add", method = RequestMethod.POST)
	public ModelAndView addRole(String name, String resourceIds) {
		Map<String, Object> model = new HashMap<String, Object>();
		String uri = ConfigParams.getBaseUrl() + "admin/security/role/add/";
		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
		formData.add("name", name);
		if(StringUtils.isNotEmpty(resourceIds)){
			formData.add("resourceIds", resourceIds);
		}
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, httpHeaders);
		String result = restTemplate.postForObject(uri, requestEntity, String.class);

		if (!ReturnConstants.SUCCESS.equals(result)) {
			model.put("error", result);
			return new ModelAndView("/admin/security/roleAdd", model);
		}
		return new ModelAndView("redirect:/admin/security/role/list/");
	}

	@RequestMapping(value = "/resource/{code}", method = RequestMethod.GET)
	@ResponseBody
	public Resource getResource(@PathVariable String code) {
		String uri = ConfigParams.getBaseUrl() + "admin/security/resource/" + code + "/";
		Resource resource = restTemplate.getForObject(uri, Resource.class);
		return resource;
	}

	@RequestMapping(value = "/role/list", method = RequestMethod.GET)
	public ModelAndView listRole() {
		String uri = ConfigParams.getBaseUrl() + "admin/security/role/list/";
		Role[] roles = restTemplate.getForObject(uri, Role[].class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("roles", roles);
		return new ModelAndView("/admin/security/roleManage", model);
	}

	@RequestMapping(value = "/role/toUpdate/{id}", method = RequestMethod.GET)
	public ModelAndView toUpdate(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "admin/security/role/get/" + id + "/";
		Role role = restTemplate.getForObject(uri, Role.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("role", role);
		return new ModelAndView("/admin/security/roleAdd", model);
	}

	@RequestMapping(value = "/role/update", method = RequestMethod.POST)
	public ModelAndView update(String id, String name, String[] resourceIds) {
		if (StringUtils.isEmpty(id)) {
			return new ModelAndView("error");
		}
		String uri = ConfigParams.getBaseUrl() + "admin/security/role/add/";
		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
		formData.add("name", name);
		formData.add("resourceIds", resourceIds);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, httpHeaders);
		restTemplate.postForObject(uri, requestEntity, String.class);
		return new ModelAndView("/admin/security/roleAdd");
	}

	@RequestMapping(value = "/role/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Role getRole(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "admin/security/role/get/" + id + "/";
		Role role = restTemplate.getForObject(uri, Role.class);
		return role;
	}

	@RequestMapping(value = "/role/notGrant/{accountId}", method = RequestMethod.GET)
	@ResponseBody
	public List<Role> getNotGrantRoles(@PathVariable String accountId) {
		String getAccountUri = ConfigParams.getBaseUrl() + "admin/security/account/get/" + accountId + "/";
		Account account = restTemplate.getForObject(getAccountUri, Account.class);
		String uri = ConfigParams.getBaseUrl() + "admin/security/role/list/";
		Role[] roles = restTemplate.getForObject(uri, Role[].class);
		List<Role> allRoles = Arrays.asList(roles);
		if (account.getRoles() != null) {
			allRoles.removeAll(account.getRoles());
		}
		return allRoles;
	}

	@RequestMapping(value = "/account/grant/{id}", method = RequestMethod.GET)
	public ModelAndView grant(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "admin/security/role/list/";
		Role[] roles = restTemplate.getForObject(uri, Role[].class);
		Map<String, Object> model = new HashMap<String, Object>();
		String getAccountUri = ConfigParams.getBaseUrl() + "admin/security/account/get/" + id + "/";
		Account account = restTemplate.getForObject(getAccountUri, Account.class);
		model.put("roles", roles);
		model.put("account", account);
		return new ModelAndView("/admin/security/grant", model);
	}

	@RequestMapping(value = "/account/{accountId}/role/recover/{id}", method = RequestMethod.GET)
	public ModelAndView recover(@PathVariable String accountId, @PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "admin/security/account/" + accountId + "/role/recover/" + id + "/";
		restTemplate.getForObject(uri, String.class);
		return new ModelAndView("redirect:/admin/security/account/grant/" + accountId + "/");
	}

	@RequestMapping(value = "/account/grant/{id}", method = RequestMethod.POST)
	public ModelAndView grantRoles(@PathVariable String id, String roleIds) {
		if (StringUtils.isEmpty(roleIds)) {
			return new ModelAndView("redirect:/admin/security/account/grant/" + id + "/");
		}

		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
		formData.add("roleIds", roleIds);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, httpHeaders);
		String uri = ConfigParams.getBaseUrl() + "admin/security/account/grant/" + id + "/";
		restTemplate.postForObject(uri, requestEntity, String.class);
		return new ModelAndView("redirect:/admin/security/account/grant/" + id + "/");
	}

}
