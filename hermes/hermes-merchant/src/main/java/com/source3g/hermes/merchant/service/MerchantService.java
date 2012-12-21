package com.source3g.hermes.merchant.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.merchant.MerchantRemindTemplate;
import com.source3g.hermes.entity.merchant.MessageLog;
import com.source3g.hermes.entity.merchant.RemindTemplate;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.utils.Page;

@Service
public class MerchantService extends BaseService {

	
	public void add(Merchant merchant) throws Exception{
		List<Merchant> list=mongoTemplate.find(new Query(Criteria.where("account").is(merchant.getAccount())), Merchant.class);
		if(list.size()==0){
			mongoTemplate.insert(merchant);
		}else{
			throw new Exception("账号已存在");
		}
	}
	
	public boolean accountValidate(String account) {
			List<Merchant> list=mongoTemplate.find(new Query(Criteria.where("account").is(account)), Merchant.class);
			if(list.size()==0){
				return true;
			}
			return false;
	}
	
	public List<Merchant> list() {
		List<Merchant> list = mongoTemplate.findAll(Merchant.class);
		return list;
	}

	public Page list(int pageNo, Merchant merchant) {
		Query query = new Query();
		if (StringUtils.isNotEmpty(merchant.getName())) {
			Pattern pattern = Pattern.compile("^.*" + merchant.getName() + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("name").is(pattern));
		}

		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, Merchant.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNo);
		List<Merchant> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), Merchant.class);
		page.setData(list);
		return page;
	}

	public void deleteById(String id) {
		super.deleteById(id, Merchant.class);
	}

	public Merchant getMerchant(String id) {
		Merchant merchant = mongoTemplate.findById(new ObjectId(id), Merchant.class);
		return merchant;
	}

	public void update(Merchant merchant) {
		mongoTemplate.save(merchant);

	}

	public List<Merchant> findByDeviceIds(String ids) {
		List<ObjectId> deviceIds = new ArrayList<ObjectId>();
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			ObjectId ObjId = new ObjectId(id);
			deviceIds.add(ObjId);
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("deviceIds").in(deviceIds));
		return mongoTemplate.find(query, Merchant.class);

	}

	public List<Merchant> findByGroupId(String id) {
		ObjectId objId = new ObjectId(id);
		return mongoTemplate.find(new Query(Criteria.where("merchantGroupId").is(objId)), Merchant.class);
	}

	/**
	 * 给短信充值 count等于负数时做减法
	 */
	public void chargeMsg(String id, int count) {
		Update update = new Update();
		update.inc("shortMessage.totalCount", count);
		if (count > 0) {
			mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(id))), update, Merchant.class);
		} else {
			return;
		}
		MessageLog msgLog = new MessageLog();
		msgLog.setMerchantId(new ObjectId(id));
		msgLog.setCount(count);
		Date chargeTime = new Date();
		msgLog.setChargeTime(chargeTime);
		addMsgLog(msgLog);

	}

	private void addMsgLog(MessageLog messageLog) {
		super.add(messageLog);
	}

	public Page msgLogList(int pageNoInt) {
		Query query = new Query();
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, MessageLog.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNoInt);
		List<MessageLog> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), MessageLog.class);
		page.setData(list);
		return page;

	}

	public void updateInfo(Merchant merchant) {
		super.updateIncludeProperties(merchant, "name", "addr", "account", "password", "merchantGroupId", "deviceIds");
	}

	public void UpdateQuota(String id, int countInt) {
		Merchant merchant = mongoTemplate.findById(new ObjectId(id), Merchant.class);
		if(merchant.getShortMessage().getSurplusMsgCount()+countInt<0
				||merchant.getShortMessage().getSurplusMsgCount()+countInt>merchant.getShortMessage().getTotalCount()){
			return;
		}
		Update update = new Update();
		update.inc("shortMessage.surplusMsgCount", countInt);
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(id))), update, Merchant.class);	
	}

	public void Switch(Merchant merchant) {
		mongoTemplate.save(merchant);	
	}

	public List<RemindTemplate> remindList() {
		List<RemindTemplate> list = mongoTemplate.findAll(RemindTemplate.class);
		return list;
	}

	public void remindSave(ObjectId merchantId,MerchantRemindTemplate merchantRemindTemplate) {
		Merchant merchant=mongoTemplate.findOne(new Query(Criteria.where("_id").is(merchantId)), Merchant.class);
		List<MerchantRemindTemplate> merchantRemindTemplates=merchant.getMerchantRemindTemplates();
		if(merchantRemindTemplates==null){
			return;
		}
		for (MerchantRemindTemplate m:merchantRemindTemplates){
			if(m.getId().equals(merchantRemindTemplate.getId())){
				m.setMessageContent(merchantRemindTemplate.getMessageContent());
			}
		}
		mongoTemplate.save(merchant);	
	}

	public void remindDelete(ObjectId merchantId, ObjectId templateId) {
		Merchant merchant=mongoTemplate.findOne(new Query(Criteria.where("_id").is(merchantId)), Merchant.class);
		List<MerchantRemindTemplate> merchantRemindTemplates=merchant.getMerchantRemindTemplates(); 
		if(merchantRemindTemplates!=null){
			MerchantRemindTemplate merchantRemindTemplate=new MerchantRemindTemplate();
			merchantRemindTemplate.setId(templateId);
			merchantRemindTemplates.remove(merchantRemindTemplate);
		}
		mongoTemplate.save(merchant);
	}

	public void remindAdd(ObjectId merchantId, ObjectId templateId)  {
		Merchant merchant=mongoTemplate.findOne(new Query(Criteria.where("merchantRemindTemplates.remindTemplate.$id").is(templateId).and("_id").is(merchantId)), Merchant.class);
		if(merchant!=null){
			return ;
		}
		RemindTemplate template=mongoTemplate.findOne(new Query(Criteria.where("_id").is(templateId)), RemindTemplate.class);
		MerchantRemindTemplate merchantRemindTemplate=new MerchantRemindTemplate();
		merchantRemindTemplate.setId(ObjectId.get());
		merchantRemindTemplate.setMessageContent(template.getMessageContent());
		merchantRemindTemplate.setRemindTemplate(template);
		Update update=new Update();
		update.addToSet("merchantRemindTemplates", merchantRemindTemplate);
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(merchantId)), update, Merchant.class);
	}

	public List<MerchantRemindTemplate> merchantRemindList(ObjectId merchantId) {
		Merchant merchant=mongoTemplate.findOne(new Query(Criteria.where("_id").is(merchantId)), Merchant.class);
		return merchant.getMerchantRemindTemplates();
	}

}
