package com.source3g.hermes.entity.customer;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;

import com.source3g.hermes.entity.AbstractEntity;

public class CustomerGroup extends AbstractEntity {
	@NotEmpty(message = "{customer.group.name.not.null}")
	private String name;

	private ObjectId merchantId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ObjectId getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(ObjectId merchantId) {
		this.merchantId = merchantId;
	}

}
