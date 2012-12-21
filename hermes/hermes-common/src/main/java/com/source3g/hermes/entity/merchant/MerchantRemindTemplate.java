package com.source3g.hermes.entity.merchant;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.source3g.hermes.entity.AbstractEntity;

public class MerchantRemindTemplate extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5210818040577743106L;
	@DBRef
	private RemindTemplate remindTemplate;
	private String messageContent;

	public RemindTemplate getRemindTemplate() {
		return remindTemplate;
	}

	public void setRemindTemplate(RemindTemplate remindTemplate) {
		this.remindTemplate = remindTemplate;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		MerchantRemindTemplate merchantRemindTemplate = (MerchantRemindTemplate) obj;
		if (this.getId() == null || merchantRemindTemplate.getId() == null) {
			return false;
		}
		return this.getId().equals(merchantRemindTemplate.getId());
	}

	@Override
	public int hashCode() {
		if (this.getId() != null) {
			return this.getId().hashCode();
		}
		return super.hashCode();
	}

}
