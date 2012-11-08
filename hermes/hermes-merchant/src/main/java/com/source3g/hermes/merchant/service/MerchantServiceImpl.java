package com.source3g.hermes.merchant.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.service.BaseService;

@Service
public class MerchantServiceImpl extends BaseService implements MerchantService {
	@Autowired
	private MongoTemplate mongoTemplate;
	private String collectionName="merchant";
	@Override
	public void add(Merchant merchant) {
		merchant.setId(ObjectId.get());
		mongoTemplate.insert(merchant, collectionName);
	}

	@Override
	public List<Merchant> list() {
		List<Merchant> list= mongoTemplate.findAll(Merchant.class,collectionName);
		return list;
	}

	@Override
	public void deleteById(String id) {
		ObjectId objId=new ObjectId(id);
		mongoTemplate.remove(
				new Query(
		                    Criteria.where("_id").is(objId)
		                ), collectionName
		          );
	}

	@Override
	public Merchant getMerchant(String id) {
		Merchant merchant=mongoTemplate.findById(new ObjectId(id), Merchant.class,collectionName);
		return merchant;
	}
	
	public void update(Merchant merchant){
		mongoTemplate.save(merchant, collectionName);
		
	}

	@Override
	public String getCollectionName() {
		return collectionName;
	}
}
