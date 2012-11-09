package com.source3g.hermes.merchant.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.merchant.MerchantGroup;
import com.source3g.hermes.merchant.service.MerchantGroupService;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping("/merchantGroup")
public class MerchantGroupApi {

	private Logger logger = LoggerFactory.getLogger(MerchantGroupApi.class);

	@Autowired
	private MerchantGroupService merchantGroupService;

	
	@RequestMapping(value="/list",method = RequestMethod.GET)
	@ResponseBody
	public Page list(String pageNo,String name) {//MerchantGroup merchantGroup,String pageNo
		logger.debug("merchantGroup list...");
		int pageNoInt=Integer.valueOf(pageNo);
		MerchantGroup merchantGroup=new MerchantGroup();
		merchantGroup.setName(name);
		return merchantGroupService.list(pageNoInt,merchantGroup);
	}
	
	
	@RequestMapping(value="/listAll",method = RequestMethod.GET)
	@ResponseBody
	public List<MerchantGroup> listAll(String name) {
		MerchantGroup merchantGroup=new MerchantGroup();
		merchantGroup.setName(name);
		return merchantGroupService.list(merchantGroup);
	}

	@RequestMapping(value="/add",method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody MerchantGroup merchantGroup) {
		merchantGroupService.add(merchantGroup);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable String id) {
		logger.debug("delete merchantGroup....");
		merchantGroupService.deleteById(id);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@RequestBody MerchantGroup merchantGroup) {
		merchantGroupService.update(merchantGroup);
		return ReturnConstants.SUCCESS;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public MerchantGroup getMerchantGroupInfo(@PathVariable String id) {
		return merchantGroupService.get(id);
	}

}
