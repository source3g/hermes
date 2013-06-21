package com.source3g.hermes.message.utils;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.lxt2.javaapi.IPassiveSubmitSender;
import com.lxt2.protocol.IApiSubmit;
import com.lxt2.protocol.cbip20.CbipSubmit;
import com.source3g.hermes.entity.message.GroupSendLog;
import com.source3g.hermes.entity.message.MessageSendLog;
import com.source3g.hermes.entity.message.ShortMessage;
import com.source3g.hermes.enums.MessageStatus;
import com.source3g.hermes.enums.MessageType;
import com.source3g.hermes.message.service.MessageQueueService;

@Service
public class SmsPassiveSubmitSender implements IPassiveSubmitSender {

	@Autowired
	private MessageQueueService messageQueueService;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Value(value = "${cbip.productId}")
	private Integer productId;

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

	public void sendSuccess(String msgId) {
		mongoTemplate.updateFirst(new Query(Criteria.where("msgId").is(msgId)), new Update().set("status", MessageStatus.已发送), ShortMessage.class);
	}

	@Override
	public IApiSubmit getSubmit() {
		CbipSubmit smsSubmit = null;
		ShortMessage shortMessage = messageQueueService.poll();
		if (shortMessage != null) {
			try {
				smsSubmit = new CbipSubmit();
				smsSubmit.setClientSeq(Long.parseLong(shortMessage.getMsgId()));
				smsSubmit.setSrcNumber("");
				smsSubmit.setMessagePriority((byte) 1);
				smsSubmit.setReportType((short) 1);
				smsSubmit.setMessageFormat((byte) 15);
				// submit.setOverTime(System.currentTimeMillis());
				smsSubmit.setSendGroupID(0);
				smsSubmit.setProductID(productId);
				smsSubmit.setMessageType((byte) 0);
				// 如果手机号码组包个数超出限制，可能抛出异常
				smsSubmit.setDestMobiles(shortMessage.getPhone());
				// 如果短信内容超出长度限制，可能抛出异常
				smsSubmit.setContentString(shortMessage.getContent());
				smsSubmit.setSendTime(System.currentTimeMillis());
				sendSuccess(shortMessage.getMsgId());
				if (MessageType.群发.equals(shortMessage.getMessageType())) {
					addGroupSuccess(shortMessage.getSendId());
				} else {
					updateLog(shortMessage.getSendId(), new Date(), MessageStatus.已发送);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return smsSubmit;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}
}
