package com.source3g.hermes.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/admin/system")
public class SystemController {
	
	@RequestMapping(value="/monitor/failedJms")
	public ModelAndView findFailedJms(){
		return new ModelAndView("/admin/system/failedJmsList");
	}
	
	@RequestMapping(value="/operateLog")
	public ModelAndView findOperatelog(){
		return new ModelAndView("/admin/system/operateLogList");
	}
	
}
