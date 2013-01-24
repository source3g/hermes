package com.source3g.hermes.message.jms.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.message.GroupSendLog;
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
					try {
						Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("_id").is(shortMessageMessage.getMerchantId())), Merchant.class);
						if (merchant == null) {
							return;
						}
						if (merchant.getShortMessage().getSurplusMsgCount() <= 0) {
							messageService.updateMessageSendLog(shortMessageMessage,MessageStatus.余额不足发送失败);
						} else {
							MessageStatus status = messageService.send(shortMessageMessage.getPhone(), shortMessageMessage.getContent());
							messageService.updateMessageSendLog(shortMessageMessage,status);
							if (MessageStatus.已发送.equals(status)) {
								Update update = new Update();
								update.inc("shortMessage.surplusMsgCount", -1).inc("shortMessage.totalCount", -1).inc("shortMessage.sentCount", 1);
								mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(merchant.getId())), update, Merchant.class);
								int successCount=shortMessageMessage.getGroupSendLog().getSendSuccessCount();
								shortMessageMessage.getGroupSendLog().setSendSuccessCount(successCount+1);
								mongoTemplate.save(shortMessageMessage.getGroupSendLog());
							}
						}
					} catch (Exception e) {
						messageService.updateMessageSendLog(shortMessageMessage,MessageStatus.发送失败);
					}
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}

	}
}
