package com.source3g.hermes.message.jms.listener;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.message.GroupSendLog;
import com.source3g.hermes.entity.message.MessageSendLog;
import com.source3g.hermes.entity.message.ShortMessage;
import com.source3g.hermes.enums.MessageStatus;
import com.source3g.hermes.enums.MessageType;
import com.source3g.hermes.message.service.MessageService;
import com.source3g.hermes.utils.DateFormateUtils;
import com.source3g.hermes.utils.JmsUtils;

@Component
public class MessageSendListener implements MessageListener {

	@Autowired
	private MessageService messageService;

	@Autowired
	protected MongoTemplate mongoTemplate;

	@Override
	public void onMessage(Message msg) {
		ShortMessage shortMessage = null;
		try {
			System.out.println("哇哈哈");
			shortMessage = JmsUtils.getObject(msg, ShortMessage.class);
			Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("_id").is(shortMessage.getMerchantId())), Merchant.class);
			if (merchant == null) {
				throw new Exception("商户不存在");
			}
			if (merchant.getMessageBalance().getSurplusMsgCount() <= 0) {
				throw new Exception("余额不足发送失败");
			} else {
				shortMessage.setMsgId(DateFormateUtils.getTimeStampStr());
				MessageStatus status = messageService.send(shortMessage);
				if (MessageStatus.已发送.equals(status)) {
					Update updateSurplus = new Update();
					updateSurplus.inc("messageBalance.surplusMsgCount", -1).inc("messageBalance.totalCount", -1).inc("messageBalance.sentCount", 1);
					mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(merchant.getId())), updateSurplus, Merchant.class);
					if (MessageType.群发.equals(shortMessage.getMessageType())) {
						Update updateGroupLog = new Update();
						updateGroupLog.inc("successCount", 1);
						mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(shortMessage.getSendId())), updateGroupLog, GroupSendLog.class);
					} else {
						Update updateSendLog = new Update();
						updateSendLog.set("status", MessageStatus.已发送);
						mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(shortMessage.getSendId())), updateSendLog, MessageSendLog.class);
					}
				}
			}
		} catch (Exception e) {
			if (shortMessage != null) {
				MessageStatus status;
				try {
					status = MessageStatus.valueOf(e.getMessage());
				} catch (Exception e2) {
					status = MessageStatus.发送失败;
				}
				shortMessage.setStatus(status);
				mongoTemplate.save(shortMessage);
			}

		}
	}
}
