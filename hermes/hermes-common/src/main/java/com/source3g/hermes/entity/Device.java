package com.source3g.hermes.entity;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class Device extends AbstractEntity {
	private static final long serialVersionUID = -4975323959505730113L;

	@NotEmpty(message = "{device.sn.not.null}")
	private String sn;
	@DBRef
	private Sim sim;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Sim getSim() {
		return sim;
	}

	public void setSim(Sim sim) {
		this.sim = sim;
	}


}
