package com.source3g.hermes.dto.message;

public class StatisticObject {
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
