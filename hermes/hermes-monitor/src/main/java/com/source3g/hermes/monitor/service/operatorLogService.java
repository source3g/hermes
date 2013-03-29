package com.source3g.hermes.monitor.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.log.OperatorLog;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.utils.Page;
@Service
public class operatorLogService  extends BaseService  {

	public Page findAll(String pageNo,String startTime,String endTime) throws ParseException {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		int pageNoInt = Integer.valueOf(pageNo);
		Query query = new Query();
		query.with(new Sort(Direction.DESC, "_id"));
		Date stTime=null;
		Date edTime=null;
		if(startTime!=null&& endTime!=null){
			 stTime = fmt.parse(startTime);
			 edTime = fmt.parse(endTime);
			query.addCriteria(Criteria.where("operateTime").gte(stTime).lte(edTime));
		}else if(startTime!=null){
			 stTime = fmt.parse(startTime);
			query.addCriteria(Criteria.where("operateTime").gte(stTime));
		}else if(endTime != null){
			 edTime = fmt.parse(endTime);
			query.addCriteria(Criteria.where("operateTime").lte(edTime));
		}
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, OperatorLog.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNoInt);
		List<OperatorLog> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), OperatorLog.class);
		page.setData(list);
		return page;
	}

}