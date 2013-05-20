package com.source3g.hermes.sync.jms.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.source3g.hermes.service.ElectricMenuTaskService;

@Component
public class PackageElectricMenuListener implements MessageListener {
	
	@Autowired
	private ElectricMenuTaskService electricMenuTaskService;
	
	@Override
	public void onMessage(Message message) {
		System.out.println("开始同步了");
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			try {
				String idStr=textMessage.getText();
				electricMenuTaskService.sync(new ObjectId(idStr));
			}catch(Exception e){
				
			}
		}
	}

}
