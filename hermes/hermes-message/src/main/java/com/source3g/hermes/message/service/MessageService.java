package com.source3g.hermes.message.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.jms.Destination;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.source3g.hermes.constants.JmsConstants;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.CustomerGroup;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.message.MessageAutoSend;
import com.source3g.hermes.entity.message.MessageSendLog;
import com.source3g.hermes.entity.message.MessageTemplate;
import com.source3g.hermes.enums.MessageStatus;
import com.source3g.hermes.enums.MessageType;
import com.source3g.hermes.enums.PhoneOperator;
import com.source3g.hermes.message.PhoneInfo;
import com.source3g.hermes.message.ShortMessageMessage;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.service.JmsService;
import com.source3g.hermes.utils.Page;
import com.source3g.hermes.utils.PhoneUtils;

@Service
public class MessageService extends BaseService {

	@Autowired
	private JmsService jmsService;

	@Autowired
	private Destination messageDestination;

	/**
	 * 短信群发
	 * @param merchantId
	 * @param ids
	 * @param content
	 */
	public void messageGroupSend(ObjectId merchantId, String[] ids, String content) throws Exception {
		Query query = new Query();
		List<ObjectId> customerGroupIds = new ArrayList<ObjectId>();
		for (String id : ids) {
			ObjectId ObjId = new ObjectId(id);
			customerGroupIds.add(ObjId);
		}
		query.addCriteria(Criteria.where("customerGroupId").in(customerGroupIds));
		Merchant merchant=mongoTemplate.findOne(new Query(Criteria.where("_id").is(merchantId)), Merchant.class);
		List<Customer> customers = mongoTemplate.find(query, Customer.class);
		if(customers.size()>merchant.getShortMessage().getSurplusMsgCount()){
			throw new Exception("余额不足");
		}
		ShortMessageMessage message = new ShortMessageMessage();
		List<PhoneInfo> phoneInfos = genPhoneInfos(merchantId, customers, content, MessageType.群发);
		message.setContent(content);
		message.setPhoneInfos(phoneInfos);
		message.setMessageType(MessageType.群发);
		message.setMerchantId(merchantId);
		jmsService.sendObject(messageDestination, message, JmsConstants.TYPE, JmsConstants.SEND_MESSAGE);
	}

	private List<PhoneInfo> genPhoneInfos(ObjectId merchantId, List<Customer> customers, String content, MessageType type) {
		List<PhoneInfo> result = new ArrayList<PhoneInfo>();
		for (Customer c : customers) {
			CustomerGroup customerGroup = mongoTemplate.findById(c.getCustomerGroupId(), CustomerGroup.class);
			String customerGroupName = null;
			if (customerGroup != null) {
				customerGroupName = customerGroup.getName();
			}
			MessageSendLog log = genMessageSendLog(c.getName(), customerGroupName, merchantId, c.getPhone(), 1, content, type, MessageStatus.发送中);
			PhoneInfo phoneInfo = new PhoneInfo(c.getPhone(), content, log.getId());
			result.add(phoneInfo);
		}
		return result;
	}

	public MessageSendLog genMessageSendLog(String name, String customerGroupName, ObjectId merchantId, String phone, int sendCount, String content, MessageType type, MessageStatus status) {
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
		log.setStatus(status);
		mongoTemplate.save(log);
		return log;
	}

	private MessageSendLog genMessageSendLog(String phone, ObjectId merchantId, String content, MessageType type) {
		String customerName = null;
		String customerGroupName = null;
		Customer c = mongoTemplate.findOne(new Query(Criteria.where("phone").is(phone)), Customer.class);
		if (c != null) {
			customerName = c.getName();
			CustomerGroup customerGroup = mongoTemplate.findById(c.getCustomerGroupId(), CustomerGroup.class);
			if (customerGroup != null) {
				customerGroupName = customerGroup.getName();
			}
		}
		MessageSendLog log = genMessageSendLog(customerName, customerGroupName, merchantId, phone, 1, content, type, MessageStatus.发送中);
		return log;
	}

	public List<MessageTemplate> listAll(String merchantId) {
		return mongoTemplate.find(new Query(Criteria.where("merchantId").is(new ObjectId(merchantId))).with(new Sort(Direction.DESC,"_id")), MessageTemplate.class);
	}

	public void save(MessageTemplate messageTemplate) {
		mongoTemplate.save(messageTemplate);
	}

	public void fastSend(ObjectId merchantId, String[] customerPhoneArray, String content) throws Exception {
		Merchant merchant=mongoTemplate.findOne(new Query(Criteria.where("_id").is(merchantId)), Merchant.class);
		if(customerPhoneArray.length>merchant.getShortMessage().getSurplusMsgCount()){
			throw new Exception("余额不足");
		}
		ShortMessageMessage message = new ShortMessageMessage();
		List<PhoneInfo> phoneInfos = genPhoneInfos(merchantId, customerPhoneArray, content, MessageType.快捷发送);
		message.setContent(content);
		message.setPhoneInfos(phoneInfos);
		message.setMessageType(MessageType.快捷发送);
		message.setMerchantId(merchantId);
		jmsService.sendObject(messageDestination, message, JmsConstants.TYPE, JmsConstants.SEND_MESSAGE);
	}

	/**
	 * 生成电话信息，并生成发送日志
	 * 
	 * @param merchantId
	 * @param messageSendLogId
	 * @param customerPhoneArray
	 * @param content
	 * @param type
	 * @return
	 */
	private List<PhoneInfo> genPhoneInfos(ObjectId merchantId, String[] customerPhoneArray, String content, MessageType type) {
		List<PhoneInfo> result = new ArrayList<PhoneInfo>();
		for (String phone : customerPhoneArray) {
			// 生成发送记录
			MessageSendLog log = genMessageSendLog(phone, merchantId, content, type);
			// 发送记录生成完成
			PhoneInfo phoneInfo = new PhoneInfo(phone, content, log.getId());
			result.add(phoneInfo);
		}
		return result;
	}

	public Page list(int pageNoInt, ObjectId merchantId, Date startTime, Date endTime, String phone, String customerGroupName) {
		Query query = new Query();
		Criteria criteria = Criteria.where("merchantId").is(merchantId);
		if (StringUtils.isNotEmpty(phone)) {
			Pattern pattern = Pattern.compile("^.*" + phone + ".*$", Pattern.CASE_INSENSITIVE);
			criteria.and("phone").is(pattern);
		}

		if (StringUtils.isNotEmpty(customerGroupName)) {
			Pattern pattern1 = Pattern.compile("^.*" + customerGroupName + ".*$", Pattern.CASE_INSENSITIVE);
			// query.addCriteria(Criteria.where("customerGroupName").is(pattern1));
			criteria.and("customerGroupName").is(pattern1);
		}

		if (startTime != null && endTime != null) {
			criteria.and("sendTime").gte(startTime).lte(endTime);
		} else if (startTime != null) {
			criteria.and("sendTime").gte(startTime);
		} else if (endTime != null) {
			criteria.and("sendTime").lte(endTime);
		}
		query.addCriteria(criteria);
		query.with(new Sort(Direction.DESC,"_id"));
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, MessageSendLog.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNoInt);
		List<MessageSendLog> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), MessageSendLog.class);
		page.setData(list);
		return page;
	}

	public void addLog(MessageSendLog log) {
		mongoTemplate.insert(log);
	}

	public void updateLog(ObjectId id, Date date, MessageStatus status) {
		Update update = new Update();
		update.set("sendTime", date);
		update.set("status", status);
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(id)), update, MessageSendLog.class);
	}

	public MessageStatus send(String phoneNumber, String content) {
		// TODO 发送消息
		// System.out.println("向" + phoneNumber + "发送" + content);
		if (PhoneOperator.移动.equals(PhoneUtils.getOperatior(phoneNumber))) {
			return sendByCm(phoneNumber, content);
		}
		if (PhoneOperator.联通.equals(PhoneUtils.getOperatior(phoneNumber))) {
			return sendByCu(phoneNumber, content);
		}
		if (PhoneOperator.电信.equals(PhoneUtils.getOperatior(phoneNumber))) {
			return sendByCt(phoneNumber, content);
		}
		return MessageStatus.电话号码有误;
	}

	private MessageStatus sendByCt(String phoneNumber, String content) {
		System.out.println("通过电信向" + phoneNumber + "发送" + content);
		return MessageStatus.已发送;
	}

	private MessageStatus sendByCu(String phoneNumber, String content) {
		System.out.println("通过联通向" + phoneNumber + "发送" + content);
		return MessageStatus.已发送;
	}

	private MessageStatus sendByCm(String phoneNumber, String content) {
		System.out.println("通过移动向" + phoneNumber + "发送" + content);
		return MessageStatus.已发送;
	}

	public void saveMessageAutoSend(MessageAutoSend messageAutoSend) {
		Update update = new Update();
		update.set("newMessageCotent", messageAutoSend.getNewMessageCotent());
		update.set("oldMessageCotent", messageAutoSend.getOldMessageCotent());
		mongoTemplate.upsert(new Query(Criteria.where("merchantId").is(messageAutoSend.getMerchantId())), update, MessageAutoSend.class);
	}

	public MessageAutoSend getMessageAutoSend(ObjectId merchantId) {
		return mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchantId)), MessageAutoSend.class);
	}

}
