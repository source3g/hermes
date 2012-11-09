package com.source3g.hermes.merchant.api;

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
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.merchant.service.MerchantService;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping("/merchant")
public class MerchantApi {
	
	
	
	private Logger logger=LoggerFactory.getLogger(MerchantApi.class);
	
	@Autowired
	private MerchantService merchantService;

	@RequestMapping(value="/add" , method=RequestMethod.POST)
	@ResponseBody
	public String add( @RequestBody Merchant merchant){
		logger.debug("add merchant....");
		merchantService.add(merchant);
		return ReturnConstants.SUCCESS;
	}
	@RequestMapping(value="/{id}" , method=RequestMethod.GET)
	@ResponseBody
	public Merchant getMerchant(@PathVariable String id){
		
		return merchantService.getMerchant(id);
	}
	@RequestMapping(value="/list",method=RequestMethod.GET)
	@ResponseBody

	public Page list(String pageNo,String name){
		logger.debug("list merchant....");
		int pageNoInt=Integer.valueOf(pageNo);
		Merchant merchant=new Merchant();
		merchant.setName(name);
		return merchantService.list(pageNoInt,merchant);
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable String id){
		logger.debug("delete merchant....");
		 merchantService.deleteById(id);
		 return ReturnConstants.SUCCESS;
	}
	
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public String update(@RequestBody Merchant merchant){
		logger.debug("update merchant....");
		merchantService.update(merchant);
		return ReturnConstants.SUCCESS;
	}
}
