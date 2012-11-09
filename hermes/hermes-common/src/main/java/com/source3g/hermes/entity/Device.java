package com.source3g.hermes.entity;

import org.hibernate.validator.constraints.NotEmpty;

public class Device extends AbstractEntity {
	
	@NotEmpty(message="{device.sn.not.null}")
	private String sn;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

}
