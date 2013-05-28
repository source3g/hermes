package com.source3g.hermes.device.api;

import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
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
import com.source3g.hermes.device.service.DeviceService;
import com.source3g.hermes.dto.sync.DeviceStatusDto;
import com.source3g.hermes.entity.device.Device;
import com.source3g.hermes.entity.device.PublicKey;
import com.source3g.hermes.utils.GpsPoint;
import com.source3g.hermes.utils.Page;
import com.source3g.hermes.vo.DeviceDistributionVo;
import com.source3g.hermes.vo.DeviceVo;

@Controller
@RequestMapping("/device")
public class DeviceApi {
	private Logger logger = LoggerFactory.getLogger(DeviceApi.class);

	@Autowired
	private DeviceService deviceService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody Device device) {
		logger.debug("add device....");
		try {
			deviceService.add(device);
		} catch (Exception e) {
			return e.getMessage();
		}
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/snValidate/{sn}", method = RequestMethod.GET)
	@ResponseBody
	public boolean snValidate(@PathVariable String sn) {
		return deviceService.snValidate(sn);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Page list(String pageNo, String sn) {
		logger.debug("list device....");
		int pageNoInt = Integer.valueOf(pageNo);
		Device device = new Device();
		device.setSn(sn);
		return deviceService.list(pageNoInt, device);
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable String id) throws Exception {
		logger.debug("delete merchant....");
		deviceService.deleteById(id);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/{ids}", method = RequestMethod.GET)
	@ResponseBody
	public List<Device> getDeviceInfo(@PathVariable String ids) {
		String idArray[] = ids.split(",");
		return deviceService.findByIds(Arrays.asList(idArray));
	}

	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public DeviceVo deviceDetail(@PathVariable String id) {
		return deviceService.findDetail(new ObjectId(id));
	}

	@RequestMapping(value = "/sn/{sn}", method = RequestMethod.GET)
	@ResponseBody
	public Device getDeviceInfoBySn(@PathVariable String sn) {
		return deviceService.findBySn(sn);
	}

	@RequestMapping(value = "/simId/{simId}", method = RequestMethod.GET)
	@ResponseBody
	public Device getDeviceInfoBySimId(@PathVariable ObjectId simId) {
		return deviceService.findBySimId(simId);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@RequestBody Device device) {
		logger.debug("update device....");
		deviceService.update(device);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/gps/report/{sn}/{x}/{y}", method = RequestMethod.GET)
	@ResponseBody
	public String gpsReport(@PathVariable String sn, @PathVariable Double x, @PathVariable Double y) {
		GpsPoint gpsPoint = new GpsPoint(x, y);
		deviceService.updateGpsPoint(sn, gpsPoint);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/imsi/report/{sn}/{imsiNo}", method = RequestMethod.GET)
	@ResponseBody
	public String imsiReport(@PathVariable String sn, @PathVariable String imsiNo) {
		deviceService.updateImsiNo(sn, imsiNo);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "deviceDistribution")
	@ResponseBody
	public List<DeviceDistributionVo> findDeviceDistribution() {
		return deviceService.findDeviceDistribution();
	}

	@RequestMapping(value = "/sync/status/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public List<DeviceStatusDto> syncStatus(@PathVariable String merchantId) {
		return deviceService.findDeviceStatusByMerchantId(new ObjectId(merchantId));
	}

	@RequestMapping(value = "/publicKey")
	@ResponseBody
	public String getPublicKey() {
		PublicKey publicKey = deviceService.getPublicKey();
		return publicKey.getPublickey();
	}

}
