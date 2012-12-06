package com.source3g.hermes.message.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.jms.Destination;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.constants.JmsConstants;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.message.MessageSendLog;
import com.source3g.hermes.entity.message.MessageTemplate;
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
		for(String id:ids){
			ObjectId ObjId = new ObjectId(id);
			customerGroupIds.add(ObjId);
			
		}
		query.addCriteria(Criteria.where("customerGroupId").in(customerGroupIds));
		List<Customer> customers=mongoTemplate.find(query, Customer.class);
		ShortMessageMessage message=new ShortMessageMessage();
		message.setContent(content);
		message.setCustomers(customers);
		message.setMessageType(MessageType.群发);

		jmsService.sendObject(messageDestination, message, JmsConstants.TYPE, JmsConstants.SEND_MESSAGE);
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
		List<Customer> customers=mongoTemplate.find(query, Customer.class);

		ShortMessageMessage message=new ShortMessageMessage();
		message.setContent(content);
		message.setCustomers(customers);
		message.setMessageType(MessageType.群发);
		jmsService.sendObject(messageDestination, message, JmsConstants.TYPE, JmsConstants.SEND_MESSAGE);
	}

	public Page list(int pageNoInt, String merchantId,Date startTime, Date endTime,String phone,String customerGroupName ) {
		Query query = new Query();
		if (StringUtils.isNotEmpty(phone)) {
			Pattern pattern = Pattern.compile("^.*" +phone + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("phone").is(pattern));
		}
/*		if(StringUtils.isNotEmpty(customerGroupName)){
			Pattern pattern = Pattern.compile("^.*" +phone + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("customerGroupName").is(customerGroupName));
		}*/
		if (startTime != null && endTime != null) {
			query.addCriteria(Criteria.where("sendTime").gte(startTime).lte(endTime));
		} else if (startTime != null) {
			query.addCriteria(Criteria.where("sendTime").gte(startTime));
		} else if (endTime != null) {
			query.addCriteria(Criteria.where("sendTime").lte(endTime));
		}
		List<MessageSendLog> r=mongoTemplate.findAll(MessageSendLog.class);
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, MessageSendLog.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNoInt);
		List<MessageSendLog> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), MessageSendLog.class);
		page.setData(list);
		return page;
	}
	
	public void addLog(MessageSendLog log){
		mongoTemplate.insert(log);
	}

}
