package com.source3g.hermes.merchant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/merchant")
public class MainController {

	@RequestMapping(value = "main", method = RequestMethod.GET)
	public ModelAndView toMain() {
		return new ModelAndView("merchant/main");
	}
}
