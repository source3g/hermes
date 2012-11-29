package com.source3g.hermes.sync.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.service.TaskService;
import com.source3g.hermes.sync.entity.TaskPackage;

@Controller
@RequestMapping(value = "/task")
public class TaskController {
	@Autowired
	private TaskService taskService;

	@RequestMapping(value = "/{sn}", method = RequestMethod.GET)
	@ResponseBody
	public List<TaskPackage> taskList(@PathVariable String sn) {
		return taskService.genTasks(sn);
	}

	@RequestMapping(value = "/report/{sn}/{result}", method = RequestMethod.GET)
	@ResponseBody
	public String report(@PathVariable String sn, @PathVariable boolean result) {
		taskService.reportResult(sn, result);
		return ReturnConstants.SUCCESS;
	}

}
