package com.source3g.hermes.entity.customer;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class PackageLock extends AbstractEntity {
	private static final long serialVersionUID = -5805893039741682170L;

	private ObjectId merchantId;

	private Boolean locking;

	public ObjectId getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(ObjectId merchantId) {
		this.merchantId = merchantId;
	}

	public Boolean getLocking() {
		return locking;
	}

	public void setLocking(Boolean locking) {
		this.locking = locking;
	}

}
