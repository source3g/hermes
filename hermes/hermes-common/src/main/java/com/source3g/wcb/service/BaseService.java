package com.source3g.wcb.service;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public abstract class BaseService {
	
	protected String collectionName;
	
	protected MongoTemplate mongoTemplate;

	public void deleteById(String id) {
		ObjectId objId = new ObjectId(id);
		mongoTemplate.remove(new Query(Criteria.where("_id").is(objId)), collectionName);
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	
	
}
