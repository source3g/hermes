package com.source3g.hermes.monitor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ErrorLogService {
	@Value(value = "${error.report.dir}")
	private String errorReportDir;

	public String getErrorReportDir() {
		return errorReportDir;
	}

	public void setErrorReportDir(String errorReportDir) {
		this.errorReportDir = errorReportDir;
	}

}
