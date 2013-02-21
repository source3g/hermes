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
import com.source3g.hermes.message.ShortMessageRecord;
import com.source3g.hermes.message.service.MessageService;
import com.source3g.hermes.utils.DateFormateUtils;

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
				if (obj instanceof ShortMessageRecord) {
					ShortMessageRecord shortMessageRecord = (ShortMessageRecord) obj;
					try {
						Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("_id").is(shortMessageRecord.getMerchantId())), Merchant.class);
						if (merchant == null) {
							throw new Exception("商户不存在");
						}
						if (merchant.getMessageBalance().getSurplusMsgCount() <= 0) {
							messageService.updateMessageSendLog(shortMessageRecord, MessageStatus.余额不足发送失败);
							throw new Exception("余额不足发送失败");
						} else {
							shortMessageRecord.setMsgId(DateFormateUtils.getTimeStampStr());
							MessageStatus status = messageService.send(shortMessageRecord);
							shortMessageRecord.setStatus(status);
							messageService.updateMessageSendLog(shortMessageRecord, status);
							if (MessageStatus.已发送.equals(status)) {
								Update updateSurplus = new Update();
								updateSurplus.inc("messageBalance.surplusMsgCount", -1).inc("messageBalance.totalCount", -1).inc("messageBalance.sentCount", 1);
								mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(merchant.getId())), updateSurplus, Merchant.class);
								Update updateGroupLog = new Update();
								updateGroupLog.inc("successCount", 1);
								mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(shortMessageRecord.getGroupLogId())), updateGroupLog, GroupSendLog.class);
								mongoTemplate.save(shortMessageRecord);
							}
						}
					} catch (Exception e) {
						messageService.updateMessageSendLog(shortMessageRecord, MessageStatus.发送失败);
						shortMessageRecord.setStatus(MessageStatus.发送失败);
					}finally{
						mongoTemplate.save(shortMessageRecord);
					}
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}

	}
}
