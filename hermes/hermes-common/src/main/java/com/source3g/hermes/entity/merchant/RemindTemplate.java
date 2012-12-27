package com.source3g.hermes.entity.merchant;

import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class RemindTemplate extends AbstractEntity {
	private static final long serialVersionUID = -3835632016914035198L;
	
	private int advancedTime;
	private String title;
	private String messageContent;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	public int getAdvancedTime() {
		return advancedTime;
	}
	public void setAdvancedTime(int advancedTime) {
		this.advancedTime = advancedTime;
	}

}
