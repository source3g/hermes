package com.source3g.hermes.message;

import java.io.Serializable;

import org.bson.types.ObjectId;

import com.source3g.hermes.entity.message.GroupSendLog;
import com.source3g.hermes.enums.MessageType;

public class ShortMessageMessage implements Serializable {

	private static final long serialVersionUID = 4430012271716760588L;

	private String phone;
	private String content;
	private MessageType messageType;
	private ObjectId merchantId;
	private ObjectId messageSendLogId;//单独发送消息时的记录，群发时不产生记录
	private GroupSendLog groupSendLog;

	
	public GroupSendLog getGroupSendLog() {
		return groupSendLog;
	}

	public void setGroupSendLog(GroupSendLog groupSendLog) {
		this.groupSendLog = groupSendLog;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public ObjectId getMessageSendLogId() {
		return messageSendLogId;
	}

	public void setMessageSendLogId(ObjectId messageSendLogId) {
		this.messageSendLogId = messageSendLogId;
	}

}
