package com.source3g.hermes.customer.jms.listener;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.source3g.hermes.customer.service.CustomerImportService;

@Component
public class CustomerRemindImportMessageListener implements MessageListener {

	@Autowired
	private CustomerImportService customerImportService;

	@Override
	public void onMessage(Message arg0) {
		System.out.println("开始导入提醒信息");
	}
}
