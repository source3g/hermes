package com.source3g.hermes.entity.message;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;
import com.source3g.hermes.enums.MessageStatus;
import com.source3g.hermes.enums.MessageType;

/**
 * 新增
 * 
 * @author Administrator
 * 
 */
@Document
public class ShortMessage extends AbstractEntity {
	private static final long serialVersionUID = -6695767113817154193L;

	private String msgId;
	private String phone;
	private String content;
	private MessageType messageType;
	private ObjectId merchantId;
	private ObjectId sendId;// 短信对应的发送记录ID，群发ID或单发ID
	private Date sendTime;
	private int priority=0;

	private MessageStatus status;

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public ObjectId getSendId() {
		return sendId;
	}

	public void setSendId(ObjectId sendId) {
		this.sendId = sendId;
	}

	public MessageStatus getStatus() {
		return status;
	}

	public void setStatus(MessageStatus status) {
		this.status = status;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
