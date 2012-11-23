package com.source3g.hermes.customer.jms.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.source3g.hermes.customer.service.CustomerImportService;
import com.source3g.hermes.entity.customer.CustomerImportLog;
import com.source3g.hermes.enums.ImportStatus;

@Component
public class CustomerImportMessageListener implements MessageListener {
	@Autowired
	private CustomerImportService customerImportService;

	@Override
	public void onMessage(Message message) {
		if (message instanceof ObjectMessage) {
			ObjectMessage objectMessage = (ObjectMessage) message;
			Object obj;
			try {
				obj = objectMessage.getObject();
				if (obj instanceof CustomerImportLog) {
					CustomerImportLog importLog = (CustomerImportLog) obj;
					customerImportService.updateStatus(importLog, ImportStatus.导入中);
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
