package com.source3g.hermes.entity.merchant;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;
@Document
public class MerchantGroup extends  AbstractEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3481824808804668803L;
	@NotEmpty(message="{merchantgroup.name.not.null}")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
