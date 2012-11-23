package com.source3g.hermes.entity.merchant;

public class ShortMessage {
	  private int sendMsg;
   private int surplusMsg;
   private int totalMsg;

   
   		public int getSendMsg() {
			return sendMsg;
		}
		public void setSendMsg(int sendMsg) {
			this.sendMsg = sendMsg;
		}
		public int getTotalMsg() {
			return totalMsg;
		}
		
		public void setTotalMsg(int totalMsg) {
			this.totalMsg = sendMsg+surplusMsg;
		}
		public int getSurplusMsg() {
			return surplusMsg;
		}
		public void setSurplusMsg(int surplusMsg) {
			this.surplusMsg = surplusMsg;
		}
		

	}

