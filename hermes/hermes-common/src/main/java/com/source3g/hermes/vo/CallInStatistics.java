package com.source3g.hermes.vo;

import java.util.List;

import com.source3g.hermes.entity.ObjectValue;

public class CallInStatistics {
	private List<ObjectValue> newList;
	private List<ObjectValue> oldList;
	private List<ObjectValue> allList;
	public CallInStatistics() {
	}

	public List<ObjectValue> getNewList() {
		return newList;
	}

	public void setNewList(List<ObjectValue> newList) {
		this.newList = newList;
	}

	public List<ObjectValue> getOldList() {
		return oldList;
	}

	public void setOldList(List<ObjectValue> oldList) {
		this.oldList = oldList;
	}

	public List<ObjectValue> getAllList() {
		return allList;
	}

	public void setAllList(List<ObjectValue> allList) {
		this.allList = allList;
	}
}
