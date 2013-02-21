package com.source3g.hermes.dto.message;

public class MessageStatisticsDto {
	private StatisticObjectDto handUpMessageSentCountThreeDay;
	private StatisticObjectDto handUpMessageSentCountAWeek;
	private StatisticObjectDto messageGroupSentCountThreeDay;
	private StatisticObjectDto messageGroupSentCountAWeek;
	public StatisticObjectDto getHandUpMessageSentCountThreeDay() {
		return handUpMessageSentCountThreeDay;
	}
	public void setHandUpMessageSentCountThreeDay(StatisticObjectDto handUpMessageSentCountThreeDay) {
		this.handUpMessageSentCountThreeDay = handUpMessageSentCountThreeDay;
	}
	public StatisticObjectDto getHandUpMessageSentCountAWeek() {
		return handUpMessageSentCountAWeek;
	}
	public void setHandUpMessageSentCountAWeek(StatisticObjectDto handUpMessageSentCountAWeek) {
		this.handUpMessageSentCountAWeek = handUpMessageSentCountAWeek;
	}
	public StatisticObjectDto getMessageGroupSentCountThreeDay() {
		return messageGroupSentCountThreeDay;
	}
	public void setMessageGroupSentCountThreeDay(StatisticObjectDto messageGroupSentCountThreeDay) {
		this.messageGroupSentCountThreeDay = messageGroupSentCountThreeDay;
	}
	public StatisticObjectDto getMessageGroupSentCountAWeek() {
		return messageGroupSentCountAWeek;
	}
	public void setMessageGroupSentCountAWeek(StatisticObjectDto messageGroupSentCountAWeek) {
		this.messageGroupSentCountAWeek = messageGroupSentCountAWeek;
	}

}
