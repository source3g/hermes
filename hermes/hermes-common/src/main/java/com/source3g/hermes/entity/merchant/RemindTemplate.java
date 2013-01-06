package com.source3g.hermes.entity.merchant;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class RemindTemplate extends AbstractEntity {
	private static final long serialVersionUID = -3835632016914035198L;
	
	private int advancedTime;
	@NotEmpty(message="{remind.template.title.not.null}")
	private String title;
	@NotEmpty(message="{remind.template.content.not.null}")
	private String messageContent;
	private Boolean isDelete=false;
	
	public Boolean getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}
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
