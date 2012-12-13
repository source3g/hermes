package com.source3g.hermes.entity.merchant;

public class Setting {
	private boolean autoSend = false;
	private boolean nameMatch = false;
	private boolean salerMatch = false;
	private boolean birthdayRemind = false;

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
}
