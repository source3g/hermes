package com.source3g.hermes.sync.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ApkVersion {
	public ApkVersion(String apkVersion, String url,Date uploadTime) {
		super();
		this.apkVersion = apkVersion;
		this.url = url;
		this.uploadTime=uploadTime;
	}

	private String apkVersion;
	private String url;
	private Date uploadTime;

	public String getApkVersion() {
		return apkVersion;
	}

	public void setApkVersion(String apkVersion) {
		this.apkVersion = apkVersion;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

}
