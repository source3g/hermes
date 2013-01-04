package com.source3g.hermes.dictionary.api;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.dictionary.service.DictionaryService;
import com.source3g.hermes.entity.merchant.RemindTemplate;

@Controller
@RequestMapping("/dictionary")
public class DictionaryApi {
	// private Logger logger = LoggerFactory.getLogger(DictionaryApi.class);

	@Autowired
	private DictionaryService dictionaryService;
	
	
	@RequestMapping(value = "/remind/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public RemindTemplate getRemind(@PathVariable String id) {
		return dictionaryService.getRemindTemplate(new ObjectId(id));
	}
	
	
	@RequestMapping(value = "/remindAdd/", method = RequestMethod.POST)
	@ResponseBody
	public String remindAdd(@RequestBody RemindTemplate remindTemplate) {
		if (remindTemplate.getId() == null) {
			remindTemplate.setId(ObjectId.get());
		}
		try {
			dictionaryService.add(remindTemplate);
			
		} catch (Exception e) {
		return	e.getMessage();
		}
		return ReturnConstants.SUCCESS;
	}
	
	@RequestMapping(value = "/titleValidate/{title}", method = RequestMethod.GET)
	@ResponseBody
	public Boolean titleValidate(@PathVariable String title) {
		return dictionaryService.titleValidate(title);
	}

	@RequestMapping(value = "/remindSetting", method = RequestMethod.GET)
	@ResponseBody
	public List<RemindTemplate> remindSetting() {
		return dictionaryService.remindList();
	}

	@RequestMapping(value = "/remindSave", method = RequestMethod.POST)
	@ResponseBody
	public String remindSave(@RequestBody RemindTemplate remindTemplate) {
		dictionaryService.remindSave(remindTemplate);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/remindDelete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String remindDelete(@PathVariable ObjectId id) {
		try {
			dictionaryService.remindDelete(id);
		} catch (Exception e) {
			return e.getMessage();
		}
		return ReturnConstants.SUCCESS;
	}
}
