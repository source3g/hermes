package com.source3g.hermes.security.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class Role extends AbstractEntity{
	private static final long serialVersionUID = 751136186764235343L;
	private List<Resource> resources;

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

}
