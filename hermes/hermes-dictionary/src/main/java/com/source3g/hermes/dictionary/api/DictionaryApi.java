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
@RequestMapping("/Dictionary")
public class DictionaryApi {
	//private Logger logger = LoggerFactory.getLogger(DictionaryApi.class);

	@Autowired
	private DictionaryService dictionaryService;
	
	
	@RequestMapping(value = "/remindAdd", method = RequestMethod.POST)
	@ResponseBody
	public String  remindAdd(@RequestBody RemindTemplate remindTemplate) {
		if(remindTemplate.getId()!=null){
			ObjectId objId=null;
			remindTemplate.setId(objId);
		}
		dictionaryService.add( remindTemplate);
		return ReturnConstants.SUCCESS;
}
	@RequestMapping(value = "/remindSetting", method = RequestMethod.GET)
	@ResponseBody
	public List<RemindTemplate>  remindSetting() {
		return dictionaryService.remindList();
	}
	@RequestMapping(value = "/remindSave", method = RequestMethod.POST)
	@ResponseBody
	public String remindSave(@RequestBody RemindTemplate remindTemplate) {
		dictionaryService.remindSave( remindTemplate);
		return ReturnConstants.SUCCESS;
	}
	@RequestMapping(value = "/remindDelete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String remindDelete(@PathVariable ObjectId id) {
		try {
			dictionaryService.remindDelete( id);
		} catch (Exception e) {
			return	e.getMessage();
		}
		return ReturnConstants.SUCCESS;
	}
}
