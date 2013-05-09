package com.source3g.hermes.merchant.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.customer.CustomerGroup;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.merchant.MerchantRemindTemplate;
import com.source3g.hermes.entity.merchant.MerchantResource;
import com.source3g.hermes.entity.merchant.MessageChargeLog;
import com.source3g.hermes.entity.merchant.RemindTemplate;
import com.source3g.hermes.entity.merchant.Setting;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.utils.Page;

@Service
public class MerchantService extends BaseService {
	public Merchant login(String username, String password) {
		return mongoTemplate.findOne(new Query(Criteria.where("account").is(username).and("password").is(password).and("canceled").is(false)), Merchant.class);
	}

	public void add(Merchant merchant) throws Exception {
		List<Merchant> list = mongoTemplate.find(new Query(Criteria.where("account").is(merchant.getAccount())), Merchant.class);
		if (list.size() == 0) {
			mongoTemplate.insert(merchant);
			initMerchant(merchant);
		} else {
			throw new Exception("账号已存在");
		}
	}

	private void initMerchant(Merchant merchant) {
		addCustomerGroup(merchant);
	}

	private void addCustomerGroup(Merchant merchant) {
		CustomerGroup customerGroup = new CustomerGroup();
		customerGroup.setMerchantId(merchant.getId());
		customerGroup.setName("默认顾客组");
		customerGroup.setId(ObjectId.get());
		mongoTemplate.insert(customerGroup);
	}

	// 是验证商户账号是否存在
	public boolean accountValidate(String account) {
		List<Merchant> list = mongoTemplate.find(new Query(Criteria.where("account").is(account)), Merchant.class);
		if (list.size() == 0) {
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
		query.with(new Sort(Direction.DESC, "_id"));
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
		update.inc("messageBalance.totalCount", count);
		if (count > 0) {
			mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(id))), update, Merchant.class);
		} else {
			return;
		}
		MessageChargeLog msgLog = new MessageChargeLog();
		msgLog.setMerchantId(new ObjectId(id));
		msgLog.setCount(count);
		Date chargeTime = new Date();
		msgLog.setChargeTime(chargeTime);
		addMsgLog(msgLog);

	}

	private void addMsgLog(MessageChargeLog messageLog) {
		super.add(messageLog);
	}

	public Page msgLogList(ObjectId merchantId, int pageNoInt) {
		Query query = new Query();
		query.addCriteria(Criteria.where("merchantId").is(merchantId));
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, MessageChargeLog.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNoInt);
		List<MessageChargeLog> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), MessageChargeLog.class);
		page.setData(list);
		return page;

	}

	public void updateInfo(Merchant merchant) {
		super.updateIncludeProperties(merchant, "name", "addr", "account", "password", "merchantGroupId", "deviceIds", "merchantTagNodes", "salerId");
	}

	public void UpdateQuota(String id, int countInt) {
		Merchant merchant = mongoTemplate.findById(new ObjectId(id), Merchant.class);
		if (merchant.getMessageBalance().getSurplusMsgCount() + countInt < 0 || merchant.getMessageBalance().getSurplusMsgCount() + countInt > merchant.getMessageBalance().getTotalCount()) {
			return;
		}
		Update update = new Update();
		update.inc("messageBalance.surplusMsgCount", countInt);
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(id))), update, Merchant.class);
	}

	public void saveSwitch(ObjectId merchantId, Setting setting) {
		Update update = new Update();
		update.set("setting", setting);
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(merchantId)), update, Merchant.class);
	}

	public List<RemindTemplate> remindList() {
		List<RemindTemplate> list = mongoTemplate.find(new Query(Criteria.where("isDelete").is(false)), RemindTemplate.class);
		return list;
	}

	public void remindSave(ObjectId merchantId, MerchantRemindTemplate merchantRemindTemplate) {
		MerchantRemindTemplate merchantRemind = mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchantId).and("_id").is(merchantRemindTemplate.getId())), MerchantRemindTemplate.class);
		if (merchantRemind == null) {
			return;
		}
		merchantRemind.setMessageContent(merchantRemindTemplate.getMessageContent());
		merchantRemind.setAdvancedTime(merchantRemindTemplate.getAdvancedTime());
		mongoTemplate.save(merchantRemind);
	}

	public void remindDelete(ObjectId merchantId, ObjectId merchantRemindtemplateId) {
		MerchantRemindTemplate merchantRemindTemplate = mongoTemplate.findOne(new Query(Criteria.where("_id").is(merchantRemindtemplateId)), MerchantRemindTemplate.class);
		merchantRemindTemplate.setIsDelete(true);
		mongoTemplate.save(merchantRemindTemplate);
	}

	public void remindAdd(ObjectId merchantId, ObjectId remindtemplateId) {
		MerchantRemindTemplate merchantRemindTemplateOld = mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchantId).and("remindTemplate.$id").is(remindtemplateId)), MerchantRemindTemplate.class);
		RemindTemplate template = mongoTemplate.findOne(new Query(Criteria.where("_id").is(remindtemplateId)), RemindTemplate.class);
		MerchantRemindTemplate merchantRemindTemplate = new MerchantRemindTemplate();
		merchantRemindTemplate.setId(ObjectId.get());
		merchantRemindTemplate.setMessageContent(template.getMessageContent());
		merchantRemindTemplate.setRemindTemplate(template);
		merchantRemindTemplate.setMerchantId(merchantId);
		merchantRemindTemplate.setAdvancedTime(template.getAdvancedTime());

		// 是否已经增加过了
		if (merchantRemindTemplateOld != null) {
			merchantRemindTemplateOld.setIsDelete(false);
			mongoTemplate.save(merchantRemindTemplateOld);
		} else {
			mongoTemplate.insert(merchantRemindTemplate);
		}
	}

	public List<MerchantRemindTemplate> merchantRemindList(ObjectId merchantId) {
		List<MerchantRemindTemplate> list = mongoTemplate.find(new Query(Criteria.where("merchantId").is(merchantId).and("isDelete").is(false)), MerchantRemindTemplate.class);
		return list;
	}

	public void cancel(ObjectId merchantId) {
		Update update = new Update();
		update.set("canceled", true);
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(merchantId)), update, Merchant.class);
	}

	public void recover(ObjectId merchantId) {
		Update update = new Update();
		update.set("canceled", false);
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(merchantId)), update, Merchant.class);
	}

	public void passwordChange(String password, String newPassword, ObjectId merchantId) throws Exception {
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("password").is(password).and("_id").is(merchantId)), Merchant.class);
		if (merchant == null) {
			throw new Exception("原密码输入有误");
		} else {
			Update update = new Update();
			update.set("password", newPassword);
			mongoTemplate.updateFirst(new Query(Criteria.where("password").is(password).and("_id").is(merchantId)), update, Merchant.class);
		}
	}

	public Boolean passwordValidate(String password,ObjectId merchantId) {
		Boolean passwordValidate=true;
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("password").is(password).and("_id").is(merchantId)), Merchant.class);
		if(merchant == null){
			passwordValidate=false;
			return passwordValidate;
		}
		return passwordValidate;
	}
	
	public void addMerchantResource(ObjectId merchantId, String name) throws Exception {
		if (name == null || name.equals("")) {
			throw new Exception("名称不能为空 ");
		}
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("_id").is(merchantId)), Merchant.class);
		MerchantResource merchantResource
		= merchant.getMerchantResource();
		if (merchantResource == null) {
			merchant.setMerchantResource(new MerchantResource());
		}
		List<String> list = merchant.getMerchantResource().getResourceList();
		if (list == null) {
			merchant.getMerchantResource().setResourceList(new ArrayList<String>());
		}
		if (merchant.getMerchantResource().getResourceList().contains(name)) {
			throw new Exception("资源名称重复 ");
		}
		merchant.getMerchantResource().getResourceList().add(name);
		mongoTemplate.save(merchant);
	}

	public void deletemerchantResource(ObjectId merchantId, String name) {
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("_id").is(merchantId)), Merchant.class);
		List<String> list = merchant.getMerchantResource().getResourceList();
		list.remove(name);
		mongoTemplate.save(merchant);
	}

	public void updateMerchantResource(String messageContent, ObjectId merchantId) {
		Merchant merchant = super.findOne(new Query(Criteria.where("_id").is(merchantId)), Merchant.class);
		MerchantResource merchantResource = merchant.getMerchantResource();
		if (merchantResource == null) {
			merchant.setMerchantResource(new MerchantResource());
		}
		merchant.getMerchantResource().setMessageContent(messageContent);
		mongoTemplate.save(merchant);
	}

	public MerchantResource getMerchantResource(ObjectId merchantId) {
		Merchant merchant = mongoTemplate.findById(merchantId, Merchant.class);
		if (merchant != null) {
			return merchant.getMerchantResource();
		}
		return null;
	}


}
