package com.source3g.hermes.message.jms.listener;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.enums.MessageStatus;
import com.source3g.hermes.message.ShortMessageMessage;
import com.source3g.hermes.message.service.MessageService;

@Component
public class MessageSendListener implements MessageListener {

	@Autowired
	private MessageService messageService;

	@Autowired
	protected MongoTemplate mongoTemplate;

	@Override
	public void onMessage(Message message) {
		if (message instanceof ObjectMessage) {
			ObjectMessage objectMessage = (ObjectMessage) message;
			try {
				Object obj = objectMessage.getObject();
				if (obj instanceof ShortMessageMessage) {
					ShortMessageMessage shortMessageMessage = (ShortMessageMessage) obj;
					List<Map<String, Object>> customersInfo = shortMessageMessage.getCustomers();
					String content = shortMessageMessage.getContent();
					for (Map<String, Object> map : customersInfo) {
						Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("_id").is((ObjectId) map.get("merchantId"))), Merchant.class);
						if (merchant.getShortMessage().getSurplusMsgCount() <= 0) {
							messageService.updateLog((ObjectId) map.get("messageSendLogId"), new Date(), MessageStatus.余额不足发送失败);
						} else {
							merchant.getShortMessage().setSurplusMsgCount(merchant.getShortMessage().getSurplusMsgCount() - 1);
							merchant.getShortMessage().setTotalCount(merchant.getShortMessage().getTotalCount() - 1);
							merchant.getShortMessage().setSentCount(merchant.getShortMessage().getSentCount() + 1);
							mongoTemplate.save(merchant);
							messageService.send((String) map.get("phone"), content);
							messageService.updateLog((ObjectId) map.get("messageSendLogId"), new Date(), MessageStatus.已发送);

						}
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
