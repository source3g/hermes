package com.source3g.hermes.message.jms.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.message.AutoSendMessageTemplate;
import com.source3g.hermes.enums.MessageType;
import com.source3g.hermes.message.CallInMessage;
import com.source3g.hermes.message.service.MessageService;
import com.source3g.hermes.utils.JmsUtils;

@Component
public class CallInListener implements MessageListener {
	@Autowired
	protected MongoTemplate mongoTemplate;
	@Autowired
	protected MessageService messageService;

	@Override
	public void onMessage(Message message) {
		try {
			CallInMessage callInMessage =JmsUtils.getObject(message, CallInMessage.class);
			AutoSendMessageTemplate messagecontent = mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(callInMessage.getMerchantId())), AutoSendMessageTemplate.class);
			Customer customer = mongoTemplate.findOne(new Query(Criteria.where("phone").is(callInMessage.getPhone()).and("merchantId").is(callInMessage.getMerchantId())), Customer.class);
			String content="";
			if (customer != null) {
				content = messagecontent.getOldMessageCotent();
			}else{
				content=messagecontent.getNewMessageCotent();
			}
			messageService.singleSend(customer,content,MessageType.挂机短信);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		
//		if (message instanceof ObjectMessage) {
//			ObjectMessage objectMessage = (ObjectMessage) message;
//			try {
//				Object obj = objectMessage.getObject();
//				if (obj instanceof CallInMessage) {
//					CallInMessage callInMessage = (CallInMessage) obj;
//					String phone = callInMessage.getPhone();
//					Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("_id").is(callInMessage.getMerchantId())), Merchant.class);
//					AutoSendMessageTemplate messagecontent = mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchant.getId())), AutoSendMessageTemplate.class);
//
//					@SuppressWarnings("unused")
//					String customerName = null;
//					@SuppressWarnings("unused")
//					String customerGroupName = null;
//					String content = null;
//					Customer customer = mongoTemplate.findOne(new Query(Criteria.where("phone").is(phone).and("merchantId").is(merchant.getId())), Customer.class);
//					if (customer != null) {
//						content = messagecontent.getOldMessageCotent();
//						customerName = customer.getName();
//						CustomerGroup customerGroup = mongoTemplate.findOne(new Query(Criteria.where("_id").is(customer.getId())), CustomerGroup.class);
//						if (customerGroup != null) {
//							customerGroupName = customerGroup.getName();
//						}
//					} else {
//						content = messagecontent.getNewMessageCotent();
//					}
//					if (merchant.getMessageBalance().getSurplusMsgCount() <= 0) {
//						messageService.genMessageSendLog(customer, merchant.getId(), 1, content, MessageType.挂机短信, MessageStatus.余额不足发送失败);
//					} else {
//						Update update = new Update();
//						update.inc("messageBalance.surplusMsgCount", -1).inc("messageBalance.totalCount", -1).inc("messageBalance.sentCount", 1);
//						mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(merchant.getId())), update, Merchant.class);
//						
//						String content1=messageService.processContent(merchant, customer, content);
//						MessageStatus status = messageService.send(DateFormateUtils.getTimeStampStr(),phone, content1);
//						messageService.genMessageSendLog(customer, merchant.getId(), 1, content, MessageType.挂机短信, status);
//					}
//				}
//			} catch (JMSException e) {
//				e.printStackTrace();
//			}
//		}
	}

}
