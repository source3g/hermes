package com.sourse3g.hermes.branch;

import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;
@Document
public class BranchCompany extends AbstractEntity {
	private static final long serialVersionUID = -775318956120545939L;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
