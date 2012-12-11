package com.source3g.hermes.message;

import java.io.Serializable;
import java.util.List;

import org.bson.types.ObjectId;

import com.source3g.hermes.enums.MessageType;

public class ShortMessageMessage implements Serializable {

	private static final long serialVersionUID = 4430012271716760588L;

	private List<PhoneInfo> phoneInfos;
	private PhoneInfo phoneInfo;
	private String content;
	private MessageType messageType;
	private ObjectId merchantId;

	
	
	public PhoneInfo getPhoneInfo() {
		return phoneInfo;
	}

	public void setPhoneInfo(PhoneInfo phoneInfo) {
		this.phoneInfo = phoneInfo;
	}

	public List<PhoneInfo> getPhoneInfos() {
		return phoneInfos;
	}

	public void setPhoneInfos(List<PhoneInfo> phoneInfos) {
		this.phoneInfos = phoneInfos;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public ObjectId getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(ObjectId merchantId) {
		this.merchantId = merchantId;
	}

}
