package com.source3g.hermes.entity.log;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class ErrorLog extends AbstractEntity {
	private static final long serialVersionUID = -3856032600670336854L;

	private String sn;
	private String content;
	private Date reportTime;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}
}
