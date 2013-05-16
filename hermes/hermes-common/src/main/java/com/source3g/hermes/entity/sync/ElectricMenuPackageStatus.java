package com.source3g.hermes.entity.sync;

import org.bson.types.ObjectId;

import com.source3g.hermes.entity.AbstractEntity;
import com.source3g.hermes.enums.ElectricMenuPackageStatusEnum;

public class ElectricMenuPackageStatus extends AbstractEntity{
	private static final long serialVersionUID = -3962181382717393971L;
	
	private ObjectId merchantId;
	
	private ElectricMenuPackageStatusEnum status;

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


}
