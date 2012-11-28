package com.source3g.hermes.sync.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

/**
 * 设备同步状态
 * 
 * @author Administrator 接收到设备请求后，记录本次请求时间，并将上次更新的时间以后的增量返回
 * 
 * 
 */
@Document
public class DeviceStatus extends AbstractEntity {
	private static final long serialVersionUID = 3597624421463521346L;

	private String deviceSn;

	private Long lastTaskId;// 上次请求的taskId
	private Long requestTaskId;// 本次请求的taskId

	private Date lastUpdateTime;// 上次更新时间
	private Date requestTime;// 本次请求时间

	private int failedCount; // 当前请求的taskId失败次数

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public int getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(int failedCount) {
		this.failedCount = failedCount;
	}

	public Long getLastTaskId() {
		return lastTaskId;
	}

	public void setLastTaskId(Long lastTaskId) {
		this.lastTaskId = lastTaskId;
	}

	public Long getRequestTaskId() {
		return requestTaskId;
	}

	public void setRequestTaskId(Long requestTaskId) {
		this.requestTaskId = requestTaskId;
	}

}
