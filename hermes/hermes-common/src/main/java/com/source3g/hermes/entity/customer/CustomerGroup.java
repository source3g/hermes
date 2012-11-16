package com.source3g.hermes.entity.customer;

import org.hibernate.validator.constraints.NotEmpty;

import com.source3g.hermes.entity.AbstractEntity;

public class CustomerGroup extends AbstractEntity {
	@NotEmpty(message = "{customer.group.name.not.null}")
	private String name;

	private Object merchantId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Object merchantId) {
		this.merchantId = merchantId;
	}

}
