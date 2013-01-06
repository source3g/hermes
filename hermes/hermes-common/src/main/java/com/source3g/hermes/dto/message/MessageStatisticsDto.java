package com.source3g.hermes.dto.message;

public class MessageStatisticsDto {
	private long handUpMessageSentCountThreeDay;
	private long handUpMessageSentCountAWeek;
	private long messageGroupSentCountThreeDay;
	private long messageGroupSentCountAWeek;

	public long getHandUpMessageSentCountThreeDay() {
		return handUpMessageSentCountThreeDay;
	}

	public void setHandUpMessageSentCountThreeDay(long handUpMessageSentCountThreeDay) {
		this.handUpMessageSentCountThreeDay = handUpMessageSentCountThreeDay;
	}

	public long getHandUpMessageSentCountAWeek() {
		return handUpMessageSentCountAWeek;
	}

	public void setHandUpMessageSentCountAWeek(long handUpMessageSentCountAWeek) {
		this.handUpMessageSentCountAWeek = handUpMessageSentCountAWeek;
	}

	public long getMessageGroupSentCountThreeDay() {
		return messageGroupSentCountThreeDay;
	}

	public void setMessageGroupSentCountThreeDay(long messageGroupSentCountThreeDay) {
		this.messageGroupSentCountThreeDay = messageGroupSentCountThreeDay;
	}

	public long getMessageGroupSentCountAWeek() {
		return messageGroupSentCountAWeek;
	}

	public void setMessageGroupSentCountAWeek(long messageGroupSentCountAWeek) {
		this.messageGroupSentCountAWeek = messageGroupSentCountAWeek;
	}

}
