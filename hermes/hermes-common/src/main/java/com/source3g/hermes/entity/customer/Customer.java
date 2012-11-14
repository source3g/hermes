package com.source3g.hermes.entity.customer;

import java.util.List;

import com.source3g.hermes.entity.AbstractEntity;
import com.source3g.hermes.enums.Sex;

public class Customer extends AbstractEntity {
	private String name;
	private Sex sex;
	private String birthday;
	private String phone;
	private String customerId;
	private boolean blackList;
	private String address;
	private List<String> otherPhones;
	private String qq;
	private String email;
	private String note;
	
	private List<CallRecord> callRecords;
	private List<Remind> reminds;
	private String merchantId;

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

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
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
}
