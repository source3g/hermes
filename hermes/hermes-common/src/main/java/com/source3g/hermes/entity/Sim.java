package com.source3g.hermes.entity;

import org.hibernate.validator.constraints.NotEmpty;

public class Sim extends AbstractEntity {
	@NotEmpty(message="{device.sn.not.null}")
	private String no;

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}
	
	
}
