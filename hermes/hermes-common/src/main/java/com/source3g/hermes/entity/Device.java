package com.source3g.hermes.entity;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;

public class Device extends AbstractEntity {

	@NotEmpty(message = "{device.sn.not.null}")
	private String sn;

	private ObjectId simId;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public ObjectId getSimId() {
		return simId;
	}

	public void setSimId(ObjectId simId) {
		this.simId = simId;
	}

}
