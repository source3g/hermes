package com.source3g.hermes.message.jms.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.source3g.hermes.constants.JmsConstants;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.message.MessageSendLog;
import com.source3g.hermes.enums.MessageStatus;
import com.source3g.hermes.message.ShortMessageMessage;
import com.source3g.hermes.message.service.MessageService;
import com.source3g.hermes.service.JmsService;

@Component
public class MessageSendListener implements MessageListener {
	
	@Autowired
	private JmsService jmsService;
	@Autowired
	private Destination messageDestination;
	@Autowired
	private MessageService messageService;
	
	@Override
	public void onMessage(Message message) {
		if (message instanceof ObjectMessage) {
			ObjectMessage objectMessage = (ObjectMessage) message;
			List<MessageSendLog> logs=new ArrayList<MessageSendLog>();
			try {
				Object obj = objectMessage.getObject();
				if (obj instanceof ShortMessageMessage) {
					ShortMessageMessage shortMessageMessage = (ShortMessageMessage) obj;
					List<Customer> customers = shortMessageMessage.getCustomers();
					String content = shortMessageMessage.getContent();
					for (Customer c : customers) {
						System.out.println("向" + c.getName() + "发送" + content);
						MessageSendLog log=new MessageSendLog();
						log.setContent(content);
						log.setCustomerName(c.getName());
						log.setDate(new Date());
						log.setMerchantId(c.getMerchantId());
						log.setPnone(c.getPhone());
						log.setSendCount(1);
						log.setType(shortMessageMessage.getMessageType());
						log.setStatus(MessageStatus.已发送);
						logs.add(log);
						messageService.addLog(log);
					}
				}
			} catch (JMSException e) {

				e.printStackTrace();
			}
	//jmsService.sendObject(messageDestination, logs.toArray(), JmsConstants.TYPE, JmsConstants.LOG_MESSAGE);
		}

	}
}
