package com.source3g.hermes.merchant.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.utils.Page;

@Service
public class MerchantServiceImpl extends BaseService implements MerchantService {

	@Override
	public void add(Merchant merchant) {
		super.add(merchant);
	}

	@Override
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

	@Override
	public void deleteById(String id) {
		super.deleteById(id, Merchant.class);
	}

	@Override
	public Merchant getMerchant(String id) {
		Merchant merchant = mongoTemplate.findById(new ObjectId(id), Merchant.class);
		return merchant;
	}

	public void update(Merchant merchant) {
		mongoTemplate.save(merchant);

	}

	@Override
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

	@Override
	public List<Merchant> findmerchantsByMerchantGroupId(String id) {
		ObjectId objId = new ObjectId(id);
		return mongoTemplate.find(new Query(Criteria.where("merchantGroupId").is(objId)), Merchant.class);
	}

}
