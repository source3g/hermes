package com.source3g.hermes.merchant.service;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.merchant.MerchantGroup;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.utils.Page;

@Service
public class MerchantGroupService extends BaseService {

	public void add(MerchantGroup merchantGroup) {
		merchantGroup.setId(ObjectId.get());
		mongoTemplate.insert(merchantGroup);
	}

	public Page list(int pageNo, MerchantGroup merchantGroup) {
		Query query = new Query();
		if (StringUtils.isNotEmpty(merchantGroup.getName())) {

			Pattern pattern = Pattern.compile("^.*" + merchantGroup.getName() + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("name").is(pattern));
		}
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, MerchantGroup.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNo);
		List<MerchantGroup> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), MerchantGroup.class);
		page.setData(list);
		return page;
	}

	public List<MerchantGroup> list(MerchantGroup merchantGroup) {
		Query query = new Query();
		if (StringUtils.isNotEmpty(merchantGroup.getName())) {

			Pattern pattern = Pattern.compile("^.*" + merchantGroup.getName() + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("name").is(pattern));
		}
		List<MerchantGroup> list = mongoTemplate.find(query, MerchantGroup.class);
		return list;
	}

	public void deleteById(String id) {
		super.deleteById(id, MerchantGroup.class);
	}

	public void update(MerchantGroup merchantGroup) {
		mongoTemplate.save(merchantGroup);
	}

	public MerchantGroup get(String id) {
		return mongoTemplate.findById(new ObjectId(id), MerchantGroup.class);
	}
}
