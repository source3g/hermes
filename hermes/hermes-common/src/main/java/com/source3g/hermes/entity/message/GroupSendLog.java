package com.source3g.hermes.entity.message;

import java.util.Date;

import org.bson.types.ObjectId;

import com.source3g.hermes.entity.AbstractEntity;

public class GroupSendLog extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1094950145584456608L;
	private int sendCount;// 发送数量
	private int sentCount;//已发送数量 
	private int successCount; // 发送成功数量
	private Date sendTime; // 发送时间
	private String content;// 短信内容
	private ObjectId merchantId;

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public ObjectId getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(ObjectId merchantId) {
		this.merchantId = merchantId;
	}

	public int getSendCount() {
		return sendCount;
	}

	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getSentCount() {
		return sentCount;
	}

	public void setSentCount(int sentCount) {
		this.sentCount = sentCount;
	}

}
