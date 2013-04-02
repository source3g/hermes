package com.sourse3g.hermes.apkVersion;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class ApkVersion extends AbstractEntity  {
	private static final long serialVersionUID = 2875299185557066342L;

	public ApkVersion(String apkVersion, String url,Date uploadTime) {
		super();
		this.apkVersion = apkVersion;
		this.url = url;
		this.uploadTime=uploadTime;
	}

	private String apkVersion;
	private String url;
	private Date uploadTime;

	
	public ApkVersion() {
		super();
	}

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
