package com.source3g.hermes.monitor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.dto.jms.FailedJmsDto;
import com.source3g.hermes.entity.log.FailedJms;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.service.JmsService;
import com.source3g.hermes.utils.Page;

@Service
public class FailedJmsService extends BaseService {
	@Autowired
	private JmsService jmsService;

	// @Autowired
	// private BaseService baseService;

	public Page findAll(String pageNo) {
		int pageNoInt = Integer.valueOf(pageNo);
		Query query = new Query();
		query.with(new Sort(Direction.DESC, "_id"));
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, FailedJms.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNoInt);
		List<FailedJms> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), FailedJms.class);
		page.setData(processFailedJms(list));
		return page;
	}

	public Boolean resendfailedJms(ObjectId id) {
		Boolean result = true;
		FailedJms failedJms = mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), FailedJms.class);
		Map<String, String> map = failedJms.getProperties();
		String type = map.get("type");
		try {
			jmsService.reSendObject(failedJms.getDestination(), failedJms.getMessage(), "type", type);
			mongoTemplate.remove(failedJms);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	private List<FailedJmsDto> processFailedJms(List<FailedJms> list) {
		List<FailedJmsDto> result = new ArrayList<FailedJmsDto>();
		for (FailedJms jms : list) {
			FailedJmsDto failedJmsDto = new FailedJmsDto();
			failedJmsDto.setFailedTime(jms.getFailedTime());
			failedJmsDto.setId(jms.getId());
			failedJmsDto.setProperties(jms.getProperties());
			failedJmsDto.setDestination(jms.getDestination().toString());
			failedJmsDto.setMessage(jms.getMessage());
			result.add(failedJmsDto);
			System.out.println("`213");
		}
		return result;
	}

	public Boolean groupResendfailedJms() {
		List<FailedJms> failedJmss = mongoTemplate.findAll(FailedJms.class);
		for (FailedJms failedJms : failedJmss) {
			resendfailedJms(failedJms.getId());
		}
		return true;
	}

}
