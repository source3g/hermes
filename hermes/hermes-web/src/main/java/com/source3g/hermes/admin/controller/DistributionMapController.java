package com.source3g.hermes.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/device")
public class DistributionMapController {

	@RequestMapping(value = "/distributionMap", method = RequestMethod.GET)
	public ModelAndView distributionMap() {
		return new ModelAndView("admin/system/distributionMap");
	}
}
