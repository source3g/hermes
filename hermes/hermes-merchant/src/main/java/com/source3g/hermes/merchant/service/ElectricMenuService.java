package com.source3g.hermes.merchant.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

	public void deleteItem(ObjectId menuId,ObjectId ItemId) {
		ElectricMenu electricMenu=mongoTemplate.findOne(new Query(Criteria.where("_id").is(menuId)),ElectricMenu.class);
		List<ElectricMenuItem> electricMenuItems=electricMenu.getItems();
		for(ElectricMenuItem e:electricMenuItems){
			if(e.getId()==ItemId){
				electricMenuItems.remove(e);
			}
		}
		mongoTemplate.save(electricMenu);
	}
	
	public void deleteItem(String title,ObjectId menuId) {
		ElectricMenu electricMenu=mongoTemplate.findOne(new Query(Criteria.where("_id").is(menuId)),ElectricMenu.class);
		List<ElectricMenuItem> electricMenuItems=electricMenu.getItems();
		for(ElectricMenuItem e:electricMenuItems){
			if(e.getTitle().equals(title)){
				electricMenuItems.remove(e);
			}
		}
		mongoTemplate.save(electricMenu);
	}

	public void addItem(ElectricMenuItem electricMenuItem, ObjectId menuId) {
		ElectricMenu electricMenu=mongoTemplate.findOne(new Query(Criteria.where("_id").is(menuId)), ElectricMenu.class);
		List<ElectricMenuItem> electricMenuItems=electricMenu.getItems();
		electricMenuItems.add(electricMenuItem);
		mongoTemplate.save(electricMenu);
	}

	public void updateItem(ElectricMenuItem electricMenuItem, ObjectId menuId) {
		ElectricMenu electricMenu=mongoTemplate.findOne(new Query(Criteria.where("_id").is(menuId)), ElectricMenu.class);
		List<ElectricMenuItem> electricMenuItems=electricMenu.getItems();
		for(ElectricMenuItem e:electricMenuItems){
			if(e.getId()==electricMenuItem.getId()){
				electricMenuItems.remove(e);
				electricMenuItems.add(electricMenuItem);
			}
		}
		mongoTemplate.save(electricMenu);
	}

	public void addMenu(ElectricMenu electricMenu, ObjectId merchantId) {
		electricMenu.setMerchantId(merchantId);
		mongoTemplate.insert(electricMenu);
	}

	public void updateMenu(ElectricMenu electricMenu) {
		mongoTemplate.save(electricMenu);
	}

	public void deleteMenu(ObjectId objectId) {
		mongoTemplate.remove(new Query(Criteria.where("_id").is(objectId)));
	}
	
	public ElectricMenuItem findItemByTitle(ObjectId menuId, String itemTitle) {
		ElectricMenu electricMenu=mongoTemplate.findOne(new Query(Criteria.where("_id").is(menuId)), ElectricMenu.class);
		List<ElectricMenuItem> electricMenuItems=electricMenu.getItems();
		for(ElectricMenuItem electricMenuItem:electricMenuItems){
			if(electricMenuItem.getTitle().equals(itemTitle)){
				return electricMenuItem;
			}
		}
		return null;
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

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

}
