package com.source3g.hermes.entity;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

public abstract class AbstractEntity {

	private ObjectId id;

	public ObjectId getId() {
		return id;
	}

	@JsonIgnore
	public String getStrId() {
		if (id == null) {
			return null;
		}
		return id.toString();
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	@JsonIgnore
	public void setId(String id) {
		if (!StringUtils.isEmpty(id)) {
			this.id = new ObjectId(id);
		}
	}

}
