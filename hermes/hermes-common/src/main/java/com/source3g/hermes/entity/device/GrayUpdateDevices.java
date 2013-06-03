package com.source3g.hermes.entity.device;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class GrayUpdateDevices extends AbstractEntity {
	private static final long serialVersionUID = -5841633969476601114L;
	// private List<ObjectId> deviceIds;
	private List<String> snList;

	public List<String> getSnList() {
		return snList;
	}

	public void setSnList(List<String> snList) {
		this.snList = snList;
	}

}
