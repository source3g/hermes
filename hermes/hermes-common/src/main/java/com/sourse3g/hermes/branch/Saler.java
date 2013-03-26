package com.sourse3g.hermes.branch;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;
@Document
public class Saler extends AbstractEntity {
	private static final long serialVersionUID = -106437632106701610L;
	private String name;
	private ObjectId branchCompanyId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ObjectId getBranchCompanyId() {
		return branchCompanyId;
	}

	public void setBranchCompanyId(ObjectId branchCompanyId) {
		this.branchCompanyId = branchCompanyId;
	}

}
