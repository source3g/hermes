package com.source3g.hermes.entity.customer;

import java.util.Date;

public class CallRecord {
	private Date callTime;
	private int callDuration;//通话时长
	private boolean newCustomer;//来电时是否为新顾客
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
	public boolean isNewCustomer() {
		return newCustomer;
	}
	public void setNewCustomer(boolean newCustomer) {
		this.newCustomer = newCustomer;
	}
}
