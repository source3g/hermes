package com.source3g.hermes.dto.message;

import java.io.Serializable;

public class StatisticObject implements Serializable {
	private static final long serialVersionUID = 5579093173384589902L;
	private String displayName;
	private long value;
	
	public StatisticObject(String displayName, long value) {
		super();
		this.displayName = displayName;
		this.value = value;
	}
	
	public StatisticObject() {
		super();
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
}
