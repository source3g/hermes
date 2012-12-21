package com.source3g.hermes.service;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.source3g.hermes.entity.Device;
import com.source3g.hermes.entity.merchant.Merchant;

public class CommonBaseService extends BaseService {
	public Merchant findMerchantByDeviceSn(String sn) throws Exception {
		Device device = mongoTemplate.findOne(new Query(Criteria.where("sn").is(sn)), Device.class);
		if (device == null) {
			throw new Exception("盒子编号不存在");
		}
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("deviceIds").is(device.getId())), Merchant.class);
		if (merchant == null) {
			throw new Exception("盒子所属商户不存在");
		}
		return merchant;
	}
}
