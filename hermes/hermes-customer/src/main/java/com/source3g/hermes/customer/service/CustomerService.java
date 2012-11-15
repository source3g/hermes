package com.source3g.hermes.customer.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.Device;
import com.source3g.hermes.entity.customer.CallRecord;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.utils.CollectionNameConstant;
import com.source3g.hermes.utils.Page;

@Service
public class CustomerService extends BaseService {

	private String collectionName = "customer";

	public Customer add(Customer customer) {
		customer.setId(ObjectId.get());
		mongoTemplate.insert(customer, collectionName);
		return customer;
	}

	public List<Customer> listAll() {
		return mongoTemplate.findAll(Customer.class, collectionName);
	}

	public Page list(int pageNo, Customer customer) {
		if (customer.getMerchantId() == null) {
			return null;
		}
		Query query = new Query();
		if (StringUtils.isNotEmpty(customer.getName())) {
			Pattern pattern = Pattern.compile("^.*" + customer.getName() + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("name").is(pattern));
		}
		if (StringUtils.isNotEmpty(customer.getPhone())) {
			Pattern pattern = Pattern.compile("^.*" + customer.getPhone() + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("phone").is(pattern));
		}

		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, collectionName);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNo);
		List<Customer> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), Customer.class, collectionName);
		page.setData(list);
		return page;
	}

	@Override
	public String getCollectionName() {
		return collectionName;
	}

	public Customer get(String id) {
		return mongoTemplate.findById(new ObjectId(id), Customer.class, collectionName);
	}

	public void callIn(String deviceSn, String phone, String time, String duration) throws Exception {
		Device device = mongoTemplate.findOne(new Query(Criteria.where("sn").is(deviceSn)), Device.class, CollectionNameConstant.DEVICE);
		if (device == null) {
			throw new Exception("盒子编号不存在");
		}
		Query findMerchantByDeviceSn = new Query();
		findMerchantByDeviceSn.addCriteria(Criteria.where(device.getId()).in("deviceIds"));
		Merchant merchant = mongoTemplate.findOne(findMerchantByDeviceSn, Merchant.class, CollectionNameConstant.MERCHANT);
		if (merchant == null) {
			throw new Exception("盒子所属商户不存在");
		}
		Customer customer = mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchant.getId())), Customer.class, collectionName);
		if (customer == null) {
			customer = new Customer();
			customer.setId(ObjectId.get());
		}

		List<CallRecord> records = customer.getCallRecords();
		if (records == null) {
			records = new ArrayList<CallRecord>();
		}
		CallRecord record = new CallRecord();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			record.setCallTime(dateFormat.parse(time));
		} catch (Exception e) {
			throw new Exception("接听时间格式不正确");
		}
		record.setCallDuration(Integer.parseInt(duration));
		records.add(record);
		customer.setCallRecords(records);
		mongoTemplate.save(customer, collectionName);
	}
}
