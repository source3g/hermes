package com.source3g.hermes.vo;

public class CallInStatisticsToday {
	
	private int newCount;
	private int oldCount;
	
	
	public CallInStatisticsToday() {
		super();
	}

	public CallInStatisticsToday(int newCount, int oldCount) {
		super();
		this.newCount = newCount;
		this.oldCount = oldCount;
	}
	
	public int getNewCount() {
		return newCount;
	}
	public void setNewCount(int newCount) {
		this.newCount = newCount;
	}
	public int getOldCount() {
		return oldCount;
	}
	public void setOldCount(int oldCount) {
		this.oldCount = oldCount;
	}
}
