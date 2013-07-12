package com.source3g.hermes.entity.merchant;

import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class MerchantRemindTemplate extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5210818040577743106L;
	@NotNull(message = "标题不能为空")
	private String title;
	@NotNull(message = "提醒不能为空")
	private Integer advancedTime;
	@NotNull(message = "提醒内容不能为空")
	private String messageContent;
	
	private ObjectId merchantId;
	private Boolean isDelete = false;

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public ObjectId getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(ObjectId merchantId) {
		this.merchantId = merchantId;
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

	public Integer getAdvancedTime() {
		return advancedTime;
	}

	public void setAdvancedTime(Integer advancedTime) {
		this.advancedTime = advancedTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
