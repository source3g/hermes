package com.source3g.hermes.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.utils.ConfigParams;
import com.sourse3g.hermes.branch.BranchCompany;
import com.sourse3g.hermes.branch.Saler;

@Controller
@RequestMapping("/admin/dictionary")
@RequiresRoles("admin")
public class DictionaryController {

	@Autowired
	private RestTemplate restTemplate;

//	@RequestMapping(value = "remind/{id}", method = RequestMethod.GET)
//	@ResponseBody
//	public RemindTemplate getRemind(@PathVariable String id) {
//		if (StringUtils.isEmpty(id)) {
//			return null;
//		}
//		String uri = ConfigParams.getBaseUrl() + "dictionary/remind/get/" + id + "/";
//		RemindTemplate remindTemplate = restTemplate.getForObject(uri, RemindTemplate.class);
//		return remindTemplate;
//	}

//	@RequestMapping(value = "toRemindTemplate", method = RequestMethod.GET)
//	public ModelAndView toRemindTemplate() {
//		String uri = ConfigParams.getBaseUrl() + "dictionary/remindSetting/";
//		RemindTemplate[] remindTemplate = restTemplate.getForObject(uri, RemindTemplate[].class);
//		Map<String, Object> model = new HashMap<String, Object>();
//		model.put("remindTemplate", remindTemplate);
//		return new ModelAndView("admin/dataDictionary/remindTemplate", model);
//	}

//	@RequestMapping(value = "/remindAdd", method = RequestMethod.POST)
//	public ModelAndView remindAdd(@Valid RemindTemplate remindTemplate, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws Exception {
//		if (bindingResult.hasErrors()) {
//			// TODO 要改
//			return new ModelAndView("error");
//		}
//		String uri = ConfigParams.getBaseUrl() + "dictionary/remindAdd/";
//		HttpEntity<RemindTemplate> entity = new HttpEntity<RemindTemplate>(remindTemplate);
//		String result = restTemplate.postForObject(uri, entity, String.class);
//		if (ReturnConstants.SUCCESS.equals(result)) {
//			return new ModelAndView("redirect:/admin/dictionary/toRemindTemplate");
//		} else {
//			redirectAttributes.addFlashAttribute("error", result);
//			return new ModelAndView("redirect:/admin/dictionary/toRemindTemplate");
//		}
//	}

	// 验证标题是否重复
	@RequestMapping(value = "/titleValidate", method = RequestMethod.GET)
	@ResponseBody
	public Boolean titleValidate(String title) throws Exception {
		String uri = ConfigParams.getBaseUrl() + "dictionary/titleValidate/" + title + "/";
		Boolean result = restTemplate.getForObject(uri, Boolean.class);
		return result;
	}

//	@RequestMapping(value = "/remindSave", method = RequestMethod.POST)
//	public ModelAndView remindSave(RemindTemplate remindTemplate, RedirectAttributes redirectAttributes) throws Exception {
//		String uri = ConfigParams.getBaseUrl() + "dictionary/remindSave/";
//		HttpEntity<RemindTemplate> entity = new HttpEntity<RemindTemplate>(remindTemplate);
//		String result = restTemplate.postForObject(uri, entity, String.class);
//		if (ReturnConstants.SUCCESS.equals(result)) {
//			return new ModelAndView("redirect:/admin/dictionary/toRemindTemplate");
//		} else {
//			redirectAttributes.addFlashAttribute("error", result);
//			return new ModelAndView("redirect:/admin/dictionary/toRemindTemplate");
//		}
//	}

//	@RequestMapping(value = "/remindDelete/{id}", method = RequestMethod.GET)
//	public String remindDelete(@PathVariable ObjectId id, RedirectAttributes redirectAttributes) throws Exception {
//		String uri = ConfigParams.getBaseUrl() + "dictionary/remindDelete/" + id + "";
//		String result = restTemplate.getForObject(uri, String.class);
//		if (ReturnConstants.SUCCESS.equals(result)) {
//			return ("redirect:/admin/dictionary/toRemindTemplate");
//		}
//		Map<String, Object> model = new HashMap<String, Object>();
//		model.put("error", result);
//		redirectAttributes.addFlashAttribute("error", result);
//		return ("redirect:/admin/dictionary/toRemindTemplate");
//	}

	@RequestMapping(value = "toBranchAndSalers", method = RequestMethod.GET)
	public ModelAndView toBranchAndSalers() {
		String uri = ConfigParams.getBaseUrl() + "branchAndSalers/branchCompanyList/";
		BranchCompany[] branchCompanys = restTemplate.getForObject(uri, BranchCompany[].class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("branchCompanys", branchCompanys);
		return new ModelAndView("admin/dataDictionary/branchAndSalers", model);
	}

	@RequestMapping(value = "addSaler/{salerName}/{branchCompanyId}", method = RequestMethod.GET)
	@ResponseBody
	public Saler addSaler(@PathVariable String salerName, @PathVariable String branchCompanyId) {
		String uri = ConfigParams.getBaseUrl() + "branchAndSalers/addSaler/" + salerName + "/" + branchCompanyId + "/";
		Saler saler = restTemplate.getForObject(uri, Saler.class);
		return saler;
	}

	@RequestMapping(value = "showSalers/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Saler[] showSalers(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "branchAndSalers/showSalers/" + id + "/";
		Saler[] salers = restTemplate.getForObject(uri, Saler[].class);
		return salers;
	}

	@RequestMapping(value = "addBranchCompany/{branchCompanyName}", method = RequestMethod.GET)
	@ResponseBody
	public BranchCompany addBranchCompany(@PathVariable String branchCompanyName) {
		String uri = ConfigParams.getBaseUrl() + "branchAndSalers/addBranchCompany/" + branchCompanyName + "/";
		BranchCompany branchCompany = restTemplate.getForObject(uri, BranchCompany.class);
		return branchCompany;
	}

	@RequestMapping(value = "deleteSaler/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String deleteSaler(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "branchAndSalers/deleteSaler/" + id + "/";
		String result = restTemplate.getForObject(uri, String.class);
		return result;
	}

	@RequestMapping(value = "deleteBranch/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String deleteBranch(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "branchAndSalers/deleteBranch/" + id + "/";
		String result = restTemplate.getForObject(uri, String.class);
		return result;
	}

	@RequestMapping(value = "branchCompanyList", method = RequestMethod.GET)
	@ResponseBody
	public BranchCompany[] branchCompanyList() {
		String uri = ConfigParams.getBaseUrl() + "branchAndSalers/branchCompanyList/";
		BranchCompany[] branchCompanys = restTemplate.getForObject(uri, BranchCompany[].class);
		return branchCompanys;
	}
}
