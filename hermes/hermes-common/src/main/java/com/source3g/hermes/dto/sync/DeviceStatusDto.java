package com.source3g.hermes.dto.sync;

import java.util.Date;

public class DeviceStatusDto {

	private Long lastTaskId;
	private Date lastUpdateTime;
	private Date lastAskTime;
	private Long requestTaskId;
	private String sn;
	/**
	 * 设备状态，TaskConstants.INIT表示设备初始化 TaskConstants.increment表示拿一个增量包
	 * 当设备发送初始化请求时，将此状态置为INIT
	 */
	private String status;

	private Long restTaskCount;

	public Long getLastTaskId() {
		return lastTaskId;
	}

	public void setLastTaskId(Long lastTaskId) {
		this.lastTaskId = lastTaskId;
	}

	public Long getRestTaskCount() {
		return restTaskCount;
	}

	public void setRestTaskCount(Long restTaskCount) {
		this.restTaskCount = restTaskCount;
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

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Date getLastAskTime() {
		return lastAskTime;
	}

	public void setLastAskTime(Date lastAskTime) {
		this.lastAskTime = lastAskTime;
	}

}
