package com.source3g.hermes.merchant.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.source3g.hermes.entity.merchant.ElectricMenu;
import com.source3g.hermes.entity.merchant.ElectricMenuItem;

@Component
public class ElectricMenuService {

	@Autowired
	private MongoTemplate mongoTemplate;

	public List<ElectricMenu> findByMerchantId(ObjectId merchantId) {
		List<ElectricMenu> electricMenus = mongoTemplate.findAll(ElectricMenu.class);
		return electricMenus;
	}

	public void deleteItem(String title) {

	}

	public void addItem(ElectricMenuItem electricMenuItem) {

	}

	public void UpdateItemName(String electricMenuTitle) {

	}

	/**
	 * 只增加menu或者修改menu的基本属性,不修改item
	 */
	public void addMenu(List<ElectricMenu> menus, ObjectId merchantId) {
		for (ElectricMenu electricMenu : menus) {
			electricMenu.setMerchantId(merchantId);
			mongoTemplate.insert(electricMenu);
		}
	}

}
