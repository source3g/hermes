package com.source3g.hermes.sync.jms.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.source3g.hermes.service.TaskService;

@Component
public class PackageAllListener implements MessageListener {
	@Autowired
	private TaskService taskService;

	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			try {
				String sn = textMessage.getText();
				try {
					taskService.init(sn);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
