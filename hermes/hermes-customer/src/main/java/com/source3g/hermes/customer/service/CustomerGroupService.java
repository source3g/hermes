package com.source3g.hermes.customer.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.customer.CustomerGroup;
import com.source3g.hermes.service.BaseService;

@Service
public class CustomerGroupService extends BaseService {

	public List<CustomerGroup> listAll(String merchantId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("merchantId").is(new ObjectId(merchantId)));
		return mongoTemplate.find(query, CustomerGroup.class);
	}

}
