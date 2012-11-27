package com.source3g.hermes.entity;

import java.util.Date;

import org.bson.types.ObjectId;

public class TaskPackage extends AbstractEntity{
	private static final long serialVersionUID = 3751253185117836302L;
	
	private Date createTime;
	private String path;
	private ObjectId merchantId;
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public ObjectId getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(ObjectId merchantId) {
		this.merchantId = merchantId;
	}
}
