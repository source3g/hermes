package com.source3g.hermes.security.entity;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class Resource extends AbstractEntity {
	
	private static final long serialVersionUID = -1269581252095220421L;
	@NotEmpty(message="{resource.name.not.empty}")
	private String name;
	@NotEmpty(message = "{resource.code.not.empty}")
	private String code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
