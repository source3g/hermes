package com.source3g.hermes.message.service;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.merchant.MerchantGroup;
import com.source3g.hermes.service.BaseService;

@Service
public class MessageService extends BaseService{

	public void messageSend(String name,String messageInfo) {
		Query query = new Query();
		if (StringUtils.isNotEmpty(name)) {

			Pattern pattern = Pattern.compile("^.*" + name+ ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("name").is(pattern));
		}
		List<MerchantGroup> list = mongoTemplate.find(query, MerchantGroup.class);
		for(int i=0;i<list.size();i++){
			System.out.print("已向"+list.get(i).getName()+"商户组发送"+messageInfo);
		}
	}
	

}
