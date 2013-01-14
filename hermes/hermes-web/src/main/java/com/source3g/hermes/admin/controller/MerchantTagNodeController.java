package com.source3g.hermes.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.dto.dictionary.MerchantTagDto;
import com.source3g.hermes.entity.dictionary.MerchantTagNode;
import com.source3g.hermes.utils.ConfigParams;

@Controller
@RequestMapping("/admin/dictionary/tag")
public class MerchantTagNodeController {
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public MerchantTagNode[] merchantTagNodeList() {
		String uri = ConfigParams.getBaseUrl() + "/dictionary/merchant/tags";
		MerchantTagNode[] merchantTagNode = restTemplate.getForObject(uri, MerchantTagNode[].class);
		return merchantTagNode;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView add(MerchantTagDto tagDto) {
		String uri = ConfigParams.getBaseUrl() + "/dictionary/merchant/tags";
		HttpEntity<MerchantTagDto> entity = new HttpEntity<MerchantTagDto>(tagDto);
		String result = restTemplate.postForObject(uri, entity, String.class);
		System.out.println(result);
		return new ModelAndView("redirect:/admin/dictionary/tag/toTagSetting/");
	}

	@RequestMapping(value = "/toTagSetting", method = RequestMethod.GET)
	public ModelAndView toMerchantTag() {
		String uri = ConfigParams.getBaseUrl() + "/dictionary/merchant/tags";
		MerchantTagNode[] merchantTagNodes = restTemplate.getForObject(uri, MerchantTagNode[].class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("nodes", merchantTagNodes);
		return new ModelAndView("/admin/dataDictionary/merchantTag", model);
	}

}