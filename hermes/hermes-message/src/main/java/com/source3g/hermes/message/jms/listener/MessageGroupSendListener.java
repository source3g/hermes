package com.source3g.hermes.message.jms.listener;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.source3g.hermes.constants.JmsConstants;
import com.source3g.hermes.enums.MessageType;
import com.source3g.hermes.message.ShortMessageRecord;
import com.source3g.hermes.service.JmsService;
@Component
public class MessageGroupSendListener implements MessageListener{

	@Autowired
	private JmsService jmsService;
	@Autowired
	private Destination messageDestination;
	
	@Override
	public void onMessage(Message message) {
		ShortMessageRecord m = new ShortMessageRecord();
		m.setContent("123");
		m.setPhone("456");
		m.setMessageType(MessageType.挂机短信);
		
		jmsService.sendObject(messageDestination, m, JmsConstants.TYPE, JmsConstants.SEND_MESSAGE);
		System.out.println(message);
	}

}
