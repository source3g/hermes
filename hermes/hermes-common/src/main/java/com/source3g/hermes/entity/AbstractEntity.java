package com.source3g.hermes.entity;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

public abstract class AbstractEntity {
	private ObjectId id;

	@JsonIgnore
	public ObjectId getIdObjId() {
		return id;
	}

	public String getId() {
		if (id == null) {
			return null;
		}
		return id.toString();
	}

	@JsonIgnore
	public void setId(ObjectId id) {
		this.id = id;
	}

	public void setId(String id) {
		if (!StringUtils.isEmpty(id)) {
			this.id = new ObjectId(id);
		}
	}

}
