package com.source3g.hermes.entity.customer;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.enums.Sex;

@Document
public class CustomerImportItem {
	@DBRef
	private CustomerImportLog importLog;

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
	private String ImportStatus;

	public CustomerImportLog getImportLog() {
		return importLog;
	}

	public void setImportLog(CustomerImportLog importLog) {
		this.importLog = importLog;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

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

	public String getImportStatus() {
		return ImportStatus;
	}

	public void setImportStatus(String importStatus) {
		ImportStatus = importStatus;
	}
}
