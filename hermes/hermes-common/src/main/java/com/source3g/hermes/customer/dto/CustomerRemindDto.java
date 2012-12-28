package com.source3g.hermes.customer.dto;

import java.util.Date;

import com.source3g.hermes.entity.merchant.MerchantRemindTemplate;

public class CustomerRemindDto {
	
	private String customerName;
	private String phone;
	private Date remindTime;
	private int advancedTime;
	private String title;
	private String content;
	private MerchantRemindTemplate merchantRemindTemplate;
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
	public MerchantRemindTemplate getMerchantRemindTemplate() {
		return merchantRemindTemplate;
	}
	public void setMerchantRemindTemplate(MerchantRemindTemplate merchantRemindTemplate) {
		this.merchantRemindTemplate = merchantRemindTemplate;
	}
	public Date getRemindTime() {
		return remindTime;
	}
	public void setRemindTime(Date remindTime) {
		this.remindTime = remindTime;
	}
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

}
