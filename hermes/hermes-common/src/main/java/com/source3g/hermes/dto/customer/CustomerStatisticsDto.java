package com.source3g.hermes.dto.customer;

/**
 * 首页的最新数据
 * 
 * @author zhaobin
 * 
 */
public class CustomerStatisticsDto {

	private long editedCustomerCount;
	private long uneditedCustomerCount;
	private long editedCallInCountThreeDay;
	private long uneditedCallInCountThreeDay;
	private long editedCallInCountAWeek;
	private long uneditedCallInCountAWeek;


	public long getEditedCallInCountThreeDay() {
		return editedCallInCountThreeDay;
	}

	public void setEditedCallInCountThreeDay(long editedCallInCountThreeDay) {
		this.editedCallInCountThreeDay = editedCallInCountThreeDay;
	}

	public long getUneditedCallInCountThreeDay() {
		return uneditedCallInCountThreeDay;
	}

	public void setUneditedCallInCountThreeDay(long uneditedCallInCountThreeDay) {
		this.uneditedCallInCountThreeDay = uneditedCallInCountThreeDay;
	}

	public long getEditedCallInCountAWeek() {
		return editedCallInCountAWeek;
	}

	public void setEditedCallInCountAWeek(long editedCallInCountAWeek) {
		this.editedCallInCountAWeek = editedCallInCountAWeek;
	}

	public long getUneditedCallInCountAWeek() {
		return uneditedCallInCountAWeek;
	}

	public void setUneditedCallInCountAWeek(long uneditedCallInCountAWeek) {
		this.uneditedCallInCountAWeek = uneditedCallInCountAWeek;
	}

	public long getEditedCustomerCount() {
		return editedCustomerCount;
	}

	public void setEditedCustomerCount(long editedCustomerCount) {
		this.editedCustomerCount = editedCustomerCount;
	}

	public long getUneditedCustomerCount() {
		return uneditedCustomerCount;
	}

	public void setUneditedCustomerCount(long uneditedCustomerCount) {
		this.uneditedCustomerCount = uneditedCustomerCount;
	}
}
