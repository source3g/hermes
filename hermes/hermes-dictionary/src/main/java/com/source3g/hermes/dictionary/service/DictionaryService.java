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

	public void remindSave(RemindTemplate remindTemplate) throws Exception {
		if(remindTemplate.getId()==null){
			remindTemplate.setId(ObjectId.get());
			add(remindTemplate);
		}else{
			RemindTemplate remindTemplateInDb=mongoTemplate.findOne(new Query(Criteria.where("title").is(remindTemplate.getTitle()).and("_id").ne(remindTemplate.getTitle())), RemindTemplate.class);
			if(remindTemplateInDb!=null){
				throw new Exception("该标题已使用");
			}
			mongoTemplate.save(remindTemplate);
		}
	}

	public void remindDelete(ObjectId id) throws Exception {
		List<Merchant> merchants=mongoTemplate.find(new Query(Criteria.where("merchantRemindTemplates.remindTemplate.$id").is(id)), Merchant.class);
		if(merchants.size()==0){
			mongoTemplate.remove(new Query(Criteria.where("_id").is(id)), RemindTemplate.class);
		}else{
			throw new Exception("该提醒已被占用");
		}
	}
	
	public RemindTemplate getRemindTemplate(ObjectId id){
		return mongoTemplate.findById(id, RemindTemplate.class);
	}

	public Boolean titleValidate(String title) {
		List<RemindTemplate> remindTemplates=mongoTemplate.find(new Query(Criteria.where("title").is(title)), RemindTemplate.class);
		if(remindTemplates.size()>0){
			return false;
		}
		return true;
	}

	public void add( RemindTemplate remindTemplate) throws Exception {
		RemindTemplate remindTemplateInDb=mongoTemplate.findOne(new Query(Criteria.where("title").is(remindTemplate.getTitle())), RemindTemplate.class);
		if(remindTemplateInDb!=null){
			throw new Exception("该标题已使用");
		}
		mongoTemplate.insert(remindTemplate);
		
	}
}
