package com.source3g.hermes.entity.merchant;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class ElectricMenu extends AbstractEntity {
	
	private static final long serialVersionUID = 5220747468343318373L;
	private String name;
	private List<ElectricMenuItem> items;
	private ObjectId merchantId;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ElectricMenuItem> getItems() {
		return items;
	}

	public void setItems(List<ElectricMenuItem> items) {
		this.items = items;
	}

	public ObjectId getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(ObjectId merchantId) {
		this.merchantId = merchantId;
	}
}
