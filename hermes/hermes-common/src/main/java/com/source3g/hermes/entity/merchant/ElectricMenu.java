package com.source3g.hermes.entity.merchant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class ElectricMenu extends AbstractEntity {
	
	private static final long serialVersionUID = 5220747468343318373L;
	private String name;
	private List<ElectricMenuItem> items=new ArrayList<ElectricMenuItem>();
	private ObjectId merchantId;
	private Date operateTime;
	
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

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
}
