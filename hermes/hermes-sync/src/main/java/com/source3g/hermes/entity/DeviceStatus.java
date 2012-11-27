package com.source3g.hermes.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class DeviceStatus extends AbstractEntity {
	private static final long serialVersionUID = 3597624421463521346L;

	private String deviceSn;
	private Date lastUpdateTime;

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
}
