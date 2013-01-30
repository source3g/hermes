package com.source3g.hermes.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.Device;
import com.source3g.hermes.entity.Sim;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping("/admin/device")
public class DeviceController {
	private static final Logger logger = LoggerFactory.getLogger(MerchantController.class);

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public ModelAndView toAdd() {
		return new ModelAndView("admin/device/add");
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView add(@Valid Device device, BindingResult errorResult) {
		Map<String, Object> model = new HashMap<String, Object>();
		if (errorResult.hasErrors()) {
			model.put("errors", errorResult.getAllErrors());
			return new ModelAndView("admin/device/add", model);
		}
		String uri = ConfigParams.getBaseUrl() + "device/add";
		HttpEntity<Device> entity = new HttpEntity<Device>(device);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			model.put(ReturnConstants.SUCCESS, ReturnConstants.SUCCESS);
			return new ModelAndView("admin/device/add", model);
		} else {
			model.put("error", result);
			return new ModelAndView("admin/device/add",model);
		}
	}
	//验证盒子名称是否存在
	@RequestMapping(value = "snValidate", method = RequestMethod.GET)
	@ResponseBody
	public Boolean snValidate(String sn) {
		String uri=ConfigParams.getBaseUrl() + "device/snValidate/"+sn+"/";
		Boolean result = restTemplate.getForObject(uri, Boolean.class);
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(Device device, String pageNo) {
		logger.debug("list.......");
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		String uri = ConfigParams.getBaseUrl() + "device/list/?pageNo=" + pageNo;
		if (StringUtils.isNotEmpty(device.getSn())) {
			uri += "&sn=" + device.getSn();
		}
		Page page = restTemplate.getForObject(uri, Page.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("page", page);
		return new ModelAndView("admin/device/list", model);
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ModelAndView deleteById(@PathVariable String id,RedirectAttributes redirectAttributes ) {
		String uri = ConfigParams.getBaseUrl() + "device/delete/" + id + "/";
		String result=restTemplate.getForObject(uri, String.class);
		if(ReturnConstants.SUCCESS.equals(result)){
			return new ModelAndView("redirect:/admin/device/list/");
		}
		redirectAttributes.addFlashAttribute("error", result);
		return new ModelAndView("redirect:/admin/device/list/");
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/sn/{sn}", method = RequestMethod.GET)
	@ResponseBody
	public Object findBySn(@PathVariable String sn) {
		String uri = ConfigParams.getBaseUrl() + "device/sn/" + sn;
		Device device = restTemplate.getForObject(uri, Device.class);
		if (device == null) {
			return "   盒子名称输入有误";
		}
		String uriMerchant = ConfigParams.getBaseUrl() + "merchant/findByDeviceIds/" + device.getId() + "/";
		List<Merchant> merchant = restTemplate.getForObject(uriMerchant, List.class);
		if (merchant != null && merchant.size() > 0) {

			return "   该盒子已被绑定";
		}
		return device;
	}

	@RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
	public ModelAndView findById(@PathVariable String id) {
		Map<String, Object> model = new HashMap<String, Object>();
		String uri = ConfigParams.getBaseUrl() + "device/" + id + "/";
		Device[] devices = restTemplate.getForObject(uri, Device[].class);
		if (devices == null || devices.length != 1) {
			return new ModelAndView(("/admin/error"));
		}
		if (devices[0].getSim() != null) {
			String uriSim = ConfigParams.getBaseUrl() + "sim/id/" + devices[0].getSim().getId() + "/";
			Sim sim = restTemplate.getForObject(uriSim, Sim.class);
			model.put("sim", sim);
		}
		model.put("device", devices[0]);
		return new ModelAndView(("/admin/device/deviceInfo"), model);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(@Valid Device device, String no, BindingResult errorResult) {
		Map<String, Object> model = new HashMap<String, Object>();
		if (errorResult.hasErrors()) {
			model.put("errors", errorResult.getAllErrors());
			return new ModelAndView("admin/device/deviceInfo", model);
		}
		String uriSim = ConfigParams.getBaseUrl() + "sim/no/" + no + "/";
		Sim sim = restTemplate.getForObject(uriSim, Sim.class);
		if (sim == null) {
			errorResult.addError(new ObjectError("simIsNull", "SIM卡号不存在"));
			model.put("errors", errorResult.getAllErrors());
			return new ModelAndView("/admin/device/deviceInfo", model);
		}
		String uriDevice = ConfigParams.getBaseUrl() + "device/simId/" + sim.getId() + "/";
		Device findDevice = restTemplate.getForObject(uriDevice, Device.class);
		if (findDevice != null) {
			errorResult.addError(new ObjectError("findDevice", "SIM卡已被使用"));
			model.put("errors", errorResult.getAllErrors());
			return new ModelAndView("/admin/device/deviceInfo", model);
		}
		device.setSim(sim);
		String uri = ConfigParams.getBaseUrl() + "device/update/";
		HttpEntity<Device> entity = new HttpEntity<Device>(device);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			model.put("success", "success");
			return new ModelAndView("/admin/device/deviceInfo", model);
		} else {
			return new ModelAndView("admin/error");
		}

	}
}
