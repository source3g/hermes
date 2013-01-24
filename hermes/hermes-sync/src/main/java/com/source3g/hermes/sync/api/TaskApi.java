package com.source3g.hermes.sync.api;

import javax.jms.Destination;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.source3g.hermes.constants.JmsConstants;
import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.constants.TaskConstants;
import com.source3g.hermes.entity.sync.DeviceStatus;
import com.source3g.hermes.service.JmsService;
import com.source3g.hermes.service.TaskService;

@Controller
@RequestMapping(value = "/task")
public class TaskApi {
	@Autowired
	private TaskService taskService;

	@Autowired
	private Destination syncDestination;
	@Autowired
	private JmsService jmsService;

	@RequestMapping(value = "/{sn}", method = RequestMethod.GET)
	@ResponseBody
	public Object taskList(@PathVariable String sn) {
		try {
			return taskService.genTasks(sn);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/{sn}/init", method = RequestMethod.GET)
	@ResponseBody
	public String taskInit(@PathVariable String sn) throws Exception {
		DeviceStatus deviceStatus = taskService.findStatus(sn);
		if (deviceStatus == null) {
			deviceStatus = new DeviceStatus();
			deviceStatus.setId(ObjectId.get());
			deviceStatus.setDeviceSn(sn);
		}
		deviceStatus.setStatus(TaskConstants.INIT);
		deviceStatus.setLastTaskId(null);
		taskService.saveDeviceStatus(deviceStatus);
		jmsService.sendString(syncDestination, sn, JmsConstants.TYPE, JmsConstants.PACKAGE_ALL);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/report/{sn}/{result}", method = RequestMethod.GET)
	@ResponseBody
	public String report(@PathVariable String sn, @PathVariable boolean result) {
		taskService.reportResult(sn, result);
		return ReturnConstants.SUCCESS;
	}

}
