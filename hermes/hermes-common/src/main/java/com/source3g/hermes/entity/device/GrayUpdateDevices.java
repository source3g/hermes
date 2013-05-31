package com.source3g.hermes.entity.device;

import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;
@Document
public class GrayUpdateDevices extends AbstractEntity {
	private static final long serialVersionUID = 4522937468847910224L;

	private String sn;

	private String apkVersion;
	
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getApkVersion() {
		return apkVersion;
	}

	public void setApkVersion(String apkVersion) {
		this.apkVersion = apkVersion;
	}


}
