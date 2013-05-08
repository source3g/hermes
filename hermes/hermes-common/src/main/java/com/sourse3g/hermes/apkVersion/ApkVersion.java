package com.sourse3g.hermes.apkVersion;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class ApkVersion extends AbstractEntity {
	private static final long serialVersionUID = 2875299185557066342L;
	private String apkVersion;
	private String url;
	private Date uploadTime;
	private String md5;
	private String describe;
	private int code;

	public ApkVersion(String apkVersion, String url, Date uploadTime,String describe,int code) {
		super();
		this.apkVersion = apkVersion;
		this.url = url;
		this.uploadTime = uploadTime;
		this.describe= describe;
		this.code=code;
	}

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

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
