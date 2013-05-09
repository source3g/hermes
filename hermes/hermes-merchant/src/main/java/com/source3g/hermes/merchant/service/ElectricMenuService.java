package com.source3g.hermes.merchant.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.source3g.hermes.entity.merchant.ElectricMenu;
import com.source3g.hermes.entity.merchant.ElectricMenuItem;

@Component
public class ElectricMenuService {

	@Autowired
	private MongoTemplate mongoTemplate;

	public List<ElectricMenu> findByMerchantId(ObjectId merchantId) {
		List<ElectricMenu> electricMenus = mongoTemplate.find(new Query(Criteria.where("merchantId").is(merchantId)), ElectricMenu.class);
		return electricMenus;
	}

	public void deleteItem(String title) {
		mongoTemplate.remove(new Query(Criteria.where("title").is(title)),ElectricMenuItem.class);
	}

	public void addItem(ElectricMenuItem electricMenuItem) {
		mongoTemplate.insert(electricMenuItem);
	}

	public void UpdateItemName(String electricMenuTitle) { 
		Update update=new Update();
		update.set("name", electricMenuTitle);
		mongoTemplate.updateFirst(new Query(), update, ElectricMenu.class);
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
