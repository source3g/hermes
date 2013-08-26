package com.source3g.hermes.message.utils;


public class SmsReport {
	private String msgId;
	private ReportStatus status;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public ReportStatus getStatus() {
		return status;
	}

	public void setStatus(ReportStatus status) {
		this.status = status;
	}

}
