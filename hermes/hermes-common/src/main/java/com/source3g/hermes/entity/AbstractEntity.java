package com.source3g.hermes.entity;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;

public abstract class AbstractEntity implements Serializable{
	private static final long serialVersionUID = -6918623741108611406L;
	@Id
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
