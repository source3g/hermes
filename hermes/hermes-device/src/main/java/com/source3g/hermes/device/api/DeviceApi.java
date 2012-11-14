package com.source3g.hermes.device.api;

import java.util.Arrays;
import java.util.List;

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
import com.source3g.hermes.entity.Device;
import com.source3g.hermes.utils.Page;



@Controller
@RequestMapping("/device")
public class DeviceApi 
{
	private Logger logger=LoggerFactory.getLogger(DeviceApi.class);
	
	@Autowired
	private DeviceService deviceService;
	
	@RequestMapping(value="/add" , method=RequestMethod.POST)
	@ResponseBody
	public String add( @RequestBody Device device){
		logger.debug("add device....");
		deviceService.add(device);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value="/list" , method=RequestMethod.GET)
	@ResponseBody
	public Page list(String pageNo,String sn){
		logger.debug("list device....");
		int pageNoInt=Integer.valueOf(pageNo);
		Device device=new Device();
		device.setSn(sn);
		return deviceService.list(pageNoInt,device);
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable String id){
		logger.debug("delete merchant....");
		 deviceService.deleteById(id);
		 return ReturnConstants.SUCCESS;
	}
	@RequestMapping(value = "/{ids}", method = RequestMethod.GET)
	@ResponseBody
	public List<Device> getDeviceInfo(@PathVariable String ids) {
		String idArray[]=ids.split(",");
		
		return deviceService.findByIds(Arrays.asList(idArray));
	}
	@RequestMapping(value="/sn/{sn}" , method=RequestMethod.GET)
	@ResponseBody
	public Device getDeviceInfoBySn(@PathVariable String sn) {
		return deviceService.findBySn(sn);
	}
}
	