package com.source3g.hermes.sim.api;

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
import com.source3g.hermes.device.api.DeviceApi;
import com.source3g.hermes.entity.Sim;
import com.source3g.hermes.sim.service.SimService;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping("/sim")
public class SimApi {

	private Logger logger = LoggerFactory.getLogger(DeviceApi.class);

	@Autowired
	private SimService simService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody Sim sim) {
		logger.debug("add device....");
		simService.add(sim);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Page list(String pageNo, String no) {
		logger.debug("list device....");
		int pageNoInt = Integer.valueOf(pageNo);
		Sim sim = new Sim();
		sim.setNo(no);
		return simService.list(pageNoInt, sim);
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable String id) {
		logger.debug("delete merchant....");
		simService.deleteById(id);
		return ReturnConstants.SUCCESS;
	}

}