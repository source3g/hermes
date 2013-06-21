package com.source3g.hermes.entity.merchant;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class Setting implements Serializable {
	private static final long serialVersionUID = -1777068155348389553L;
	private boolean autoSend = false;
	private boolean nameMatch = false;
	private boolean title=true;
	private boolean salerMatch = false;
	private boolean birthdayRemind = false;
	private int birthdayRemindAdvancedTime=0;
	//生日提醒模板
	@DBRef
	private MerchantRemindTemplate birthdayRemindTemplate;

	public boolean isAutoSend() {
		return autoSend;
	}

	public void setAutoSend(boolean autoSend) {
		this.autoSend = autoSend;
	}

	public boolean isNameMatch() {
		return nameMatch;
	}

	public void setNameMatch(boolean nameMatch) {
		this.nameMatch = nameMatch;
	}

	public boolean isSalerMatch() {
		return salerMatch;
	}

	public void setSalerMatch(boolean salerMatch) {
		this.salerMatch = salerMatch;
	}

	public boolean isBirthdayRemind() {
		return birthdayRemind;
	}

	public void setBirthdayRemind(boolean birthdayRemind) {
		this.birthdayRemind = birthdayRemind;
	}

	public int getBirthdayRemindAdvancedTime() {
		return birthdayRemindAdvancedTime;
	}

	public void setBirthdayRemindAdvancedTime(int birthdayRemindAdvancedTime) {
		this.birthdayRemindAdvancedTime = birthdayRemindAdvancedTime;
	}

	public MerchantRemindTemplate getBirthdayRemindTemplate() {
		return birthdayRemindTemplate;
	}

	public void setBirthdayRemindTemplate(MerchantRemindTemplate birthdayRemindTemplate) {
		this.birthdayRemindTemplate = birthdayRemindTemplate;
	}

	public boolean isTitle() {
		return title;
	}

	public void setTitle(boolean title) {
		this.title = title;
	}


}
