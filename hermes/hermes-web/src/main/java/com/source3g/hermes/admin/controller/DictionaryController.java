package com.source3g.hermes.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.merchant.RemindTemplate;
import com.source3g.hermes.utils.ConfigParams;

@Controller
@RequestMapping("/admin/Dictionary")
public class DictionaryController {
	//sprivate static final Logger logger = LoggerFactory.getLogger(DictionaryController.class);
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value = "toRemindTemplate", method = RequestMethod.GET)
	public ModelAndView toRemindTemplate() {
		String uri=ConfigParams.getBaseUrl() + "Dictionary/remindSetting/";
		RemindTemplate[] remindTemplate = restTemplate.getForObject(uri, RemindTemplate[].class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("remindTemplate", remindTemplate);
		return new ModelAndView("admin/dataDictionary/remindTemplate",model);
	}
	
	@RequestMapping(value = "/remindAdd",method=RequestMethod.POST)
	public ModelAndView remindAdd(RemindTemplate remindTemplate,HttpServletRequest req) throws Exception {
		String uri=ConfigParams.getBaseUrl() + "Dictionary/remindAdd/";
		HttpEntity<RemindTemplate> entity = new HttpEntity<RemindTemplate>(remindTemplate);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {	
		return new ModelAndView("redirect:/admin/Dictionary/toRemindTemplate");
		}
		return new ModelAndView("admin/error");
		
	}
	@RequestMapping(value = "/remindSave",method=RequestMethod.POST)
	public ModelAndView remindSave(RemindTemplate remindTemplate) throws Exception {
		String uri=ConfigParams.getBaseUrl() + "Dictionary/remindSave/";
		HttpEntity<RemindTemplate> entity = new HttpEntity<RemindTemplate>(remindTemplate);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {	
		return new ModelAndView("redirect:/admin/Dictionary/toRemindTemplate");
		}
		return new ModelAndView("admin/error");
		
	}
	@RequestMapping(value = "/remindDelete/{id}",method=RequestMethod.GET)
	public String remindDelete(@PathVariable ObjectId id,RedirectAttributes redirectAttributes) throws Exception {
		String uri=ConfigParams.getBaseUrl() + "Dictionary/remindDelete/"+id+"";
		String result = restTemplate.getForObject(uri, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {	
		return ("redirect:/admin/Dictionary/toRemindTemplate");
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("error", result);
		redirectAttributes.addFlashAttribute("error", result);
		return ("redirect:/admin/Dictionary/toRemindTemplate");
	}
}
