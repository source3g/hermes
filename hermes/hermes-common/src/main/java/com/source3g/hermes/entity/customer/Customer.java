package com.source3g.hermes.entity.customer;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;
import com.source3g.hermes.enums.Sex;

@Document
@CompoundIndexes({ @CompoundIndex(name = "merchant_phone", def = "{'phone': 1, 'merchantId': -1}", unique = true) })
public class Customer extends AbstractEntity {
	private static final long serialVersionUID = 6014996097125743375L;
	private String name;
	private Sex sex;
	private String birthday;
	private String phone;
	private boolean blackList;
	private String address;
	private List<String> otherPhones;
	private String qq;
	private String email;
	private String note;

	private List<CallRecord> callRecords;

	private List<Remind> reminds;
	private ObjectId merchantId;
	private Date lastCallInTime; // 最后通电话时间
	private ObjectId customerGroupId;
	private Date operateTime;

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isBlackList() {
		return blackList;
	}

	public void setBlackList(boolean blackList) {
		this.blackList = blackList;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<String> getOtherPhones() {
		return otherPhones;
	}

	public void setOtherPhones(List<String> otherPhones) {
		this.otherPhones = otherPhones;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ObjectId getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(ObjectId merchantId) {
		this.merchantId = merchantId;
	}

	public List<Remind> getReminds() {
		return reminds;
	}

	public void setReminds(List<Remind> reminds) {
		this.reminds = reminds;
	}

	public List<CallRecord> getCallRecords() {
		return callRecords;
	}

	public void setCallRecords(List<CallRecord> callRecords) {
		this.callRecords = callRecords;
	}

	public Date getLastCallInTime() {
		return lastCallInTime;
	}

	public void setLastCallInTime(Date lastCallInTime) {
		this.lastCallInTime = lastCallInTime;
	}

	public ObjectId getCustomerGroupId() {
		return customerGroupId;
	}

	public void setCustomerGroupId(ObjectId customerGroupId) {
		this.customerGroupId = customerGroupId;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String toInsertOrUpdateSql() throws JsonGenerationException, JsonMappingException, IOException {
// 	FilterProvider filterProvider = new SimpleFilterProvider().addFilter("filterPropreties", SimpleBeanPropertyFilter.serializeAllExcept("otherPhones", "callRecords", "operateTime"));
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("otherPhones", "callRecords", "operateTime");
		FilterProvider fp = new SimpleFilterProvider().addFilter("onlyAFilter", filter);
		ObjectMapper objectMapper = new ObjectMapper();
		String strJson = objectMapper.filteredWriter(fp).writeValueAsString(this);
		return "REPLACE INTO CUSTOMER (phone,context) values(" + phone + "," + strJson + "); ";
	}

	public String toDeleteSql() {
		return "delete from  CUSTOMER where phone=" + phone;
	}

}
