package com.source3g.hermes.device.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.source3g.hermes.dto.sync.DeviceStatusDto;
import com.source3g.hermes.entity.Device;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.sync.DeviceStatus;
import com.source3g.hermes.entity.sync.TaskPackage;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.utils.GpsPoint;
import com.source3g.hermes.utils.Page;
import com.source3g.hermes.vo.DeviceVo;

@Service
public class DeviceService extends BaseService {

	public void add(Device device) throws Exception {
		List<Device> list = mongoTemplate.find(new Query(Criteria.where("account").is(device.getSn())), Device.class);
		if (list.size() == 0) {
			mongoTemplate.insert(device);
		} else {
			throw new Exception("账号已存在");
		}

	}

	public boolean snValidate(String sn) {
		List<Device> list = mongoTemplate.find(new Query(Criteria.where("sn").is(sn)), Device.class);
		if (list.size() == 0) {
			return true;
		}
		return false;
	}

	public Page list(int pageNo, Device device) {
		Query query = new Query();
		query.with(new Sort(Direction.DESC, "_id"));
		if (StringUtils.isNotEmpty(device.getSn())) {
			Pattern pattern = Pattern.compile("^.*" + device.getSn() + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("sn").is(pattern));
		}
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, Device.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNo);
		List<Device> devices = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), Device.class);
		List<DeviceVo> deviceVosList = new ArrayList<DeviceVo>();
		for (Device d : devices) {
			DeviceVo deviceVo = new DeviceVo();
			deviceVo.setDevice(d);
			Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("deviceIds").is(d.getId())), Merchant.class);
			if (merchant != null) {
				deviceVo.setMerchant(merchant);
			}
			deviceVosList.add(deviceVo);
		}
		page.setData(deviceVosList);
		return page;
	}

	public void deleteById(String id) throws Exception {
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("deviceIds").is(new ObjectId(id))), Merchant.class);
		if (merchant != null) {
			throw new Exception("该盒子已被绑定");
		}
		super.deleteById(id, Device.class);
	}

	public List<Device> findByIds(List<String> ids) {
		List<ObjectId> list = new ArrayList<ObjectId>();
		Query query = new Query();
		for (String id : ids) {
			list.add(new ObjectId(id));
		}
		query.addCriteria(Criteria.where("_id").in(list));
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

	public void updateGpsPoint(String sn, GpsPoint gpsPoint) {
		Update update = new Update();
		update.set("gpsPoint", gpsPoint);
		mongoTemplate.updateFirst(new Query(Criteria.where("sn").is(sn)), update, Device.class);
	}

	public List<DeviceStatusDto> findDeviceStatusByMerchantId(String merchantId) {
		List<DeviceStatusDto> result = new ArrayList<DeviceStatusDto>();
		Merchant merchant = mongoTemplate.findById(new ObjectId(merchantId), Merchant.class);
		List<ObjectId> ids = merchant.getDeviceIds();
		if (CollectionUtils.isEmpty(ids)) {
			return null;
		}
		List<Device> devices = mongoTemplate.find(new Query(Criteria.where("_id").in(ids)), Device.class);
		for (Device d : devices) {
			DeviceStatusDto deviceStatusDto = new DeviceStatusDto();
			deviceStatusDto.setSn(d.getSn());
			DeviceStatus deviceStatus = mongoTemplate.findOne(new Query(Criteria.where("deviceSn").is(d.getSn())), DeviceStatus.class);
			if (deviceStatus != null) {
				deviceStatusDto.setLastTaskId(deviceStatus.getLastTaskId());
				deviceStatusDto.setLastUpdateTime(deviceStatus.getLastUpdateTime());
				deviceStatusDto.setRequestTaskId(deviceStatus.getRequestTaskId());
				Long restTaskCount = mongoTemplate.count(new Query(Criteria.where("taskId").gt(deviceStatus.getLastTaskId()).and("merchantId").is(new ObjectId(merchantId))), TaskPackage.class);
				deviceStatusDto.setRestTaskCount(restTaskCount);
			}
			result.add(deviceStatusDto);
		}
		return result;
	}

}
