package com.source3g.hermes.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.vo.DeviceDistributionVo;

@Controller
@RequestMapping(value = "/device")
public class DistributionMapController {

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/distributionInfo", method = RequestMethod.GET)
	@ResponseBody
	public DeviceDistributionVo[] distributionInfo() {
		String url = ConfigParams.getBaseUrl() + "device/deviceDistribution/";
		DeviceDistributionVo[] result = restTemplate.getForObject(url, DeviceDistributionVo[].class);
		return result;
	}

	@RequestMapping(value = "/distributionMap", method = RequestMethod.GET)
	public ModelAndView distributionMap() {
		return new ModelAndView("admin/system/distributionMap");
	}

}
