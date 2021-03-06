package com.source3g.hermes.dto.customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;


public class CustomerRemindDto {

	private List<CustomerInfo> customers=new ArrayList<CustomerInfo>();
	
	private int advancedTime;
	private String title;
	private String content;
	
	private ObjectId merchantRemindId;


	public int getAdvancedTime() {
		return advancedTime;
	}
	public void setAdvancedTime(int advancedTime) {
		this.advancedTime = advancedTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public List<CustomerInfo> getCustomers() {
		return customers;
	}
	public void setCustomers(List<CustomerInfo> customers) {
		this.customers = customers;
	}
	@JsonIgnore
	public void addRemind(CustomerInfo customerInfo)
	{
		if(customerInfo==null){
			return;
		}
		customers.add(customerInfo);
	}

	public ObjectId getMerchantRemindId() {
		return merchantRemindId;
	}
	public void setMerchantRemindId(ObjectId merchantRemindId) {
		this.merchantRemindId = merchantRemindId;
	}

	public static class CustomerInfo{
		public CustomerInfo() {
			super();
		}
		public CustomerInfo(String customerName, String phone, Date remindTime) {
			super();
			this.customerName = customerName;
			this.phone = phone;
			this.remindTime = remindTime;
		}
		private String customerName;
		private String phone;
		private Date remindTime;
		
		public Date getRemindTime() {
			return remindTime;
		}
		public void setRemindTime(Date remindTime) {
			this.remindTime = remindTime;
		}
		public String getCustomerName() {
			return customerName;
		}
		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
	}
	
}
