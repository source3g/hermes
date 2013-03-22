package com.source3g.hermes.utils;

import java.util.Date;

public class GpsPoint {
	private Double x;
	private Double y;
	private Date reportTime;

	public GpsPoint(Double x, Double y) {
		super();
		this.x = x;
		this.y = y;
		this.reportTime = new Date();
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}
}
