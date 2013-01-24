package com.source3g.hermes.message;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;
import com.source3g.hermes.enums.MessageStatus;
import com.source3g.hermes.enums.MessageType;
@Document
public class ShortMessageRecord extends AbstractEntity {

	private static final long serialVersionUID = 4430012271716760588L;
	
	private String msgId;
	private String phone;
	private String content;
	private MessageType messageType;
	private ObjectId merchantId;
	private ObjectId messageSendLogId;//单独发送消息时的记录，群发时不产生记录
	private ObjectId groupLogId;//群发消息时的记录，单发时没有
	
	private MessageStatus status;
	
	public ObjectId getGroupLogId() {
		return groupLogId;
	}

	public void setGroupLogId(ObjectId groupLogId) {
		this.groupLogId = groupLogId;
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

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public MessageStatus getStatus() {
		return status;
	}

	public void setStatus(MessageStatus status) {
		this.status = status;
	}

}
