package com.source3g.hermes.admin.security.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class Role extends AbstractEntity {
	private static final long serialVersionUID = 751136186764235343L;
	private String name;
	@DBRef
	private List<Resource> resources;

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Role r = (Role) obj;
		if (this.getId() != null) {
			return this.getId().equals(r.getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (this.getId() != null) {
			return this.getId().hashCode();
		}
		return super.hashCode();
	}

}
