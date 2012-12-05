package com.source3g.hermes.entity.message;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.enums.MessageStatus;
import com.source3g.hermes.enums.MessageType;
@Document
public class MessageSendLog {
	private String customerName;
	private String customerGroupName;
	private String pnone; // 电话号码
	private int sendCount; // 发送数量
	private Date date; // 发送时间
	private MessageType type; // 发送形式
	private ObjectId merchantId;// 商户Id
	private MessageStatus status;
	private String content;//短信内容 
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerGroupName() {
		return customerGroupName;
	}

	public void setCustomerGroupName(String customerGroupName) {
		this.customerGroupName = customerGroupName;
	}

	public String getPnone() {
		return pnone;
	}

	public void setPnone(String pnone) {
		this.pnone = pnone;
	}

	public int getSendCount() {
		return sendCount;
	}

	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public ObjectId getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(ObjectId merchantId) {
		this.merchantId = merchantId;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public MessageStatus getStatus() {
		return status;
	}

	public void setStatus(MessageStatus status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
