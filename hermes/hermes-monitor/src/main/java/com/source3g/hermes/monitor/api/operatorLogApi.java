package com.source3g.hermes.monitor.api;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.source3g.hermes.monitor.service.operatorLogService;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping("/operatorLog")
public class operatorLogApi {

	@Autowired
	private operatorLogService operateLogService;
	
	@RequestMapping(value="toLogList")
	@ResponseBody
	public Page findAll(String pageNo,String startTime,String endTime) throws ParseException {
		return operateLogService.findAll(pageNo,startTime,endTime);
	}
}
