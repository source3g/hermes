package com.source3g.hermes.service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

@Service
public class JmsService {
	
	private static final Logger logger=LoggerFactory.getLogger(JmsService.class);
	

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private JmsTemplate jmsTemplate;

	public void sendString(Destination destination, final String text, final String type, final String value) {
		try {
			jmsTemplate.send(destination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					TextMessage message = session.createTextMessage(text);
					message.setStringProperty(type, value);
					return message;
				}
			});
		} catch (Exception e) {
			logger.debug("消息发送失败"+destination.toString()+":"+text);
			e.printStackTrace();
			FailedJms failedJms = new FailedJms();
			failedJms.setId(ObjectId.get());
			failedJms.setDestination(destination);
			failedJms.setMessage(text);
			failedJms.setFailedTime(new Date());
			Map<String, String> properties = new HashMap<String, String>();
			properties.put(type, value);
			failedJms.setProperties(properties);
			saveFailedJms(failedJms);
		}
	}

	public void sendObject(Destination destination, final Serializable object, final String type, final String value) {
		try {
			jmsTemplate.send(destination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					ObjectMessage objectMessage = session.createObjectMessage(object);
					objectMessage.setStringProperty(type, value);
					return objectMessage;
				}
			});
		} catch (Exception e) {
			logger.debug("消息发送失败"+destination.toString()+":"+object);
			e.printStackTrace();
			FailedJms failedJms = new FailedJms();
			failedJms.setId(ObjectId.get());
			failedJms.setDestination(destination);
			failedJms.setMessage(object);
			failedJms.setFailedTime(new Date());
			Map<String, String> properties = new HashMap<String, String>();
			properties.put(type, value);
			failedJms.setProperties(properties);
			saveFailedJms(failedJms);
		}
	}

	public void saveFailedJms(FailedJms failedJms) {
		mongoTemplate.save(failedJms);
	}

}
