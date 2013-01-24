package com.source3g.hermes.dto.customer;

import com.source3g.hermes.dto.message.StatisticObject;

/**
 * 首页的最新数据
 * 
 * @author zhaobin
 * 
 */
public class CustomerStatisticsDto {

	private StatisticObject editedCustomerCount;
	private StatisticObject uneditedCustomerCount;
	private StatisticObject editedCallInCountThreeDay;
	private StatisticObject uneditedCallInCountThreeDay;
	private StatisticObject editedCallInCountAWeek;
	private StatisticObject uneditedCallInCountAWeek;
	public StatisticObject getEditedCustomerCount() {
		return editedCustomerCount;
	}
	public void setEditedCustomerCount(StatisticObject editedCustomerCount) {
		this.editedCustomerCount = editedCustomerCount;
	}
	public StatisticObject getUneditedCustomerCount() {
		return uneditedCustomerCount;
	}
	public void setUneditedCustomerCount(StatisticObject uneditedCustomerCount) {
		this.uneditedCustomerCount = uneditedCustomerCount;
	}
	public StatisticObject getEditedCallInCountThreeDay() {
		return editedCallInCountThreeDay;
	}
	public void setEditedCallInCountThreeDay(StatisticObject editedCallInCountThreeDay) {
		this.editedCallInCountThreeDay = editedCallInCountThreeDay;
	}
	public StatisticObject getUneditedCallInCountThreeDay() {
		return uneditedCallInCountThreeDay;
	}
	public void setUneditedCallInCountThreeDay(StatisticObject uneditedCallInCountThreeDay) {
		this.uneditedCallInCountThreeDay = uneditedCallInCountThreeDay;
	}
	public StatisticObject getEditedCallInCountAWeek() {
		return editedCallInCountAWeek;
	}
	public void setEditedCallInCountAWeek(StatisticObject editedCallInCountAWeek) {
		this.editedCallInCountAWeek = editedCallInCountAWeek;
	}
	public StatisticObject getUneditedCallInCountAWeek() {
		return uneditedCallInCountAWeek;
	}
	public void setUneditedCallInCountAWeek(StatisticObject uneditedCallInCountAWeek) {
		this.uneditedCallInCountAWeek = uneditedCallInCountAWeek;
	}
}
