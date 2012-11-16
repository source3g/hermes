package com.source3g.hermes.entity;

import org.hibernate.validator.constraints.NotEmpty;

public class Device extends AbstractEntity {
	
	@NotEmpty(message="{device.sn.not.null}")
	private String sn;
	private String simId;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getSimId() {
		return simId;
	}

	public void setSimId(String simId) {
		this.simId = simId;
	}
	
	
}
