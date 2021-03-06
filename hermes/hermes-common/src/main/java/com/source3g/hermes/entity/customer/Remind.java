package com.source3g.hermes.entity.customer;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.source3g.hermes.entity.merchant.MerchantRemindTemplate;

public class Remind {
	@DBRef
	private MerchantRemindTemplate merchantRemindTemplate;
	private Date remindTime;
	private boolean alreadyRemind;


	public Date getRemindTime() {
		return remindTime;
	}
	public MerchantRemindTemplate getMerchantRemindTemplate() {
		return merchantRemindTemplate;
	}
	public void setMerchantRemindTemplate(MerchantRemindTemplate merchantRemindTemplate) {
		this.merchantRemindTemplate = merchantRemindTemplate;
	}
	public void setRemindTime(Date remindTime) {
		this.remindTime = remindTime;
	}

	public boolean isAlreadyRemind() {
		return alreadyRemind;
	}

	public void setAlreadyRemind(boolean alreadyRemind) {
		this.alreadyRemind = alreadyRemind;
	}
}