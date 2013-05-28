package com.source3g.hermes.entity.customer;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;
import com.source3g.hermes.enums.Sex;

@Document
public class CustomerImportItem extends AbstractEntity {
	private static final long serialVersionUID = -1666811215606533985L;

	//@DBRef
	private CustomerImportLog importLog;
	
	private String name;
	private Sex sex;
	private String birthday;
	
	@NotEmpty(message="电话号码不能为空")
	@Length(max=11,min=11,message="{电话号码位数不对}")
	private String phone;
	
	private String address;
	private String qq;
	private String email;
	private String note;
	private ObjectId merchantId;

	private String customerGroupName;
	private ObjectId customerImportLogId;
	
	private String importStatus;//导入状态
	private String failedReason;//失败原因

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
		return importStatus;
	}

	public void setImportStatus(String importStatus) {
		this.importStatus = importStatus;
	}

	public String getCustomerGroupName() {
		return customerGroupName;
	}

	public void setCustomerGroupName(String customerGroupName) {
		this.customerGroupName = customerGroupName;
	}

	public ObjectId getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(ObjectId merchantId) {
		this.merchantId = merchantId;
	}

	public ObjectId getCustomerImportLogId() {
		return customerImportLogId;
	}

	public void setCustomerImportLogId(ObjectId customerImportLogId) {
		this.customerImportLogId = customerImportLogId;
	}

	public String getFailedReason() {
		return failedReason;
	}

	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}
}
