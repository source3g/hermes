package com.source3g.hermes.monitor.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.source3g.hermes.entity.log.FailedMessage;

@Controller
@RequestMapping(value="/monitor")
public class FailedMessageApi {
	
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@RequestMapping(value="failedMessage",method=RequestMethod.GET)
	@ResponseBody
	public List<FailedMessage> findFailedMessage(){
		List<FailedMessage> failedMessage= mongoTemplate.findAll(FailedMessage.class);
		return failedMessage;
	}
	

}
