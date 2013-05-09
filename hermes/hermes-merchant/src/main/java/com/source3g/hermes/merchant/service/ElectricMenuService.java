package com.source3g.hermes.merchant.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	@Value(value = "${image.menu.dir}")
	private String picPath;

	public List<ElectricMenu> findByMerchantId(ObjectId merchantId) {
		List<ElectricMenu> electricMenus = mongoTemplate.find(new Query(Criteria.where("merchantId").is(merchantId)), ElectricMenu.class);
		return electricMenus;
	}

	public void deleteItem(String title,ObjectId menuId) {
		mongoTemplate.remove(new Query(Criteria.where("_id").is(menuId).and("title").is(title)),ElectricMenuItem.class);
	}

	public void addItem(ElectricMenuItem electricMenuItem,ObjectId menuId) {
		ElectricMenu electricMenu=mongoTemplate.findOne(new Query(Criteria.where("_id").is(menuId)), ElectricMenu.class);
		List<ElectricMenuItem> electricMenuItems=electricMenu.getItems();
		electricMenuItems.add(electricMenuItem);
		mongoTemplate.save(electricMenuItem);
	}

	public void UpdateItemName(String electricMenuTitle) { 
		Update update=new Update();
		update.set("name", electricMenuTitle);
		mongoTemplate.updateFirst(new Query(), update, ElectricMenu.class);
	}

	public void deleteItem(ElectricMenuItem electricMenuItem) {
		mongoTemplate.remove(electricMenuItem);
	}
	
	public void addMenu(ElectricMenu electricMenu, ObjectId merchantId) {
		
	}
	public void updateMenu(ElectricMenu electricMenu){
		mongoTemplate.save(electricMenu);
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

	public void deleteMenu(ObjectId objectId) {
		
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

}
