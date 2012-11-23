package com.source3g.hermes.customer.jms.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.stereotype.Component;

@Component
public class CustomerDelMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		System.out.println("111");
		if (message instanceof ObjectMessage) {
			ObjectMessage objectMessage = (ObjectMessage) message;
			Object obj;
			try {
				obj = objectMessage.getObject();
				System.out.println(obj);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		System.out.println(message);
		System.out.println("recv.." + System.currentTimeMillis());
	}
}
