package com.source3g.hermes.customer.jms.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.source3g.hermes.customer.service.CustomerImportService;
import com.source3g.hermes.entity.customer.CustomerRemindImportLog;

@Component
public class CustomerRemindImportMessageListener implements MessageListener {
	private static Logger logger = LoggerFactory.getLogger(CustomerRemindImportMessageListener.class);
	@Autowired
	private CustomerImportService customerImportService;

	@Override
	public void onMessage(Message message) {
		System.out.println("开始导入提醒信息");
		ObjectMessage objectMessage = (ObjectMessage) message;
		Object obj;
		CustomerRemindImportLog importLog = null;
		try {
			obj = objectMessage.getObject();
			if (obj instanceof CustomerRemindImportLog) {
				importLog = (CustomerRemindImportLog) obj;
				customerImportService.importCustomerRemind(importLog);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("接受提醒消息出错:" + e.getMessage());
		}

	}
}
