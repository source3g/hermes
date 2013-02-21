package com.source3g.hermes.entity.merchant;

import java.io.Serializable;

public class MessageBalance implements Serializable{
	private static final long serialVersionUID = -3419910566493186938L;
	private int sentCount=0;// 已发送短信数量
	private int surplusMsgCount=0;// 可用数量
	private int totalCount=0;// 充值短信总数量

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getSentCount() {
		return sentCount;
	}

	public void setSentCount(int sentCount) {
		this.sentCount = sentCount;
	}

	public int getSurplusMsgCount() {
		return surplusMsgCount;
	}

	public void setSurplusMsgCount(int surplusMsgCount) {
		this.surplusMsgCount = surplusMsgCount;
	}

}
