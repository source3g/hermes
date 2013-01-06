package com.source3g.hermes.dictionary.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.merchant.MerchantRemindTemplate;
import com.source3g.hermes.entity.merchant.RemindTemplate;
import com.source3g.hermes.service.BaseService;

@Service
public class DictionaryService extends BaseService {

	public List<RemindTemplate> remindList() {
		List<RemindTemplate> list=mongoTemplate.find(new Query(Criteria.where("isDelete").is(false)), RemindTemplate.class);
		return list;
	}

	public void remindSave(RemindTemplate remindTemplate) throws Exception {
		if(remindTemplate.getId()==null){
			remindTemplate.setId(ObjectId.get());
			add(remindTemplate);
		}else{
			//RemindTemplate remindTemplateInDb=mongoTemplate.findOne(new Query(Criteria.where("title").is(remindTemplate.getTitle()).and("_id").is(remindTemplate.getId())), RemindTemplate.class);
			mongoTemplate.save(remindTemplate);
		}
	}

	public void remindDelete(ObjectId id) throws Exception {
		RemindTemplate remindTemplate=mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), RemindTemplate.class);
		List<MerchantRemindTemplate> merchantRemindTemplate =mongoTemplate.find(new Query(Criteria.where("remindTemplate.$id").is(id).and("isDelete").is(false)), MerchantRemindTemplate.class);
		if(merchantRemindTemplate.size()==0){
			remindTemplate.setIsDelete(true);
			mongoTemplate.save(remindTemplate);
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
		RemindTemplate remindTemplate1=mongoTemplate.findOne(new Query(Criteria.where("title").is(remindTemplate.getTitle())), RemindTemplate.class);
		if(remindTemplate1!=null&&remindTemplate1.getIsDelete()==false){
			throw new Exception("该标题已被占用");
		}else if(remindTemplate1!=null&&remindTemplate1.getIsDelete()==true){
			remindTemplate1.setAdvancedTime(remindTemplate.getAdvancedTime());
			remindTemplate1.setIsDelete(false);
			remindTemplate1.setMessageContent(remindTemplate.getMessageContent());
			remindTemplate1.setTitle(remindTemplate.getTitle());
			mongoTemplate.save(remindTemplate1);
		}else{
			mongoTemplate.insert(remindTemplate);
		}
	}
}
