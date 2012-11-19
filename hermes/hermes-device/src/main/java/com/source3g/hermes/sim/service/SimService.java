package com.source3g.hermes.sim.service;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.constants.CollectionNameConstant;
import com.source3g.hermes.entity.Sim;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.utils.Page;

@Service
public class SimService extends BaseService {

	private String collectionName = CollectionNameConstant.SIM;

	public void add(Sim sim) {
		sim.setId(ObjectId.get());
		mongoTemplate.insert(sim, collectionName);
	}

	public Page list(int pageNo, Sim sim) {
		Query query = new Query();
		if (StringUtils.isNotEmpty(sim.getNo())) {
			Pattern pattern = Pattern.compile("^.*" + sim.getNo() + ".*$",
					Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("no").is(pattern));
		}
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, collectionName);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNo);
		List<Sim> list = mongoTemplate.find(query.skip(page.getStartRow())
				.limit(page.getPageSize()), Sim.class, collectionName);
		page.setData(list);
		return page;
	}

	public void deleteById(String id) {
		ObjectId objId = new ObjectId(id);
		mongoTemplate.remove(new Query(Criteria.where("_id").is(objId)),
				collectionName);
	}

	@Override
	public String getCollectionName() {

		return null;
	}

}