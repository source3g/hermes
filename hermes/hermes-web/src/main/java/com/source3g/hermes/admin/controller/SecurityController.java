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
import com.source3g.hermes.utils.ConfigParams;
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
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			securityService.addAccount(account);
		} catch (Exception e) {
			String error=e.getMessage();
			model.put("error", error);
		return new ModelAndView("/admin/security/accountAdd",model);
		}
		model.put("success", "success");
		return new ModelAndView("/admin/security/accountList",model);
	}
	
	//验证账号是否存在
	@RequestMapping(value = "accountValidate", method = RequestMethod.GET)
	@ResponseBody
	public Boolean accountValidate(String account) {
		Boolean result=securityService.accountValidate(account);
		return result;
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

	//验证资源名称,代码是否存在
	@RequestMapping(value = "resourceValidate", method = RequestMethod.GET)
	@ResponseBody
	public Boolean resourceValidate(String name,String code) {
		Boolean result=securityService.resourceValidate(name,code);
		return result;
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
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			securityService.addRole(name, resourceIds);
		} catch (Exception e) {
		String error=e.getMessage();
		model.put("error", error);
		return new ModelAndView("/admin/security/roleAdd",model);
		}
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
		Role role = securityService.getById(id);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("role", role);
		return new ModelAndView("/admin/security/roleAdd", model);
	}

	@RequestMapping(value = "/role/update", method = RequestMethod.POST)
	public ModelAndView update(String id, String name, String[] resourceIds) {
		if (StringUtils.isEmpty(id)) {
			return new ModelAndView("error");
		}
		Role role = securityService.getById(id);
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
		Role role = securityService.getById(id);
		return role;
	}

	@RequestMapping(value = "/account/grant/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView grant(@PathVariable String id) {
		List<Role> roles = securityService.listRole();
		Account account = securityService.findAccountById(id);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("roles", roles);
		model.put("account", account);
		return new ModelAndView("/admin/security/grant", model);
	}

}
