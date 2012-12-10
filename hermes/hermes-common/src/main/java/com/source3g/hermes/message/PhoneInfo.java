package com.source3g.hermes.message;

import java.io.Serializable;

import org.bson.types.ObjectId;

public class PhoneInfo implements Serializable {

	private static final long serialVersionUID = 3575578879913333816L;

	public PhoneInfo() {
		super();
	}

	public PhoneInfo(String phoneNumber, String content, ObjectId messageSendLogId) {
		super();
		this.phoneNumber = phoneNumber;
		this.content = content;
		this.messageSendLogId = messageSendLogId;
	}

	private String phoneNumber;
	private String content;
	private ObjectId messageSendLogId;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ObjectId getMessageSendLogId() {
		return messageSendLogId;
	}

	public void setMessageSendLogId(ObjectId messageSendLogId) {
		this.messageSendLogId = messageSendLogId;
	}

}
