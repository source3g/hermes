package com.source3g.hermes.entity.customer;

import java.util.Date;

public class CallRecord {
	private Date callTime;
	private int callDuration;//通话时长
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
}
