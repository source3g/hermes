package com.source3g.hermes.customer.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.source3g.hermes.constants.CollectionNameConstant;
import com.source3g.hermes.constants.JmsConstants;
import com.source3g.hermes.entity.AbstractEntity;
import com.source3g.hermes.entity.Device;
import com.source3g.hermes.entity.customer.CallRecord;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.utils.Page;

@Service
public class CustomerService extends BaseService {

	@Autowired
	private JmsTemplate jmsTemplate;

	private String collectionName = CollectionNameConstant.CUSTOMER;

	public Customer add(Customer customer) {
		customer.setId(ObjectId.get());
		mongoTemplate.insert(customer, collectionName);
		return customer;
	}

	@Override
	public <T extends AbstractEntity> void updateExcludeProperties(T entity, String... properties) {
		super.updateExcludeProperties(entity, properties);
		final String id = entity.getId().toString();
		jmsTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage createTextMessage = session.createTextMessage();
				createTextMessage.setText(id);
				createTextMessage.setStringProperty(JmsConstants.MESSAGE_TYPE, JmsConstants.UPDATE_CUSTOMER);
				return createTextMessage;
			}
		});
	}

	public List<Customer> listAll() {
		return mongoTemplate.findAll(Customer.class, collectionName);
	}

	public Page list(int pageNo, Customer customer, boolean isNew) {
		Query query = new Query();
		if (customer.getMerchantId() == null) {
			return null;
		} else {
			query.addCriteria(Criteria.where("merchantId").is(customer.getMerchantId()));
		}

		if (isNew == true) {
			query.addCriteria(Criteria.where("name").is(null));
		} else if (StringUtils.isNotEmpty(customer.getName())) {
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

	public Page list(int pageNo, Customer customer) {
		return list(pageNo, customer, false);
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
		findMerchantByDeviceSn.addCriteria(Criteria.where("deviceIds").is(device.getId()));
		Merchant merchant = mongoTemplate.findOne(findMerchantByDeviceSn, Merchant.class, CollectionNameConstant.MERCHANT);
		if (merchant == null) {
			throw new Exception("盒子所属商户不存在");
		}
		CallRecord record = new CallRecord();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date callInTime = null;
		try {
			callInTime = dateFormat.parse(time);
		} catch (Exception e) {
			throw new Exception("接听时间格式不正确");
		}
		record.setCallTime(callInTime);
		record.setCallDuration(Integer.parseInt(duration));
		Update update = new Update();
		update.set("phone", phone).set("merchantId", merchant.getId()).set("lastCallInTime", callInTime).addToSet("callRecords", record);
		mongoTemplate.upsert(new Query(Criteria.where("merchantId").is(merchant.getId()).and("phone").is(phone)), update, collectionName);
	}
}
