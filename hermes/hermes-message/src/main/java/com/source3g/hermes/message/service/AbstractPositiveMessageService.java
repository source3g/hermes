package com.source3g.hermes.message.service;

import java.util.Date;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.message.GroupSendLog;
import com.source3g.hermes.entity.message.MessageSendLog;
import com.source3g.hermes.entity.message.ShortMessage;
import com.source3g.hermes.enums.MessageStatus;
import com.source3g.hermes.enums.MessageType;

@Service
public abstract class AbstractPositiveMessageService {
	private static Logger logger = LoggerFactory.getLogger(AbstractPositiveMessageService.class);
	@Autowired
	private MongoTemplate mongoTemplate;

	protected abstract String send(ShortMessage shortMessage) throws Exception;

	public void reportSuccess(String msgId) {
		mongoTemplate.updateFirst(new Query(Criteria.where("msgId").is(msgId)), new Update().set("status", MessageStatus.发送成功), ShortMessage.class);
	}

	public String sendMessage(ShortMessage shortMessage) {
		if (shortMessage == null) {
			return null;
		}
		logger.debug("向服务器发送" + shortMessage.getPhone() + ":" + shortMessage.getContent() + "使用通道" + this.getClass());
		String msgId = null;
		try {
			msgId = send(shortMessage);
			sendSuccess(shortMessage.getId(), msgId);
			if (MessageType.群发.equals(shortMessage.getMessageType())) {
				addGroupSuccess(shortMessage.getSendId());
			} else {
				updateLog(shortMessage.getSendId(), new Date(), MessageStatus.已发送);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (MessageType.群发.equals(shortMessage.getMessageType())) {
				// addGroupSuccess(shortMessage.getSendId());
			} else {
				updateLog(shortMessage.getSendId(), new Date(), MessageStatus.发送失败);
			}
		}
		return msgId;
	}

	public void sendSuccess(ObjectId id, String msgId) {
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(id)), new Update().set("status", MessageStatus.已发送).set("msgId", msgId),
				ShortMessage.class);
	}

	public void updateLog(ObjectId id, Date date, MessageStatus status) {
		Update update = new Update();
		update.set("sendTime", date);
		update.set("status", status);
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(id)), update, MessageSendLog.class);
	}

	public void addGroupSuccess(ObjectId groupId) {
		Update updateGroupLog = new Update();
		updateGroupLog.inc("successCount", 1);
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(groupId)), updateGroupLog, GroupSendLog.class);
	}

}
