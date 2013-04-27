package com.source3g.hermes.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping("/admin/message")
@RequiresRoles("admin")
public class MessageAdminController {

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/failed/list")
	public ModelAndView toFailedList(Merchant merchant, String pageNo, String startTime, String endTime, String status) {
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		String uri = ConfigParams.getBaseUrl() + "shortMessage/failedMessagelist/?pageNo=" + pageNo;
		if (StringUtils.isNotEmpty(startTime)) {
			uri += "&startTime=" + startTime;
		}
		if (StringUtils.isNotEmpty(endTime)) {
			uri += "&endTime=" + endTime;
		}
		if (StringUtils.isNotEmpty(status)) {
			uri += "&status=" + status;
		}
		Page page = restTemplate.getForObject(uri, Page.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("startTime", startTime);
		model.put("endTime", endTime);
		model.put("status", status);
		model.put("page", page);
		return new ModelAndView("/admin/system/failedMessageList", model);
	}

	@RequestMapping(value = "/failed/resend/{id}")
	public ModelAndView failedMessageSendAgain(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "shortMessage/failed/resend/" + id + "/";
		String result = restTemplate.getForObject(uri, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return new ModelAndView("redirect:/admin/message/failed/list");
		}
		return new ModelAndView("admin/error");
	}

	@RequestMapping(value="/failed/resendAll")
	public ModelAndView allFailedMessagesSendAgain( String startTime, String endTime, String status){
		String uri = ConfigParams.getBaseUrl() + "shortMessage/failed/resendAll/"+startTime+"/"+endTime+"/?a=1";
		if(status!=null){
			uri+="&status="+status;
		}
		String result=restTemplate.getForObject(uri, String.class);
		if(ReturnConstants.SUCCESS.equals(result)){
			return new ModelAndView("redirect:/admin/message/failed/list");
		}
		return new ModelAndView("admin/error");
	}
}
