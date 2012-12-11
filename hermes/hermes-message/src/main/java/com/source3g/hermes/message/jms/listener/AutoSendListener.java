package com.source3g.hermes.message.jms.listener;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.CustomerGroup;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.message.MessageAutoSend;
import com.source3g.hermes.entity.message.MessageSendLog;
import com.source3g.hermes.enums.MessageStatus;
import com.source3g.hermes.enums.MessageType;
import com.source3g.hermes.message.CallInMessage;
import com.source3g.hermes.message.PhoneInfo;
import com.source3g.hermes.message.service.MessageService;
@Component
public class AutoSendListener implements MessageListener {

	@Autowired
	protected MongoTemplate mongoTemplate;
	
	@Autowired
	protected MessageService messageService;
	
	@Override
	public void onMessage(Message message) {
		if (message instanceof ObjectMessage) {
			ObjectMessage objectMessage = (ObjectMessage) message;
			try {
				Object obj = objectMessage.getObject();
				if (obj instanceof CallInMessage) {
					CallInMessage callInMessage = (CallInMessage) obj;
					String phone=callInMessage.getPhone();
					Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("_id").is(callInMessage.getMerchantId())), Merchant.class);
					Customer customer = mongoTemplate.findOne(new Query(Criteria.where("phone").is(phone).and("merchantId").is(merchant.getId())), Customer.class);
					MessageAutoSend messagecontent=mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchant.getId())), MessageAutoSend.class);
					if (merchant.getShortMessage().getSurplusMsgCount() <= 0) {
						PhoneInfo phoneInfo=genPhoneInfo(callInMessage.getMerchantId(), customer, messagecontent, MessageType.挂机短信, phone);
						messageService.updateLog(phoneInfo.getMessageSendLogId(), new Date(), MessageStatus.余额不足发送失败);
					}else{
						Update update = new Update();
						update.inc("shortMessage.surplusMsgCount", -1).inc("shortMessage.totalCount", -1).inc("shortMessage.sentCount", 1);
						mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(merchant.getId())), update, Merchant.class);
						PhoneInfo phoneInfo=genPhoneInfo(callInMessage.getMerchantId(), customer, messagecontent, MessageType.挂机短信, phone);
						messageService.send(phoneInfo.getPhoneNumber(), messagecontent.getNewMessageCotent());
					}
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	private PhoneInfo genPhoneInfo(ObjectId merchantId, Customer customer, MessageAutoSend messagecontent, MessageType type,String phone) {		
			if(customer == null || StringUtils.isEmpty(customer.getName())){
				MessageSendLog log = genMessageSendLog( merchantId, phone, 1, messagecontent.getNewMessageCotent(), type);
				PhoneInfo phoneInfo = new PhoneInfo(phone, messagecontent.getNewMessageCotent(), log.getId());
				return phoneInfo;
			}else{
				CustomerGroup customerGroup  = mongoTemplate.findOne(new Query(Criteria.where("_id").is(customer.getId())), CustomerGroup.class);
				if(customerGroup==null){
					customerGroup=null;
				}
				MessageSendLog log = genMessageSendLog(customer.getName(), merchantId, phone, 1, messagecontent.getOldMessageCotent(), type, customerGroup.getName());
				PhoneInfo phoneInfo = new PhoneInfo(customer.getPhone(), messagecontent.getOldMessageCotent(), log.getId());
				return phoneInfo;
			}
	}	
	
	private MessageSendLog genMessageSendLog(String name, ObjectId merchantId, String phone, int sendCount, String content, MessageType type, String customerGroupName) {
		MessageSendLog log = new MessageSendLog();
		log.setId(ObjectId.get());
		log.setContent(content);
		log.setSendTime(new Date());
		log.setCustomerName(name);
		log.setCustomerGroupName(customerGroupName);
		log.setMerchantId(merchantId);
		log.setPhone(phone);
		log.setSendCount(sendCount);
		log.setType(type);
		log.setStatus(MessageStatus.已发送);
		mongoTemplate.save(log);
		return log;
	}
	private MessageSendLog genMessageSendLog( ObjectId merchantId, String phone, int sendCount, String content, MessageType type) {
		MessageSendLog log = new MessageSendLog();
		log.setId(ObjectId.get());
		log.setContent(content);
		log.setSendTime(new Date());
		log.setMerchantId(merchantId);
		log.setPhone(phone);
		log.setSendCount(sendCount);
		log.setType(type);
		log.setStatus(MessageStatus.已发送);
		mongoTemplate.save(log);
		return log;
	}
	
}
