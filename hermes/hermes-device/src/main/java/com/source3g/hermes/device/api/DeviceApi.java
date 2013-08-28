package com.source3g.hermes.device.api;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	@RequestMapping(value = "/add/api", method = RequestMethod.POST)
	@ResponseBody
	public String add(String sn) {
		logger.debug("add device....");
		try {
			Device device = new Device();
			device.setSn(sn);
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
	public Page list(String pageNo, String sn, String merchantAccount) {
		logger.debug("list device....");
		int pageNoInt = Integer.valueOf(pageNo);
		Device device = new Device();
		device.setSn(sn);
		return deviceService.list(pageNoInt, device, merchantAccount);
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
	public DeviceVo detail(@PathVariable String id) {
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

	@RequestMapping(value = "/export", method = RequestMethod.GET)
	@ResponseBody
	public String export(String sn, String merchantAccount) throws IOException {
		String url = deviceService.exportDevice(sn, merchantAccount);
		return url;
	}

	@RequestMapping(value = "/export/{year}/{month}/{day}/{fileName}")
	public void download(@PathVariable String year, @PathVariable String month, @PathVariable String day, @PathVariable String fileName,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		String downLoadPath = deviceService.getExportDir() + DeviceService.EXPORT_FOLDER_NAME + "/" + year + "/" + month + "/" + day + "/" + fileName;
		long fileLength = new File(downLoadPath).length();
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		response.setHeader("Content-Length", String.valueOf(fileLength));
		bis = new BufferedInputStream(new FileInputStream(downLoadPath));
		bos = new BufferedOutputStream(response.getOutputStream());
		byte[] buff = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		bis.close();
		bos.close();
	}

	@RequestMapping(value = "/publicKey")
	@ResponseBody
	public String getPublicKey() {
		PublicKey publicKey = deviceService.getPublicKey();
		return publicKey.getPublickey();
	}

	@RequestMapping(value = "/now")
	@ResponseBody
	public Date now() {
		return new Date();
	}

}
