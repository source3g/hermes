package com.source3g.hermes.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.utils.ConfigParams;
import com.sourse3g.hermes.branch.BranchCompany;
import com.sourse3g.hermes.branch.Saler;

@Controller
@RequestMapping("/admin/BranchAndSalers")
public class BranchAndSalersController {
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value = "toBranchAndSalers", method = RequestMethod.GET)
	public ModelAndView toBranchAndSalers() {
		String uri = ConfigParams.getBaseUrl() + "branchAndSalers/branchCompanyList/";
		BranchCompany[] BranchCompanys=restTemplate.getForObject(uri, BranchCompany[].class);
		Map<String, Object> model=new HashMap<String, Object>();
		model.put("BranchCompanys", BranchCompanys);
		return new ModelAndView("admin/dataDictionary/branchAndSalers",model);
	}
	
	@RequestMapping(value = "addSaler/{salerName}/{branchCompanyId}", method = RequestMethod.GET)
	@ResponseBody
	public Saler addSaler(@PathVariable String salerName,@PathVariable String branchCompanyId) {
		String uri = ConfigParams.getBaseUrl() + "branchAndSalers/addSaler/" + salerName +"/"+branchCompanyId+"/";
		Saler saler=restTemplate.getForObject(uri, Saler.class);
		return saler;
	}
	
	@RequestMapping(value = "showSalers/{id}", method = RequestMethod.GET)
	@ResponseBody
	public  Saler[] showSalers(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "branchAndSalers/showSalers/" + id +"/";
		Saler[] salers=restTemplate.getForObject(uri, Saler[].class);
			return salers;
	}
	
	@RequestMapping(value = "addBranchCompany/{branchCompanyName}", method = RequestMethod.GET)
	public  ModelAndView addBranchCompany(@PathVariable String branchCompanyName) {
		String uri = ConfigParams.getBaseUrl() + "branchAndSalers/addBranchCompany/" +branchCompanyName +"/";
		String result=restTemplate.getForObject(uri, String.class);
		if(ReturnConstants.SUCCESS.equals(result)){
			return new ModelAndView ("redirect:/admin/BranchAndSalers/toBranchAndSalers");
		}
		return new ModelAndView("admin/error");
	}
	
	@RequestMapping(value = "deleteSaler/{id}", method = RequestMethod.GET)
	@ResponseBody
	public  String deleteSaler(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "branchAndSalers/deleteSaler/" +id +"/";
		String result=restTemplate.getForObject(uri, String.class);
		return result;
	}
	
	@RequestMapping(value = "deleteBranch/{id}", method = RequestMethod.GET)
	@ResponseBody
	public  String deleteBranch(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "branchAndSalers/deleteBranch/" +id +"/";
		String result=restTemplate.getForObject(uri, String.class);
		return result;
	}
	
	
	
	
	
	
	
	
}






