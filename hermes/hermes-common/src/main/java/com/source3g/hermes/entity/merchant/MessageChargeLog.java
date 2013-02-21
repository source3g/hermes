package com.source3g.hermes.entity.merchant;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class MessageChargeLog extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4307800998556986220L;
	private ObjectId merchantId;
	private Date chargeTime;
	private  int count;

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
	public Date getChargeTime() {
		return chargeTime;
	}
	public void setChargeTime(Date chargeTime) {
		this.chargeTime = chargeTime;
	}
	
	
}
