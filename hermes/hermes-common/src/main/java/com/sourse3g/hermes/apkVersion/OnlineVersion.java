package com.sourse3g.hermes.apkVersion;

import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class OnlineVersion extends AbstractEntity {
	private static final long serialVersionUID = 1644988623170723623L;

	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
