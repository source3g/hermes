package com.source3g.hermes.customer.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.customer.Customer;

@Service
public class CustomerService {
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public Customer add(Customer customer){
		customer.setId(ObjectId.get());
		mongoTemplate.insert(customer, "source3g.hermes.customer");
		return customer;
	}
	
	public List<Customer> listAll(){
		return mongoTemplate.findAll(Customer.class,"source3g.hermes.customer");
	}
	
}
