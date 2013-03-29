package com.source3g.hermes.vo;

import com.source3g.hermes.entity.Device;

public class DeviceDistributeVo {
	private Device device;
	// private Merchant merchant;
	// private Saler saler;
	// private BranchCompany branchCompany;
	private String merchantName;
	private String salerName;
	private String branchCompanyName;

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getSalerName() {
		return salerName;
	}

	public void setSalerName(String salerName) {
		this.salerName = salerName;
	}

	public String getBranchCompanyName() {
		return branchCompanyName;
	}

	public void setBranchCompanyName(String branchCompanyName) {
		this.branchCompanyName = branchCompanyName;
	}

}
