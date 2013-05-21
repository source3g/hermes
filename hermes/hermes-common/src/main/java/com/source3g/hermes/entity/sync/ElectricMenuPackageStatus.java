package com.source3g.hermes.entity.sync;

import java.util.Date;

import org.bson.types.ObjectId;

import com.source3g.hermes.entity.AbstractEntity;
import com.source3g.hermes.enums.ElectricMenuPackageStatusEnum;

public class ElectricMenuPackageStatus extends AbstractEntity{
	private static final long serialVersionUID = -3962181382717393971L;
	
	private ObjectId merchantId;
	
	private ElectricMenuPackageStatusEnum status;
	
	private Date lastChangeTime;

	public ElectricMenuPackageStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ElectricMenuPackageStatusEnum status) {
		this.status = status;
	}

	public ObjectId getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(ObjectId merchantId) {
		this.merchantId = merchantId;
	}

	public Date getLastChangeTime() {
		return lastChangeTime;
	}

	public void setLastChangeTime(Date lastChangeTime) {
		this.lastChangeTime = lastChangeTime;
	}


}
