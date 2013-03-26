package com.source3g.hermes.monitor.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.log.ErrorLog;

@Controller
@RequestMapping(value = "/errorLog")
public class ErrorLogApi {

	@RequestMapping(value = "/report", method = RequestMethod.POST)
	public String report(ErrorLog errorLog) {
		System.out.println(errorLog);
		return ReturnConstants.SUCCESS;
	}
}
