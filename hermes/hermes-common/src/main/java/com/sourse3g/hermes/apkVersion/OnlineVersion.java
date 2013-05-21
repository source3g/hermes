package com.sourse3g.hermes.apkVersion;

import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class OnlineVersion extends AbstractEntity {
	private static final long serialVersionUID = 1644988623170723623L;

	private String apkVersion;
	private VersionType versionType;

	public String getApkVersion() {
		return apkVersion;
	}

	public void setApkVersion(String apkVersion) {
		this.apkVersion = apkVersion;
	}
	
	public VersionType getVersionType() {
		return versionType;
	}

	public void setVersionType(VersionType versionType) {
		this.versionType = versionType;
	}

	public enum VersionType{
		RELEASE,
	}
}