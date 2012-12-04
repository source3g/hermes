package com.source3g.hermes.service;

import java.io.Serializable;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

@Service
public class JmsService {

	@Autowired
	private JmsTemplate jmsTemplate;

	public void sendString(Destination destination ,final String text,final String type,final String value) {
		jmsTemplate.send(destination,new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage(text);
				message.setStringProperty(type, value);
				return message;
			}
		});
	}
	public void sendObject(Destination destination,final  Serializable object,final String type,final String value){
		jmsTemplate.send(destination,new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				ObjectMessage objectMessage = session.createObjectMessage(object);
				objectMessage.setStringProperty(type, value);
				return objectMessage;
			}
		});
	}

}
