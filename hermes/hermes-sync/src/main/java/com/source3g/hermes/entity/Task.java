package com.source3g.hermes.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Task extends AbstractEntity{
	
	private static final long serialVersionUID = -6381822236940621106L;
	private String remoteUri = "";
	private String md5 = "";
	
	public String getRemoteUri() {
		return remoteUri;
	}

	public void setRemoteUri(String remoteUri) {
		this.remoteUri = remoteUri;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}
}