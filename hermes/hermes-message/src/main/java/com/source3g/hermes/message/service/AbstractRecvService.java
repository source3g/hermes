package com.source3g.hermes.message.service;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.source3g.hermes.entity.message.ShortMessage;
import com.source3g.hermes.enums.MessageStatus;
import com.source3g.hermes.service.BaseService;

public abstract class AbstractRecvService extends BaseService {

	public abstract void recv() throws Exception;

	public void updateLog(String msgId, MessageStatus status) {
		Update update = new Update();
		update.set("status", status);
		mongoTemplate.updateFirst(new Query(Criteria.where("msgId").is(msgId)), update, ShortMessage.class);
	}
}
