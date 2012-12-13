package com.source3g.hermes.device.service;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.Device;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.utils.Page;

@Service
public class DeviceService extends BaseService {

	public void add(Device device) throws Exception{
		List<Device> list=mongoTemplate.find(new Query(Criteria.where("account").is(device.getSn())), Device.class);
		if(list.size()==0){
			mongoTemplate.insert(device);
		}
		throw new Exception("账号已存在");
	}
	public boolean snValidate(String sn) {
		List<Device> list=mongoTemplate.find(new Query(Criteria.where("sn").is(sn)), Device.class);
		if(list.size()==0){
			return true;
		}
		return false;
}
	public Page list(int pageNo, Device device) {
		Query query = new Query();
		if (StringUtils.isNotEmpty(device.getSn())) {
			Pattern pattern = Pattern.compile("^.*" + device.getSn() + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("sn").is(pattern));
		}
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, Device.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNo);
		List<Device> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), Device.class);
		page.setData(list);
		return page;
	}

	public void deleteById(String id) {
		super.deleteById(id, Device.class);
	}

	public List<Device> findByIds(List<String> ids) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(ids));
		return mongoTemplate.find(query, Device.class);
	}

	public Device findBySn(String sn) {
		return mongoTemplate.findOne(new Query(Criteria.where("sn").is(sn)), Device.class);
	}

	public Device findById(String id) {
		Device device = mongoTemplate.findById(new ObjectId(id), Device.class);
		return device;
	}

	public void update(Device device) {
		mongoTemplate.save(device);
	}

	public Device findBySimId(ObjectId simId) {
		return mongoTemplate.findOne(new Query(Criteria.where("simId").is(simId)), Device.class);
	}
}
