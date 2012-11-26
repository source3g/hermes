package com.source3g.hermes.entity.customer;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;
@Document
public class CustomerGroup extends AbstractEntity {
	private static final long serialVersionUID = 5514478207740377728L;

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
