package com.source3g.hermes.message.utils;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.lxt2.javaapi.IReceiver;
import com.lxt2.protocol.cbip20.CbipReport;
import com.source3g.hermes.entity.message.ShortMessage;
import com.source3g.hermes.enums.MessageStatus;
import com.source3g.hermes.service.BaseService;

@Component
public class ReportReceiver implements IReceiver<CbipReport> {
	private MongoTemplate mongoTemplate;

	public ReportReceiver() {
		mongoTemplate = (MongoTemplate) BaseService.getBean("mongoTemplate");
	}

	@Override
	public void receive(CbipReport report) {
		System.out.println("cbipMessage:收到report:" + report);
		System.out.println(mongoTemplate);
		System.out.println("seq:" + report.getClientSeq());
		if (report.getStatus() == 0 && "0".equals(report.getErrorCode())) {
			mongoTemplate.updateFirst(new Query(Criteria.where("msgId").is(String.valueOf(report.getClientSeq()))), new Update().set("status", MessageStatus.发送成功), ShortMessage.class);
		}
		report.getStatus();// 0
		report.getErrorCode();// 0
	}
}
