package com.source3g.hermes.security.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.security.admin.Account;
import com.source3g.hermes.entity.security.admin.Resource;
import com.source3g.hermes.entity.security.admin.Role;
import com.source3g.hermes.security.service.SecurityService;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping(value = "/admin/security")
public class AdminSecurityApi {

	@Autowired
	private SecurityService securityService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public Account login(String username, String password) {
		return securityService.login(username, password);
	}

	@RequestMapping(value = "/install", method = RequestMethod.GET)
	@ResponseBody
	public void install() {
		securityService.install();
	}

	@RequestMapping(value = "/account/add", method = RequestMethod.POST)
	@ResponseBody
	public String addAccount(@RequestBody Account account) {
		try {
			securityService.addAccount(account);
		} catch (Exception e) {
			String error = e.getMessage();
			return error;
		}
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/account/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Account addAccount(@PathVariable String id) {
		Account account = securityService.getAccountById(id);
		return account;
	}

	// 验证账号是否存在
	@RequestMapping(value = "/account/accountValidate", method = RequestMethod.GET)
	@ResponseBody
	public Boolean accountValidate(String account) {
		Boolean result = securityService.accountValidate(account);
		return result;
	}

	@RequestMapping(value = "/account/delete/{accountId}", method = RequestMethod.GET)
	@ResponseBody
	public String deleteAccount(@PathVariable String accountId) {
		securityService.deleteAccount(accountId);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/account/list", method = RequestMethod.GET)
	@ResponseBody
	public Page listAccount(String pageNo, String account) {
		int pageNoInt = 0;
		if (StringUtils.isNotEmpty(pageNo)) {
			pageNoInt = Integer.parseInt(pageNo);
		}
		Page page = securityService.listAccount(pageNoInt, account);
		return page;
	}

	@RequestMapping(value = "/resource/list", method = RequestMethod.GET)
	@ResponseBody
	public Page listResouce(String pageNo, String name, String code) {

		int pageNoInt = 0;
		if (StringUtils.isNotEmpty(pageNo)) {
			pageNoInt = Integer.parseInt(pageNo);
		}
		Page page = securityService.listResource(pageNoInt, name, code);
		return page;
	}

	@RequestMapping(value = "/resource/add", method = RequestMethod.POST)
	@ResponseBody
	public String addResource(@RequestBody Resource resource) {// ,
																// BindingResult
																// errorResult
		// if (errorResult.hasErrors()) {
		// String error = "";
		// for (ObjectError err : errorResult.getAllErrors()) {
		// error += "|" + err.getDefaultMessage();
		// }
		// return error;
		// }
		securityService.addResource(resource);
		return ReturnConstants.SUCCESS;
	}

	// 验证资源名称,代码是否存在
	@RequestMapping(value = "resourceValidate", method = RequestMethod.GET)
	@ResponseBody
	public Boolean resourceValidate(String name, String code) {
		Boolean result = securityService.resourceValidate(name, code);
		return result;
	}

	@RequestMapping(value = "/resource/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String deleteResource(@PathVariable String id) {
		securityService.deleteResource(id);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/role/add", method = RequestMethod.POST)
	@ResponseBody
	public String addRole(String name, String[] resourceIds) {
		try {
			securityService.addRole(name, resourceIds);
		} catch (Exception e) {
			return e.getMessage();
		}
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/resource/{code}", method = RequestMethod.GET)
	@ResponseBody
	public Resource getResource(@PathVariable String code) {
		Resource resource = securityService.findResourceByCode(code);
		return resource;
	}

	@RequestMapping(value = "/role/list", method = RequestMethod.GET)
	@ResponseBody
	public List<Role> listRole() {
		List<Role> roles = securityService.listRole();
		return roles;
	}

	@RequestMapping(value = "/role/update", method = RequestMethod.POST)
	@ResponseBody
	public String update(String id, String name, String[] resourceIds) {
		if (StringUtils.isEmpty(id)) {
			return "id不能为空";
		}
		Role role = securityService.getRoleById(id);
		role.setName(name);
		List<Resource> result = new ArrayList<Resource>();
		for (String resourceId : resourceIds) {
			Resource r = new Resource();
			r.setId(resourceId);
			result.add(r);
		}
		role.setResources(result);
		securityService.update(role);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/role/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable String id) {
		securityService.delete(id);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/role/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Role getRole(@PathVariable String id) {
		Role role = securityService.getRoleById(id);
		return role;
	}

	@RequestMapping(value = "/role/notGrant/{accountId}", method = RequestMethod.GET)
	@ResponseBody
	public List<Role> getNotGrantRoles(@PathVariable String accountId) {
		Account account = securityService.getAccountById(accountId);
		List<Role> allRoles = securityService.listRole();
		if (account.getRoles() != null) {
			allRoles.removeAll(account.getRoles());
		}
		return allRoles;
	}

	// @RequestMapping(value = "/account/grant/{id}", method =
	// RequestMethod.GET)
	// @ResponseBody
	// public String grant(@PathVariable String id) {
	// List<Role> roles = securityService.listRole();
	// Account account = securityService.getAccountById(id);
	// Map<String, Object> model = new HashMap<String, Object>();
	// model.put("roles", roles);
	// model.put("account", account);
	// return new ModelAndView("/admin/security/grant", model);
	// }

	@RequestMapping(value = "/account/{accountId}/role/recover/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String recover(@PathVariable String accountId, @PathVariable String id) {
		securityService.recoverRole(accountId, id);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/account/grant/{id}", method = RequestMethod.POST)
	@ResponseBody
	public String grantRoles(@PathVariable String id, String roleIds) {
		if (StringUtils.isEmpty(roleIds)) {
			return ReturnConstants.SUCCESS;
		}
		roleIds = roleIds.trim();
		String[] roleIdArray = roleIds.split(",");
		securityService.grant(id, roleIdArray);
		return ReturnConstants.SUCCESS;
	}

}
