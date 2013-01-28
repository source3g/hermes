package com.source3g.hermes.customer.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.CustomerGroup;
import com.source3g.hermes.service.BaseService;

@Service
public class CustomerGroupService extends BaseService {

	public List<CustomerGroup> listAll(String merchantId) {
		Query q = new Query();
		q.addCriteria(Criteria.where("merchantId").is(new ObjectId(merchantId)));
		return mongoTemplate.find(q, CustomerGroup.class);
	}
	public void add(CustomerGroup customerGroup) throws Exception{
		List<CustomerGroup> list=mongoTemplate.find(new Query(Criteria.where("name").is(customerGroup.getName()).and("merchantId").is(customerGroup.getMerchantId())), CustomerGroup.class);
		if(list.size()==0){
			mongoTemplate.insert(customerGroup);
		}else{
			throw new Exception("顾客组名称已存在");
		}
		
	}
	public boolean nameValidate(ObjectId id,String name) {
		List<CustomerGroup> list=mongoTemplate.find(new Query(Criteria.where("name").is(name).and("merchantId").is(id)), CustomerGroup.class);
		if(list.size()==0){
		return true;
		}
		return false;
	}
	public void updateCustomerGroup(String customerGroupId, String selectorId,String merchantId) {
		if(customerGroupId.equals(selectorId)){
			return ;
		}
		Update update=new Update();
		update.set("customerGroup.$id", new ObjectId(selectorId));
		mongoTemplate.updateMulti(new Query(Criteria.where("customerGroup.$id").is(new ObjectId(customerGroupId)).and("merchantId").is(new ObjectId(merchantId))), update, Customer.class);
		mongoTemplate.remove(new Query(Criteria.where("_id").is(new ObjectId(customerGroupId))), CustomerGroup.class);
	}
}
