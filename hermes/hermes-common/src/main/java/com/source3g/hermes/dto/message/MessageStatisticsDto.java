package com.source3g.hermes.dto.message;

public class MessageStatisticsDto {
	private StatisticObject handUpMessageSentCountThreeDay;
	private StatisticObject handUpMessageSentCountAWeek;
	private StatisticObject messageGroupSentCountThreeDay;
	private StatisticObject messageGroupSentCountAWeek;
	public StatisticObject getHandUpMessageSentCountThreeDay() {
		return handUpMessageSentCountThreeDay;
	}
	public void setHandUpMessageSentCountThreeDay(StatisticObject handUpMessageSentCountThreeDay) {
		this.handUpMessageSentCountThreeDay = handUpMessageSentCountThreeDay;
	}
	public StatisticObject getHandUpMessageSentCountAWeek() {
		return handUpMessageSentCountAWeek;
	}
	public void setHandUpMessageSentCountAWeek(StatisticObject handUpMessageSentCountAWeek) {
		this.handUpMessageSentCountAWeek = handUpMessageSentCountAWeek;
	}
	public StatisticObject getMessageGroupSentCountThreeDay() {
		return messageGroupSentCountThreeDay;
	}
	public void setMessageGroupSentCountThreeDay(StatisticObject messageGroupSentCountThreeDay) {
		this.messageGroupSentCountThreeDay = messageGroupSentCountThreeDay;
	}
	public StatisticObject getMessageGroupSentCountAWeek() {
		return messageGroupSentCountAWeek;
	}
	public void setMessageGroupSentCountAWeek(StatisticObject messageGroupSentCountAWeek) {
		this.messageGroupSentCountAWeek = messageGroupSentCountAWeek;
	}

}
