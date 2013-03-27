package com.source3g.hermes.dictionary.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.dictionary.service.BranchAndSalersService;
import com.sourse3g.hermes.branch.BranchCompany;
import com.sourse3g.hermes.branch.Saler;

@Controller
@RequestMapping("/branchAndSalers")
public class BranchAndSalersApi {

	@Autowired
	private BranchAndSalersService BranchAndSalersService;
	
	@RequestMapping(value = "/addSaler/{salerName}/{branchCompanyId}", method = RequestMethod.GET)
	@ResponseBody
	public Saler addSaler(@PathVariable String salerName,@PathVariable String branchCompanyId) {
		 return BranchAndSalersService.addSaler(salerName, branchCompanyId);
	}
	
	@RequestMapping(value = "/branchCompanyList", method = RequestMethod.GET)
	@ResponseBody
	public List<BranchCompany> showBranchCompany() {
		 return  BranchAndSalersService.showBranchCompany();
	}
	
	@RequestMapping(value = "/showSalers/{id}", method = RequestMethod.GET)
	@ResponseBody
	public List<Saler> showSalers(@PathVariable String id) {
		 return  BranchAndSalersService.showSalers(id);
	}
	
	@RequestMapping(value = "/addBranchCompany/{branchCompanyName}", method = RequestMethod.GET)
	@ResponseBody
	public BranchCompany addBranchCompany(@PathVariable String branchCompanyName) {
		return    BranchAndSalersService.addBranchCompany(branchCompanyName);
	}
	
	@RequestMapping(value = "/deleteSaler/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String deleteSaler(@PathVariable String id) {
		   BranchAndSalersService.deleteSaler(id);
		   return ReturnConstants.SUCCESS;
	}
	
	@RequestMapping(value = "/deleteBranch/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String deleteBranch(@PathVariable String id) {
		   BranchAndSalersService.deleteBranch(id);
		   return ReturnConstants.SUCCESS;
	}
}













