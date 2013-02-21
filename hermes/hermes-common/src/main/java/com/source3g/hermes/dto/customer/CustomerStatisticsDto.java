package com.source3g.hermes.dto.customer;

import java.io.Serializable;

import com.source3g.hermes.dto.message.StatisticObjectDto;


/**
 * 首页的最新数据
 * 
 * @author zhaobin
 * 
 */
public class CustomerStatisticsDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2744908961482991196L;
	private StatisticObjectDto editedCustomerCount;
	private StatisticObjectDto uneditedCustomerCount;
	private StatisticObjectDto editedCallInCountThreeDay;
	private StatisticObjectDto uneditedCallInCountThreeDay;
	private StatisticObjectDto editedCallInCountAWeek;
	private StatisticObjectDto uneditedCallInCountAWeek;
	public StatisticObjectDto getEditedCustomerCount() {
		return editedCustomerCount;
	}
	public void setEditedCustomerCount(StatisticObjectDto editedCustomerCount) {
		this.editedCustomerCount = editedCustomerCount;
	}
	public StatisticObjectDto getUneditedCustomerCount() {
		return uneditedCustomerCount;
	}
	public void setUneditedCustomerCount(StatisticObjectDto uneditedCustomerCount) {
		this.uneditedCustomerCount = uneditedCustomerCount;
	}
	public StatisticObjectDto getEditedCallInCountThreeDay() {
		return editedCallInCountThreeDay;
	}
	public void setEditedCallInCountThreeDay(StatisticObjectDto editedCallInCountThreeDay) {
		this.editedCallInCountThreeDay = editedCallInCountThreeDay;
	}
	public StatisticObjectDto getUneditedCallInCountThreeDay() {
		return uneditedCallInCountThreeDay;
	}
	public void setUneditedCallInCountThreeDay(StatisticObjectDto uneditedCallInCountThreeDay) {
		this.uneditedCallInCountThreeDay = uneditedCallInCountThreeDay;
	}
	public StatisticObjectDto getEditedCallInCountAWeek() {
		return editedCallInCountAWeek;
	}
	public void setEditedCallInCountAWeek(StatisticObjectDto editedCallInCountAWeek) {
		this.editedCallInCountAWeek = editedCallInCountAWeek;
	}
	public StatisticObjectDto getUneditedCallInCountAWeek() {
		return uneditedCallInCountAWeek;
	}
	public void setUneditedCallInCountAWeek(StatisticObjectDto uneditedCallInCountAWeek) {
		this.uneditedCallInCountAWeek = uneditedCallInCountAWeek;
	}
}
