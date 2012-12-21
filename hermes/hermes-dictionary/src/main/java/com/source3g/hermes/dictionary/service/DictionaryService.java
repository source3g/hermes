package com.source3g.hermes.dictionary.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.merchant.RemindTemplate;
import com.source3g.hermes.service.BaseService;

@Service
public class DictionaryService extends BaseService {

	public List<RemindTemplate> remindList() {
		List<RemindTemplate> list = mongoTemplate.findAll(RemindTemplate.class);
		return list;
	}

	public void remindSave(RemindTemplate remindTemplate) {
		mongoTemplate.save(remindTemplate);
	}

	public void remindDelete(ObjectId id) throws Exception {
		List<Merchant> merchants=mongoTemplate.find(new Query(Criteria.where("merchantRemindTemplates.remindTemplate.$id").is(id)), Merchant.class);
		if(merchants.size()==0){
			mongoTemplate.remove(new Query(Criteria.where("_id").is(id)), RemindTemplate.class);
		}
		throw new Exception("该提醒已被占用");
	}
}
