package com.source3g.hermes.entity.message;

import org.bson.types.ObjectId;

import com.source3g.hermes.entity.AbstractEntity;

public class MessageAutoSend extends AbstractEntity {

	private static final long serialVersionUID = -3079593257726925539L;
	private String newMessageCotent;
	private String oldMessageCotent;
	private ObjectId merchantId;

	public String getNewMessageCotent() {
		return newMessageCotent;
	}

	public void setNewMessageCotent(String newMessageCotent) {
		this.newMessageCotent = newMessageCotent;
	}

	public String getOldMessageCotent() {
		return oldMessageCotent;
	}

	public void setOldMessageCotent(String oldMessageCotent) {
		this.oldMessageCotent = oldMessageCotent;
	}

	public ObjectId getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(ObjectId merchantId) {
		this.merchantId = merchantId;
	}

}
