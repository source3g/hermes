package com.source3g.hermes.customer.dto;

import java.util.Date;

public class RemindDto {
	private String  name;
	private Date remindTime;
	private String advancedTime;// 提前提醒的天数
	private boolean alreadyRemind;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getRemindTime() {
		return remindTime;
	}
	public void setRemindTime(Date remindTime) {
		this.remindTime = remindTime;
	}
	public String getAdvancedTime() {
		return advancedTime;
	}
	public void setAdvancedTime(String advancedTime) {
		this.advancedTime = advancedTime;
	}
	public boolean isAlreadyRemind() {
		return alreadyRemind;
	}
	public void setAlreadyRemind(boolean alreadyRemind) {
		this.alreadyRemind = alreadyRemind;
	}
}
