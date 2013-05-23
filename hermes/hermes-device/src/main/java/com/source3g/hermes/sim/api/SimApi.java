package com.source3g.hermes.sim.api;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.device.api.DeviceApi;
import com.source3g.hermes.entity.sim.SimInfo;
import com.source3g.hermes.sim.service.SimService;
import com.source3g.hermes.utils.FormateUtils;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping("/sim")
public class SimApi {

	private Logger logger = LoggerFactory.getLogger(DeviceApi.class);

	@Autowired
	private SimService simService;

	@RequestMapping(value = "/import", method = RequestMethod.POST)
	@ResponseBody
	public String importSim(MultipartFile file, String oldName) throws Exception {
		logger.debug("导入Sim卡信息");
		String filePath = simService.getImportDir() + FormateUtils.getDirByDay() + file.getOriginalFilename();
		File excelFile = new File(filePath);
		FileUtils.copyInputStreamToFile(file.getInputStream(), excelFile);
		simService.importFromExcel(excelFile);
		//
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/simValidate/{no}", method = RequestMethod.GET)
	@ResponseBody
	public boolean simValidate(@PathVariable String no) {
		return simService.simValidate(no);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Page list(String pageNo, String serviceNo,String imsiNo) {
		logger.debug("list device....");
		int pageNoInt = Integer.valueOf(pageNo);
		return simService.list(pageNoInt, serviceNo, imsiNo);
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable String id) {
		logger.debug("delete sim....");
		simService.deleteById(id);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/no/{no}", method = RequestMethod.GET)
	@ResponseBody
	public SimInfo findByNo(@PathVariable String no) {
		logger.debug("find sim..");
		return simService.findByNo(no);
	}

	@RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
	@ResponseBody
	public SimInfo findById(@PathVariable ObjectId id) {
		logger.debug("find sim..");
		return simService.findById(id);
	}

}
