package com.source3g.hermes.vo;

import com.source3g.hermes.entity.device.Device;
import com.source3g.hermes.entity.merchant.Merchant;

public class DeviceVo {
private Device device;
private Merchant merchant;

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


}
