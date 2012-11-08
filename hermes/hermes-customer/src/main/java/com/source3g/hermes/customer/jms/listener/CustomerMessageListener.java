package com.source3g.hermes.customer.jms.listener;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Component;

@Component
public class CustomerMessageListener implements MessageListener{
	
	@Override
	public void onMessage(Message message) {
		System.out.println(message);
		System.out.println("recv.."+System.currentTimeMillis());
	}
}
