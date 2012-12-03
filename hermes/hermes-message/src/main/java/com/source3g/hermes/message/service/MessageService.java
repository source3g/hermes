package com.source3g.hermes.message.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.message.MessageTemplate;
import com.source3g.hermes.service.BaseService;

@Service
public class MessageService extends BaseService {

	public void messageSend(String[] ids, String messageInfo) {
		Query query = new Query();
		List<ObjectId> customerGroupIds = new ArrayList<ObjectId>();
		for(String id:ids){
			ObjectId ObjId = new ObjectId(id);
			customerGroupIds.add(ObjId);
			
		}
		query.addCriteria(Criteria.where("customerGroupId").in(customerGroupIds));
		List<Customer> customers=mongoTemplate.find(query, Customer.class);
		for(Customer customer:customers){
			System.out.println("已向"+customer.getName()+"发送"+messageInfo);
		}
		
	}

	public List<MessageTemplate> listAll(String merchantId) {
		return mongoTemplate.find(new Query(Criteria.where("merchantId").is(new ObjectId(merchantId))), MessageTemplate.class);
	}

	public void save(MessageTemplate messageTemplate) {
		mongoTemplate.save(messageTemplate);
	}

}
