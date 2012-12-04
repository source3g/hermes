package com.source3g.hermes.message.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.message.MessageSendLog;
import com.source3g.hermes.entity.message.MessageTemplate;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.utils.Page;

@Service
public class MessageService extends BaseService {

	public void messageSend(String[] ids, String content) {
		Query query = new Query();
		List<ObjectId> customerGroupIds = new ArrayList<ObjectId>();
		for(String id:ids){
			ObjectId ObjId = new ObjectId(id);
			customerGroupIds.add(ObjId);
			
		}
		query.addCriteria(Criteria.where("customerGroupId").in(customerGroupIds));
		List<Customer> customers=mongoTemplate.find(query, Customer.class);
		for(Customer customer:customers){
			System.out.println("已向"+customer.getName()+"发送"+content);
		}
		
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
		for(Customer customer:customers){
			System.out.println("已向"+customer.getName()+"发送"+content);
		}
	}

	public Page list(int pageNoInt, String merchantId) {
		Query query = new Query();
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, Merchant.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNoInt);
		List<MessageSendLog> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), MessageSendLog.class);
		page.setData(list);
		return page;
	}

}
