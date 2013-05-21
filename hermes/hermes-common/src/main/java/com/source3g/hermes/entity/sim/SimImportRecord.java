package com.source3g.hermes.entity.sim;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;
import com.source3g.hermes.utils.excel.ErrorResult;

@Document
public class SimImportRecord extends AbstractEntity {
	private static final long serialVersionUID = -8109568612074806815L;

	private Date importTime;
	private List<ErrorResult> reportErrors;
	private Integer importCount;

	public Date getImportTime() {
		return importTime;
	}

	public void setImportTime(Date importTime) {
		this.importTime = importTime;
	}

	public Integer getImportCount() {
		return importCount;
	}

	public void setImportCount(Integer importCount) {
		this.importCount = importCount;
	}

	public List<ErrorResult> getReportErrors() {
		return reportErrors;
	}

	public void setReportErrors(List<ErrorResult> reportErrors) {
		this.reportErrors = reportErrors;
	}

}
