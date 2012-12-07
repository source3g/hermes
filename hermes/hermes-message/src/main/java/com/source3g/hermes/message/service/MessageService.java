package com.source3g.hermes.message.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.jms.Destination;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.source3g.hermes.constants.JmsConstants;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.message.MessageSendLog;
import com.source3g.hermes.entity.message.MessageTemplate;
import com.source3g.hermes.enums.MessageStatus;
import com.source3g.hermes.enums.MessageType;
import com.source3g.hermes.message.ShortMessageMessage;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.service.JmsService;
import com.source3g.hermes.utils.Page;

@Service
public class MessageService extends BaseService {

	@Autowired
	private JmsService jmsService;

	@Autowired
	private Destination messageDestination;

	public void messageSend(String[] ids, String content) {
		Query query = new Query();
		List<ObjectId> customerGroupIds = new ArrayList<ObjectId>();
		for (String id : ids) {
			ObjectId ObjId = new ObjectId(id);
			customerGroupIds.add(ObjId);

		}
		query.addCriteria(Criteria.where("customerGroupId").in(customerGroupIds));
		List<Customer> customers = mongoTemplate.find(query, Customer.class);
		ShortMessageMessage message = new ShortMessageMessage();
		List<Map<String, Object>> customersInfo = handleCustomers(customers, content, MessageType.群发);
		message.setContent(content);
		message.setCustomers(customersInfo);
		message.setMessageType(MessageType.群发);

		jmsService.sendObject(messageDestination, message, JmsConstants.TYPE, JmsConstants.SEND_MESSAGE);
	}

	private List<Map<String, Object>> handleCustomers(List<Customer> customers, String content, MessageType type) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (Customer c : customers) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", c.getName());
			map.put("phone", c.getPhone());
			map.put("merchantId", c.getMerchantId());
			MessageSendLog log = genMessageSendLog(c, content, type);
			mongoTemplate.save(log);
			map.put("messageSendLogId", log.getId());
			result.add(map);
		}
		return result;
	}

	private MessageSendLog genMessageSendLog(Customer c, String content, MessageType type) {
		MessageSendLog log = new MessageSendLog();
		log.setId(ObjectId.get());
		log.setContent(content);
		log.setCustomerName(c.getName());
		log.setSendTime(new Date());
		log.setMerchantId(c.getMerchantId());
		log.setPhone(c.getPhone());
		log.setSendCount(1);
		log.setType(type);
		log.setStatus(MessageStatus.发送中);
		return log;
	}

	public List<MessageTemplate> listAll(String merchantId) {
		return mongoTemplate.find(new Query(Criteria.where("merchantId").is(new ObjectId(merchantId))), MessageTemplate.class);
	}

	public void save(MessageTemplate messageTemplate) {
		mongoTemplate.save(messageTemplate);
	}

	public void fastSend(String type, String[] customerPhoneArray, String content) {
		Query query = new Query();
		query.addCriteria(Criteria.where("phone").in(Arrays.asList(customerPhoneArray)));
		List<Customer> customers = mongoTemplate.find(query, Customer.class);
		ShortMessageMessage message = new ShortMessageMessage();
		List<Map<String, Object>> customersInfo = handleCustomers(customers, content, MessageType.群发);
		message.setContent(content);
		message.setCustomers(customersInfo);
		message.setMessageType(MessageType.群发);
		jmsService.sendObject(messageDestination, message, JmsConstants.TYPE, JmsConstants.SEND_MESSAGE);
	}

	public Page list(int pageNoInt, String merchantId, Date startTime, Date endTime, String phone, String customerGroupName) {
		Query query = new Query();
		if (StringUtils.isNotEmpty(phone)) {
			Pattern pattern = Pattern.compile("^.*" + phone + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("phone").is(pattern));
		}
		/*
		 * if(StringUtils.isNotEmpty(customerGroupName)){ Pattern pattern =
		 * Pattern.compile("^.*" +phone + ".*$", Pattern.CASE_INSENSITIVE);
		 * query
		 * .addCriteria(Criteria.where("customerGroupName").is(customerGroupName
		 * )); }
		 */
		if (startTime != null && endTime != null) {
			query.addCriteria(Criteria.where("sendTime").gte(startTime).lte(endTime));
		} else if (startTime != null) {
			query.addCriteria(Criteria.where("sendTime").gte(startTime));
		} else if (endTime != null) {
			query.addCriteria(Criteria.where("sendTime").lte(endTime));
		}
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
		System.out.println("向" + phoneNumber + "发送" + content);
		return MessageStatus.已发送;
	}

}
