package com.source3g.hermes.entity.sync;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

/**
 * 设备同步状态
 * 
 * @author zhaobin 接收到设备请求后，记录本次请求时间，并将上次更新的时间以后的增量返回
 * 
 * 
 */
@Document
public class DeviceStatus extends AbstractEntity {
	private static final long serialVersionUID = 3597624421463521346L;

	private String deviceSn;

	private Long lastTaskId;// 上次请求的taskId
	private Long requestTaskId;// 本次请求的taskId

	private Date lastAskTime;// 上次心跳时间

	private Date lastUpdateTime;// 上次更新时间 显示上次成功拿走任务的时间

	private Date requestTime;// 本次请求时间 //并没有起作用

	private int failedCount; // 当前请求的taskId失败次数
	/**
	 * 设备状态，TaskConstants.INIT表示设备初始化 TaskConstants.increment表示拿一个增量包
	 * 当设备发送初始化请求时，将此状态置为INIT
	 */
	private String status;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLastAskTime() {
		return lastAskTime;
	}

	public void setLastAskTime(Date lastAskTime) {
		this.lastAskTime = lastAskTime;
	}

}
