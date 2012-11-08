package com.source3g.hermes.entity.merchant;

import org.hibernate.validator.constraints.NotEmpty;

import com.source3g.hermes.entity.AbstractEntity;

public class MerchantGroup extends  AbstractEntity{
	@NotEmpty(message="{merchantgroup.name.not.null}")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
