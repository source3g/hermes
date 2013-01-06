package com.source3g.hermes.dto.customer;

import java.util.Date;

public class NewCustomerDto {
	private String phone;
	private Date lastCallTime;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getLastCallTime() {
		return lastCallTime;
	}
	public void setLastCallTime(Date lastCallTime) {
		this.lastCallTime = lastCallTime;
	}
	
	

}
