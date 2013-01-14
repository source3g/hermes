package com.source3g.hermes.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.source3g.hermes.entity.dictionary.MerchantTagNode;
import com.source3g.hermes.utils.ConfigParams;

@Controller
@RequestMapping("/admin/merchantTagNode")
public class MerchantTagNodeController {
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value = "/merchantTagNodeList", method = RequestMethod.GET)
	@ResponseBody
	public MerchantTagNode[] merchantTagNodeList() {
		String uri = ConfigParams.getBaseUrl() + "/dictionary/merchant/tags";
		MerchantTagNode[] merchantTagNode = restTemplate.getForObject(uri, MerchantTagNode[].class);
		return merchantTagNode;
	}
}









