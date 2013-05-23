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

import com.source3g.hermes.constants.TaskConstants;
import com.source3g.hermes.dto.sync.DeviceStatusDto;
import com.source3g.hermes.entity.device.Device;
import com.source3g.hermes.entity.device.PublicKey;
import com.source3g.hermes.entity.device.SimChangeRecord;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.sim.SimInfo;
import com.source3g.hermes.entity.sync.DeviceStatus;
import com.source3g.hermes.entity.sync.TaskPackage;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.utils.GpsPoint;
import com.source3g.hermes.utils.MD5;
import com.source3g.hermes.utils.Page;
import com.source3g.hermes.vo.DeviceDistributionVo;
import com.sourse3g.hermes.branch.BranchCompany;
import com.sourse3g.hermes.branch.Saler;

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
		page.setData(devices);
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
				deviceStatusDto.setLastAskTime(deviceStatus.getLastAskTime());
				Long restTaskCount = 0L;
				if (TaskConstants.INIT.equals(deviceStatus.getStatus())) {
					TaskPackage lastAllPackage = mongoTemplate.findOne(new Query(Criteria.where("taskId").gt(deviceStatus.getLastTaskId()).and("merchantId").is(new ObjectId(merchantId)).and("type").is(TaskConstants.ALL_PACKAGE)), TaskPackage.class);
					if (lastAllPackage != null) {
						restTaskCount = mongoTemplate.count(new Query(Criteria.where("taskId").gte(lastAllPackage.getTaskId()).and("merchantId").is(new ObjectId(merchantId))), TaskPackage.class);
					} else {
						restTaskCount = 0L;
					}
				} else {
					restTaskCount = mongoTemplate.count(new Query(Criteria.where("taskId").gt(deviceStatus.getLastTaskId()).and("merchantId").is(new ObjectId(merchantId)).and("type").is(TaskConstants.INCREMENT_PACKAGE)), TaskPackage.class);
				}
				deviceStatusDto.setRestTaskCount(restTaskCount);
			}
			result.add(deviceStatusDto);
		}
		return result;
	}

	public List<DeviceDistributionVo> findDeviceDistribution() {
		List<DeviceDistributionVo> result = new ArrayList<DeviceDistributionVo>();
		List<Device> devices = mongoTemplate.find(new Query(Criteria.where("gpsPoint").ne(null)), Device.class);
		for (Device device : devices) {
			DeviceDistributionVo deviceDistributionVo = new DeviceDistributionVo();
			deviceDistributionVo.setDevice(device);
			try {
				Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("deviceIds").is(device.getId())), Merchant.class);
				deviceDistributionVo.setMerchantName(merchant.getName());
				Saler saler = mongoTemplate.findOne(new Query(Criteria.where("_id").is(merchant.getSalerId())), Saler.class);
				deviceDistributionVo.setSalerName(saler.getName());
				BranchCompany branchCompany = mongoTemplate.findOne(new Query(Criteria.where("_id").is(saler.getBranchCompanyId())), BranchCompany.class);
				deviceDistributionVo.setBranchCompanyName(branchCompany.getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			result.add(deviceDistributionVo);
		}
		return result;
	}

	public PublicKey getPublicKey() {
		return super.findOne(new Query(), PublicKey.class);
	}

	public Boolean validateSecret(String sn, String secret) {
		PublicKey publicKey = getPublicKey();
		String secretResult = MD5.md5(getSecret(sn, publicKey.getPublickey()));
		if (secretResult.equals(secret)) {
			return true;
		}
		return false;
	}

	private static String getSecret(String sn, String publicKey) {
		char[] snBytes = sn.toCharArray();
		for (int i = 0; i < snBytes.length; i++) {
			char a = snBytes[i];
			a = (char) (a + 10);
			snBytes[i] = a;
		}
		char[] keyBytes = publicKey.toCharArray();
		for (int i = 0; i < keyBytes.length; i++) {
			char b = keyBytes[i];
			b = (char) (b - 10);
			keyBytes[i] = b;
		}
		return new String(snBytes) + new String(keyBytes);
	}

	@SuppressWarnings("unused")
	private static String getSecret1(String sn, String publicKey) {
		char[] snBytes = sn.toCharArray();
		for (int i = 0; i < snBytes.length; i++) {
			char a = snBytes[i];
			a = (char) (a - 10);
			snBytes[i] = a;
		}
		char[] keyBytes = publicKey.toCharArray();
		for (int i = 0; i < keyBytes.length; i++) {
			char b = keyBytes[i];
			b = (char) (b + 10);
			keyBytes[i] = b;
		}
		return new String(snBytes) + new String(keyBytes);
	}

	public static void main(String[] args) {
		String s = getSecret("abc-dbc-ff", "aabb1112222-333");
		System.out.println(s);
		// System.out.println(getSecret1(s.split("\\|")[0], s.split("\\|")[1]));
	}

	public void updateImsiNo(String sn, String imsiNo) {
		SimInfo newSimInfo = mongoTemplate.findOne(new Query(Criteria.where("imsiNo").is(imsiNo)), SimInfo.class);
		if (newSimInfo == null) {
			return;
		}
		Device device = mongoTemplate.findOne(new Query(Criteria.where("sn").is(sn)), Device.class);
		SimInfo oldSimInfo = device.getSimInfo();
		if (oldSimInfo != null) {
			if (!oldSimInfo.getImsiNo().equals(imsiNo)) {
				SimChangeRecord simChangeRecord = new SimChangeRecord(oldSimInfo, newSimInfo);
				List<SimChangeRecord> records = device.getSimChangeRecords();
				records.add(simChangeRecord);
				mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(device.getId())), new Update().set("simInfo", newSimInfo).set("simChangeRecords", records), Device.class);
			}
		} else {
			mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(device.getId())), new Update().set("simInfo", newSimInfo), Device.class);
		}
	}
}
