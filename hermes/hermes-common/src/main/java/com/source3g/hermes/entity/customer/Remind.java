package com.source3g.hermes.entity.customer;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.source3g.hermes.entity.merchant.RemindTemplate;

public class Remind {
	@DBRef
	private RemindTemplate remindTemplate;
	private Date remindTime;
	private String advancedTime;// 提前提醒的天数
	private boolean alreadyRemind;


	public Date getRemindTime() {
		return remindTime;
	}

	public RemindTemplate getRemindTemplate() {
		return remindTemplate;
	}

	public void setRemindTemplate(RemindTemplate remindTemplate) {
		this.remindTemplate = remindTemplate;
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