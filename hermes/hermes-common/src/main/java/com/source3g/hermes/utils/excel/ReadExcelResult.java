package com.source3g.hermes.utils.excel;

import java.util.ArrayList;
import java.util.List;

public class ReadExcelResult<T> {
	private List<T> result;
	private List<ErrorResult> reports = new ArrayList<ErrorResult>();

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public List<ErrorResult> getReports() {
		return reports;
	}

	public void setReports(List<ErrorResult> reports) {
		this.reports = reports;
	}

}