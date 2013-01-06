package com.source3g.hermes.vo;

public class CallInStatisticsCount {
	
	private int newCount;
	private int oldCount;
	
	
	public CallInStatisticsCount() {
		super();
	}

	public CallInStatisticsCount(int newCount, int oldCount) {
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
