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
	//@Autowired
	//private BaseService baseService;
	
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

	public void resendfailedJms(ObjectId id) {
		FailedJms failedJms=mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), FailedJms.class);
		Map<String, String> map=failedJms.getProperties();
		String type=map.get("type");
		 jmsService.sendObject(failedJms.getDestination(), failedJms.getMessage(), "type",type);
		 mongoTemplate.remove(failedJms);
	}
	
	private List<FailedJmsDto> processFailedJms(List<FailedJms> list){
		List<FailedJmsDto> result=new ArrayList<FailedJmsDto>();
		for (FailedJms jms:list){
			FailedJmsDto failedJmsDto=new FailedJmsDto();
			failedJmsDto.setFailedTime(jms.getFailedTime());
			failedJmsDto.setId(jms.getId());
			failedJmsDto.setProperties(jms.getProperties());
			failedJmsDto.setDestination(jms.getDestination().toString());
			failedJmsDto.setMessage(jms.getMessage());
			result.add(failedJmsDto);
		}
		return result;
	}

	public void groupResendfailedJms(String ids) {
		List<ObjectId> list=new ArrayList<ObjectId>();
		String[] failedJmsIds=ids.split(",");
		for(int i=0;i<failedJmsIds.length;i++){
			ObjectId obj=new ObjectId(failedJmsIds[i]);
			list.add(obj);
		}
		List<FailedJms> failedJmss=mongoTemplate.find(new Query(Criteria.where("_id").in(list)), FailedJms.class);
		for(FailedJms failedJms:failedJmss){
			resendfailedJms(failedJms.getId());
		}
	}

}
