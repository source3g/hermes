package com.source3g.hermes.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.merchant.RemindTemplate;
import com.source3g.hermes.utils.ConfigParams;

@Controller
@RequestMapping("/admin/dictionary")
@RequiresRoles("admin")
public class DictionaryController {
	// sprivate static final Logger logger =
	// LoggerFactory.getLogger(DictionaryController.class);
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "remind/{id}", method = RequestMethod.GET)
	@ResponseBody
	public RemindTemplate getRemind(@PathVariable String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		String uri = ConfigParams.getBaseUrl() + "dictionary/remind/get/" + id + "/";
		RemindTemplate remindTemplate = restTemplate.getForObject(uri, RemindTemplate.class);
		return remindTemplate;
	}

	@RequestMapping(value = "toRemindTemplate", method = RequestMethod.GET)
	public ModelAndView toRemindTemplate() {
		String uri = ConfigParams.getBaseUrl() + "dictionary/remindSetting/";
		RemindTemplate[] remindTemplate = restTemplate.getForObject(uri, RemindTemplate[].class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("remindTemplate", remindTemplate);
		return new ModelAndView("admin/dataDictionary/remindTemplate", model);
	}

	@RequestMapping(value = "/remindAdd", method = RequestMethod.POST)
	public ModelAndView remindAdd(@Valid RemindTemplate remindTemplate, BindingResult bindingResult,RedirectAttributes redirectAttributes) throws Exception {
		if (bindingResult.hasErrors()) {
			// TODO 要改
			return new ModelAndView("error");
		}
		String uri = ConfigParams.getBaseUrl() + "dictionary/remindAdd/";
		HttpEntity<RemindTemplate> entity = new HttpEntity<RemindTemplate>(remindTemplate);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return new ModelAndView("redirect:/admin/dictionary/toRemindTemplate");
		} else {
			redirectAttributes.addFlashAttribute("error", result);
			return new ModelAndView("redirect:/admin/dictionary/toRemindTemplate");
		}
	}

	// 验证标题是否重复
	@RequestMapping(value = "/titleValidate", method = RequestMethod.GET)
	@ResponseBody
	public Boolean titleValidate(String title) throws Exception {
		String uri = ConfigParams.getBaseUrl() + "dictionary/titleValidate/" + title + "/";
		Boolean result = restTemplate.getForObject(uri, Boolean.class);
		return result;
	}

	@RequestMapping(value = "/remindSave", method = RequestMethod.POST)
	public ModelAndView remindSave(RemindTemplate remindTemplate, RedirectAttributes redirectAttributes) throws Exception {
		String uri = ConfigParams.getBaseUrl() + "dictionary/remindSave/";
		HttpEntity<RemindTemplate> entity = new HttpEntity<RemindTemplate>(remindTemplate);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return new ModelAndView("redirect:/admin/dictionary/toRemindTemplate");
		} else {
			redirectAttributes.addFlashAttribute("error", result);
			return new ModelAndView("redirect:/admin/dictionary/toRemindTemplate");
		}
	}

	@RequestMapping(value = "/remindDelete/{id}", method = RequestMethod.GET)
	public String remindDelete(@PathVariable ObjectId id, RedirectAttributes redirectAttributes) throws Exception {
		String uri = ConfigParams.getBaseUrl() + "dictionary/remindDelete/" + id + "";
		String result = restTemplate.getForObject(uri, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return ("redirect:/admin/dictionary/toRemindTemplate");
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("error", result);
		redirectAttributes.addFlashAttribute("error", result);
		return ("redirect:/admin/dictionary/toRemindTemplate");
	}
	
}
