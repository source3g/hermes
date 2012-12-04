package com.source3g.hermes.merchant.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.note.Note;
import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.LoginUtils;

@Controller
@RequestMapping(value = "/merchant/note")
public class NoteController {

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public ModelAndView toAdd() {
		return new ModelAndView("/merchant/note/add");
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	public ModelAndView add(HttpServletRequest req,Note note) throws Exception {
		Merchant merchant=LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "note/add/"+merchant.getId()+"/";
		HttpEntity<Note> entity = new HttpEntity<Note>(note);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return new ModelAndView("/merchant/note/add");
		} else {
			return new ModelAndView("error");
		}
	}
	
	//@RequestMapping(value = "list", method = RequestMethod.GET)
	//public ModelAndView list(HttpServletRequest req,Note note) throws Exception {
		
	//}

}
