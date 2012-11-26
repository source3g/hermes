package com.source3g.hermes.entity.merchant;

import java.util.Date;

import org.bson.types.ObjectId;

import com.source3g.hermes.entity.AbstractEntity;

public class MessageNotes extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4307800998556986220L;
	private ObjectId merchantId;
	private Date date;
	private  int count;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public ObjectId getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(ObjectId merchantId) {
		this.merchantId = merchantId;
	}
	
	
}
