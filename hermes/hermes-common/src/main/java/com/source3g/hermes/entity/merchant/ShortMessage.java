package com.source3g.hermes.entity.merchant;

public class ShortMessage {
	  private int sentCount;//已发送短信数量 
   private int surplusMsgCount;//可用数量
   private int totalCount;//充值短信总数量

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

