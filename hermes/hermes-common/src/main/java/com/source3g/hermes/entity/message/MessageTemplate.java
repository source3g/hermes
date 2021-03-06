package com.source3g.hermes.entity.message;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class MessageTemplate extends AbstractEntity {

	private static final long serialVersionUID = -2717722978097843802L;
	@NotEmpty(message="{message.template.title.not.empty}")
	private String title;
	@NotEmpty(message="{message.template.content.not.empty}")
	private String content;
	
	private ObjectId merchantId;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ObjectId getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(ObjectId merchantId) {
		this.merchantId = merchantId;
	}

}
