package com.source3g.hermes.entity.device;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;
import com.source3g.hermes.entity.sim.SimInfo;
import com.source3g.hermes.utils.GpsPoint;

@Document
public class Device extends AbstractEntity {
	private static final long serialVersionUID = -4975323959505730113L;

	@NotEmpty(message = "{device.sn.not.null}")
	private String sn;

	private String apkVersion;

	private GpsPoint gpsPoint;

	private SimInfo simInfo;

	private List<SimChangeRecord> simChangeRecords;

	public GpsPoint getGpsPoint() {
		return gpsPoint;
	}

	public void setGpsPoint(GpsPoint gpsPoint) {
		this.gpsPoint = gpsPoint;
	}

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

	public SimInfo getSimInfo() {
		return simInfo;
	}

	public void setSimInfo(SimInfo simInfo) {
		this.simInfo = simInfo;
	}

	public List<SimChangeRecord> getSimChangeRecords() {
		if (simChangeRecords == null) {
			simChangeRecords = new ArrayList<SimChangeRecord>();
		}
		return simChangeRecords;
	}

	public void setSimChangeRecords(List<SimChangeRecord> simChangeRecords) {
		this.simChangeRecords = simChangeRecords;
	}

}
