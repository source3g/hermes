package com.source3g.hermes.utils;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

public class JmsUtils {

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T getObject(Message message, Class<T> c) throws JMSException {
		if (message instanceof ObjectMessage) {
			ObjectMessage objectMessage = (ObjectMessage) message;
				Object obj = objectMessage.getObject();
				T t = (T) obj;
				return t;
		}
		return null;
	}
}
