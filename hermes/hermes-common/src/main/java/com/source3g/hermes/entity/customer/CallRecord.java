package com.source3g.hermes.entity.customer;

import java.util.Date;

import com.source3g.hermes.enums.CallStatus;

public class CallRecord {
	private Date callTime;
	private int callDuration;//通话时长
	private boolean newCustomer;//来电时是否为新顾客
	private CallStatus callStatus;
	

	public CallStatus getCallStatus() {
		return callStatus;
	}
	public void setCallStatus(CallStatus callStatus) {
		this.callStatus = callStatus;
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
	public boolean isNewCustomer() {
		return newCustomer;
	}
	public void setNewCustomer(boolean newCustomer) {
		this.newCustomer = newCustomer;
	}
	
}
