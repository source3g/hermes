package com.source3g.hermes.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.source3g.hermes.entity.AbstractEntity;

@Component
public abstract class BaseService {

	@Autowired
	protected MongoTemplate mongoTemplate;

	public void deleteById(String id) {
		ObjectId objId = new ObjectId(id);
		mongoTemplate.remove(new Query(Criteria.where("_id").is(objId)), getCollectionName());
	}

	public <T extends AbstractEntity> void add(T entity) {
		if (entity == null) {
			return;
		} else if (entity.getId() == null) {
			entity.setId(ObjectId.get());
		}
		mongoTemplate.insert(entity, getCollectionName());
	}

	public <T extends AbstractEntity> void update(T entity) {
		if (entity == null || entity.getId() == null) {
			return;
		}
	//	mongoTemplate.updateFirst(query, new Update()., collectionName)
		mongoTemplate.save(entity, getCollectionName());
	}

	public abstract String getCollectionName();

}
