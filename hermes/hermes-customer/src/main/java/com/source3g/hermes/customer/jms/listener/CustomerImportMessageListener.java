package com.source3g.hermes.customer.jms.listener;

import java.io.File;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.source3g.hermes.customer.service.CustomerImportService;
import com.source3g.hermes.entity.customer.CustomerImportItem;
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
			CustomerImportLog importLog = null;
			try {
				obj = objectMessage.getObject();
				if (obj instanceof CustomerImportLog) {
					importLog = (CustomerImportLog) obj;
					Resource resource=new FileSystemResource(new File(importLog.getFilePath()));
					List<CustomerImportItem> importItems=customerImportService.readFromExcel(resource, importLog.getMerchant().getId().toString(), importLog.getId().toString());
					customerImportService.importCustomer(importItems, importLog.getMerchant().getId().toString(), importLog.getId().toString());
				}
			} catch (JMSException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				customerImportService.updateStatus(importLog, ImportStatus.导入失败);
			}
		}
	}
}
