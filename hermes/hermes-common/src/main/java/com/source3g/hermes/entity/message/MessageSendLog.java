package com.source3g.hermes.entity.message;

import java.util.Date;

import org.bson.types.ObjectId;

public class MessageSendLog {
	private String customerName;
	private String customerGroupName;
	private String pnone; //电话号码
	private int  count;	 //发送数量
	private Date date;  //发送时间
	private String tppe; //发送形式
	private ObjectId merchantId;//商户Id
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerGroupName() {
		return customerGroupName;
	}
	public void setCustomerGroupName(String customerGroupName) {
		this.customerGroupName = customerGroupName;
	}
	public String getPnone() {
		return pnone;
	}
	public void setPnone(String pnone) {
		this.pnone = pnone;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getTppe() {
		return tppe;
	}
	public void setTppe(String tppe) {
		this.tppe = tppe;
	}
	public ObjectId getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(ObjectId merchantId) {
		this.merchantId = merchantId;
	}
	
	
}
