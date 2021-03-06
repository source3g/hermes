package com.source3g.hermes.dictionary.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.source3g.hermes.dictionary.service.DictionaryService;

@Controller
@RequestMapping("/dictionary")
public class DictionaryApi {
	// private Logger logger = LoggerFactory.getLogger(DictionaryApi.class);

	@Autowired
	private DictionaryService dictionaryService;
	
	
//	@RequestMapping(value = "/remind/get/{id}", method = RequestMethod.GET)
//	@ResponseBody
//	public RemindTemplate getRemind(@PathVariable String id) {
//		return dictionaryService.getRemindTemplate(new ObjectId(id));
//	}
//	
//	
//	@RequestMapping(value = "/remindAdd", method = RequestMethod.POST)
//	@ResponseBody
//	public String remindAdd(@RequestBody RemindTemplate remindTemplate) {
//		if (remindTemplate.getId() == null) {
//			remindTemplate.setId(ObjectId.get());
//		}
//		try {
//			dictionaryService.add(remindTemplate);
//			
//		} catch (Exception e) {
//		return	e.getMessage();
//		}
//		return ReturnConstants.SUCCESS;
//	}
//	
//	@RequestMapping(value = "/titleValidate/{title}", method = RequestMethod.GET)
//	@ResponseBody
//	public Boolean titleValidate(@PathVariable String title) {
//		return dictionaryService.titleValidate(title);
//	}
//
//	@RequestMapping(value = "/remindSetting", method = RequestMethod.GET)
//	@ResponseBody
//	public List<RemindTemplate> remindSetting() {
//		return dictionaryService.remindList();
//	}
//
//	@RequestMapping(value = "/remindSave", method = RequestMethod.POST)
//	@ResponseBody
//	public String remindSave(@RequestBody RemindTemplate remindTemplate) {
//		try {
//			dictionaryService.remindSave(remindTemplate);
//		} catch (Exception e) {
//			return e.getMessage();
//		}
//		return ReturnConstants.SUCCESS;
//	}
//
//	@RequestMapping(value = "/remindDelete/{id}", method = RequestMethod.GET)
//	@ResponseBody
//	public String remindDelete(@PathVariable ObjectId id) {
//		try {
//			dictionaryService.remindDelete(id);
//		} catch (Exception e) {
//			return e.getMessage();
//		}
//		return ReturnConstants.SUCCESS;
//	}
}
