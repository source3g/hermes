package com.source3g.hermes.entity.sync;

import java.util.Date;

import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.source3g.hermes.entity.AbstractEntity;

@JsonIgnoreProperties("id")
public class TaskPackage extends AbstractEntity {
	private static final long serialVersionUID = 3751253185117836302L;
	private long taskId;
	@JsonIgnore
	private Date createTime;
	private String remoteUrl;
	private String md5;
	@JsonIgnore
	private ObjectId merchantId;
	@JsonIgnore
	private String type;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public ObjectId getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(ObjectId merchantId) {
		this.merchantId = merchantId;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getRemoteUrl() {
		return remoteUrl;
	}

	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}
}
