package com.source3g.hermes.dto.customer;

import java.util.Date;

public class CallRecordDto implements Comparable<CallRecordDto> {

	private String customerName;
	private String phone;
	private Date callTime;
	private int callDuration;// 通话时长

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCallTime() {
		return callTime;
	}

	public void setCallTime(Date callTime) {
		this.callTime = callTime;
	}

	public int getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(int callDuration) {
		this.callDuration = callDuration;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Override
	public int compareTo(CallRecordDto o) {
		if (o.getCallTime() == null) {
			return 0;
		}
		if (this.getCallTime().getTime() > o.getCallTime().getTime()) {
			return -1;
		} else {
			return 1;
		}
	}
}
