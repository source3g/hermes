package com.source3g.hermes.merchant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/merchant/message")
public class MessageController {

	@RequestMapping(value = "/template", method = RequestMethod.GET)
	public ModelAndView template() {

		return new ModelAndView("/merchant/shortMessage/messageTemplate");
	}

}
