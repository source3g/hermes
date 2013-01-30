package com.source3g.hermes.monitor.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.log.FailedMessage;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.utils.Page;

@Service
public class FailedMessageService extends BaseService {

	public Page findAll(String pageNo) {
		int pageNoInt = Integer.valueOf(pageNo);
		Query query = new Query();
		query.with(new Sort(Direction.DESC, "_id"));
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, FailedMessage.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNoInt);
		List<FailedMessage> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), FailedMessage.class);
		page.setData(list);
		return page;
	}

}
