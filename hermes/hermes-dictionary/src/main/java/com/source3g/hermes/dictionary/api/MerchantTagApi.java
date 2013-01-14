package com.source3g.hermes.dictionary.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.dictionary.service.MerchantTagService;
import com.source3g.hermes.entity.dictionary.MerchantTagNode;

@Controller
@RequestMapping("/dictionary")
public class MerchantTagApi {

	@Autowired
	private MerchantTagService merchantTagService;

	@RequestMapping(value = "/merchant/tags", method = RequestMethod.GET)
	@ResponseBody
	public List<MerchantTagNode> listAll() {
		return merchantTagService.findAllNodes();
	}
	
	@RequestMapping(value = "/merchant/tags", method = RequestMethod.POST)
	@ResponseBody
	public String add(List<MerchantTagNode> nodes) {
		 merchantTagService.save(nodes);
		 return ReturnConstants.SUCCESS;
	}

		
}
