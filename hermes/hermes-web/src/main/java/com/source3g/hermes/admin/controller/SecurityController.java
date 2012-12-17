package com.source3g.hermes.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.security.entity.Account;
import com.source3g.hermes.security.entity.Resource;
import com.source3g.hermes.security.entity.Role;
import com.source3g.hermes.security.service.SecurityService;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping(value = "/admin/security")
public class SecurityController {

	@Autowired
	private SecurityService securityService;

	@RequestMapping(value = "/account/add", method = RequestMethod.GET)
	public ModelAndView toAddAccount() {
		return new ModelAndView("/admin/security/accountAdd");
	}

	@RequestMapping(value = "/account/add", method = RequestMethod.POST)
	public ModelAndView addAccount(Account account) {
		securityService.addAccount(account);
		return new ModelAndView("/admin/security/accountAdd");
	}
	
	@RequestMapping(value = "/account/delete/{accountId}", method = RequestMethod.GET)
	public ModelAndView deleteAccount(@PathVariable String accountId) {
		securityService.deleteAccount(accountId);
		return new ModelAndView("redirect:/admin/security/account/list/");
	}

	@RequestMapping(value = "/account/list", method = RequestMethod.GET)
	public ModelAndView listAccount(String pageNo, String account) {
		int pageNoInt = 0;
		if (StringUtils.isNotEmpty(pageNo)) {
			pageNoInt = Integer.parseInt(pageNo);
		}
		Page page = securityService.listAccount(pageNoInt, account);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("page", page);
		model.put("pageNo", pageNo);
		model.put("account", account);
		return new ModelAndView("/admin/security/accountList", model);
	}

	@RequestMapping(value = "/resource/list", method = RequestMethod.GET)
	public ModelAndView listResouce(String pageNo, String name, String code) {
		int pageNoInt = 0;
		if (StringUtils.isNotEmpty(pageNo)) {
			pageNoInt = Integer.parseInt(pageNo);
		}
		Page page = securityService.listResource(pageNoInt, name, code);
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
		securityService.addResource(resource);
		return new ModelAndView("redirect:/admin/security/resource/list/");
	}

	@RequestMapping(value = "/resource/delete/{id}", method = RequestMethod.GET)
	public ModelAndView deleteResource(@PathVariable String id) {
		securityService.deleteResource(id);
		return new ModelAndView("redirect:/admin/security/resource/list/");
	}

	@RequestMapping(value = "/role/add", method = RequestMethod.GET)
	public ModelAndView toAddRole() {
		return new ModelAndView("/admin/security/roleAdd");
	}

	@RequestMapping(value = "/role/add", method = RequestMethod.POST)
	public ModelAndView addRole(String name, String[] resourceIds) {
		securityService.addRole(name, resourceIds);
		return new ModelAndView("/admin/security/roleAdd");
	}

	@RequestMapping(value = "/resource/{code}", method = RequestMethod.GET)
	@ResponseBody
	public Resource getResource(@PathVariable String code) {
		Resource resource = securityService.findResourceByCode(code);
		return resource;
	}

	@RequestMapping(value = "/role/list", method = RequestMethod.GET)
	public ModelAndView listRole() {
		List<Role> roles = securityService.listRole();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("roles", roles);
		return new ModelAndView("/admin/security/roleManage", model);
	}

	@RequestMapping(value = "/role/toUpdate/{id}", method = RequestMethod.GET)
	public ModelAndView toUpdate(@PathVariable String id) {
		Role role = securityService.getRoleById(id);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("role", role);
		return new ModelAndView("/admin/security/roleAdd", model);
	}

	@RequestMapping(value = "/role/update", method = RequestMethod.POST)
	public ModelAndView update(String id, String name, String[] resourceIds) {
		if (StringUtils.isEmpty(id)) {
			return new ModelAndView("error");
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
		return new ModelAndView("/admin/security/roleAdd");
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

	@RequestMapping(value = "/account/grant/{id}", method = RequestMethod.GET)
	public ModelAndView grant(@PathVariable String id) {
		List<Role> roles = securityService.listRole();
		Account account = securityService.getAccountById(id);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("roles", roles);
		model.put("account", account);
		return new ModelAndView("/admin/security/grant", model);
	}
	
	@RequestMapping(value = "/account/{accountId}/role/recover/{id}", method = RequestMethod.GET)
	public ModelAndView recover(@PathVariable String accountId,@PathVariable String id) {
		securityService.recoverRole(accountId,id);
		return new ModelAndView("redirect:/admin/security/account/grant/"+accountId+"/");
	}

	@RequestMapping(value = "/account/grant/{id}", method = RequestMethod.POST)
	public ModelAndView grantRoles(@PathVariable String id, String roleIds) {
		if (StringUtils.isEmpty(roleIds)) {
			return new ModelAndView("redirect:/admin/security/account/grant/"+id+"/");
		}
		roleIds = roleIds.trim();
		String[] roleIdArray = roleIds.split(",");
		securityService.grant(id,roleIdArray);
		return new ModelAndView("redirect:/admin/security/account/grant/"+id+"/");
	}

}
