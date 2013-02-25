package com.source3g.hermes.message;

import java.io.Serializable;

import org.bson.types.ObjectId;

public class GroupSendMsg implements Serializable {
	private static final long serialVersionUID = 9048724675969377166L;
	private String[] phoneArray;
	private String[] ids;
	private String content;
	private ObjectId merchantId;
	private ObjectId groupLogId;

	
	

	public GroupSendMsg(String[] phoneArray, String[] ids, String content, ObjectId merchantId, ObjectId groupLogId) {
		super();
		this.phoneArray = phoneArray;
		this.ids = ids;
		this.content = content;
		this.merchantId = merchantId;
		this.groupLogId = groupLogId;
	}

	public GroupSendMsg() {
		super();
	}

	public String[] getPhoneArray() {
		return phoneArray;
	}

	public void setPhoneArray(String[] phoneArray) {
		this.phoneArray = phoneArray;
	}

	public String[] getIds() {
		return ids;
	}
	

	public ObjectId getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(ObjectId merchantId) {
		this.merchantId = merchantId;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ObjectId getGroupLogId() {
		return groupLogId;
	}

	public void setGroupLogId(ObjectId groupLogId) {
		this.groupLogId = groupLogId;
	}

}
