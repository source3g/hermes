package com.source3g.hermes.vo;

import com.source3g.hermes.entity.device.Device;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.sync.DeviceStatus;
import com.sourse3g.hermes.branch.Saler;

public class DeviceVo {
	private Device device;
	private Merchant merchant;
	private DeviceStatus deviceStatus;
	private Long restTaskCount;
	private Saler saler;

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	public DeviceStatus getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(DeviceStatus deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public Long getRestTaskCount() {
		return restTaskCount;
	}

	public void setRestTaskCount(Long restTaskCount) {
		this.restTaskCount = restTaskCount;
	}

	public Saler getSaler() {
		return saler;
	}

	public void setSaler(Saler saler) {
		this.saler = saler;
	}

}
