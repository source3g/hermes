package com.source3g.hermes.message.jms.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.source3g.hermes.entity.message.MessageSendLog;
import com.source3g.hermes.enums.MessageStatus;
import com.source3g.hermes.message.ShortMessageMessage;
import com.source3g.hermes.message.service.MessageService;

@Component
public class MessageSendListener implements MessageListener {

	@Autowired
	private MessageService messageService;

	@Override
	public void onMessage(Message message) {
		if (message instanceof ObjectMessage) {
			ObjectMessage objectMessage = (ObjectMessage) message;
			List<MessageSendLog> logs = new ArrayList<MessageSendLog>();
			try {
				Object obj = objectMessage.getObject();
				if (obj instanceof ShortMessageMessage) {
					ShortMessageMessage shortMessageMessage = (ShortMessageMessage) obj;
					List<Map<String, Object>> customersInfo = shortMessageMessage.getCustomers();
					String content = shortMessageMessage.getContent();
					for (Map<String, Object> map : customersInfo) {
						// System.out.println("向" + c.getName() + "发送" +
						// content);
						MessageStatus status = messageService.send((String) map.get("phone"), content);
						if (MessageStatus.已发送.equals(status)) {
							// TODO 消息发送成功处理
						}
						MessageSendLog log = new MessageSendLog();
						log.setContent(content);
						log.setCustomerName((String) map.get("name"));
						log.setSendTime(new Date());
						log.setMerchantId((ObjectId) map.get("merchantId"));
						log.setPhone((String) map.get("phone"));
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
			// jmsService.sendObject(messageDestination, logs.toArray(),
			// JmsConstants.TYPE, JmsConstants.LOG_MESSAGE);
		}

	}
}
