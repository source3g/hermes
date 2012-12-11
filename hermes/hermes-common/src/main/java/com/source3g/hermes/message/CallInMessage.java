package com.source3g.hermes.message;

import java.io.Serializable;

import org.bson.types.ObjectId;

public class CallInMessage implements Serializable {
	

	private static final long serialVersionUID = 8370392559526131833L;
	private String deviceSn;
	private String phone;
	private String time;
	private String duration;
	private ObjectId MerchantId;

	public String getDeviceSn() {
		return deviceSn;
	}
	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public ObjectId getMerchantId() {
		return MerchantId;
	}
	public void setMerchantId(ObjectId merchantId) {
		MerchantId = merchantId;
	}
	
}
