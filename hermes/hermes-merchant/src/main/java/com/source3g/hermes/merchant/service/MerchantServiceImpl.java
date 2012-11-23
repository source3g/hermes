package com.source3g.hermes.merchant.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.source3g.hermes.constants.CollectionNameConstant;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.utils.Page;

@Service
public class MerchantServiceImpl extends BaseService implements MerchantService {

	private String collectionName = CollectionNameConstant.MERCHANT;

	@Override
	public void add(Merchant merchant) {
		super.add(merchant);
	}

	@Override
	public List<Merchant> list() {
		List<Merchant> list = mongoTemplate.findAll(Merchant.class, collectionName);
		return list;
	}

	public Page list(int pageNo, Merchant merchant) {
		Query query = new Query();
		if (StringUtils.isNotEmpty(merchant.getName())) {
			Pattern pattern = Pattern.compile("^.*" + merchant.getName() + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("name").is(pattern));
		}

		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, collectionName);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNo);
		List<Merchant> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), Merchant.class, collectionName);
		page.setData(list);
		return page;
	}

	@Override
	public void deleteById(String id) {
		ObjectId objId = new ObjectId(id);
		mongoTemplate.remove(new Query(Criteria.where("_id").is(objId)), collectionName);
	}

	@Override
	public Merchant getMerchant(String id) {
		Merchant merchant = mongoTemplate.findById(new ObjectId(id), Merchant.class, collectionName);
		return merchant;
	}

	public void update(Merchant merchant) {
		mongoTemplate.save(merchant, collectionName);
		
	}

	@Override
	public List<Merchant> findByDeviceIds(String ids) {
		List<ObjectId> deviceIds=new ArrayList<ObjectId>();
		String[] idArray=ids.split(",");
		for(String id:idArray){
			ObjectId ObjId=new ObjectId(id);
			 deviceIds.add(ObjId);
		}
		 Query query=new Query();
			query.addCriteria(Criteria.where("deviceIds").in(deviceIds));
		return mongoTemplate.find(query, Merchant.class, collectionName);
		
	}
	@Override
	public List<Merchant> findByGroupId(String id) {
		ObjectId objId=new ObjectId(id);
		return mongoTemplate.find(new Query(Criteria.where("merchantGroupId").is(objId)), Merchant.class);
	}
	/**
	 * 给短信充值 count等于负数时做减法
	 */
	@Override
	public void chargeMsg(String id, int count) {
		
		Update update=new Update();
		update.inc("shortMessage.totalMsg", count);
		if(count>0){
			mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(id))), update, Merchant.class);
		}else{
			mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(id)).and("shortMessage.totalMsg").gte(0-count)), update, Merchant.class);
		}
		
	}
	
	@Override
	public String getCollectionName() {
		return collectionName;
	}
}
