package com.source3g.hermes.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.Sim;
import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping("/admin/sim")
public class SimController {
	private static final Logger logger = LoggerFactory
			.getLogger(MerchantController.class);
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public ModelAndView toAdd() {
		return new ModelAndView("admin/sim/add");
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView add(@Valid Sim sim, BindingResult errorResult) {
		if (errorResult.hasErrors()) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("errors", errorResult.getAllErrors());
			return new ModelAndView("admin/sim/add", model);
		}
		String uri = ConfigParams.getBaseUrl() + "sim/add";
		HttpEntity<Sim> entity = new HttpEntity<Sim>(sim);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS_WIDTH_QUOT.equals(result)) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put(ReturnConstants.SUCCESS, ReturnConstants.SUCCESS);
			return new ModelAndView("admin/sim/add", model);
		} else {
			return new ModelAndView("admin/error");
		}

	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(Sim sim, String pageNo) {
		logger.debug("list.......");
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		String uri = ConfigParams.getBaseUrl() + "sim/list/?pageNo=" + pageNo;
		if (StringUtils.isNotEmpty(sim.getNo())) {
			uri += "&no=" + sim.getNo();
		}
		Page page = restTemplate.getForObject(uri, Page.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("page", page);
		return new ModelAndView("admin/sim/list", model);
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ModelAndView deleteById(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "sim/delete/" + id + "/";
		restTemplate.getForObject(uri, String.class);
		return new ModelAndView("redirect:/admin/sim/list/");
	}

}
