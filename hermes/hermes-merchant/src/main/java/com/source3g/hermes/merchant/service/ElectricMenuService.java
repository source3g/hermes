package com.source3g.hermes.merchant.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.source3g.hermes.entity.merchant.ElectricMenu;
import com.source3g.hermes.entity.merchant.ElectricMenuItem;
import com.source3g.hermes.service.BaseService;

@Component
public class ElectricMenuService extends BaseService {

	@Autowired
	private MongoTemplate mongoTemplate;

	public List<ElectricMenu> findByMerchantId(ObjectId merchantId) {
		List<ElectricMenu> electricMenus = mongoTemplate.find(new Query(Criteria.where("merchantId").is(merchantId)), ElectricMenu.class);
		return electricMenus;
	}

	public void deleteItem(ObjectId ItemId, ObjectId menuId) {
		ElectricMenu electricMenu = mongoTemplate.findOne(new Query(Criteria.where("_id").is(menuId)), ElectricMenu.class);
		List<ElectricMenuItem> electricMenuItems = electricMenu.getItems();
		for (int i=0;i<electricMenuItems.size();i++) {
			if (electricMenuItems.get(i).getId().equals(ItemId)) {
				electricMenuItems.remove(i);
			}
		}
		Update update=new Update();
		update.set("items", electricMenuItems);
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(menuId)), update, ElectricMenu.class);
	}

	public void addItem(ElectricMenuItem electricMenuItem, ObjectId menuId) {
		ElectricMenu electricMenu = mongoTemplate.findOne(new Query(Criteria.where("_id").is(menuId)), ElectricMenu.class);
		List<ElectricMenuItem> electricMenuItems = electricMenu.getItems();
		if (electricMenuItems == null) {
			electricMenuItems = new ArrayList<ElectricMenuItem>();
		}
		electricMenuItems.add(electricMenuItem);
		electricMenu.setItems(electricMenuItems);
		mongoTemplate.save(electricMenu);
	}

	public void updateItem(ElectricMenuItem electricMenuItem, ObjectId menuId ) {
		ElectricMenu electricMenu=mongoTemplate.findOne(new Query(Criteria.where("_id").is(menuId).and("items.id").is(electricMenuItem.getId())), ElectricMenu.class);
			if(electricMenu==null){
				ObjectId obj=electricMenuItem.getId();
				ElectricMenu oldElectricMenu=mongoTemplate.findOne(new Query(Criteria.where("items.id").is(obj)),ElectricMenu.class);
				if(electricMenuItem.getPicPath()==null){
					List<ElectricMenuItem> electricMenuItems=oldElectricMenu.getItems();
					for(ElectricMenuItem e:electricMenuItems){
						if(e.getId().equals(electricMenuItem.getId())){
							electricMenuItem.setPicPath(e.getPicPath());
						}
					}
				}
				electricMenuItem.setId(new ObjectId());
				addItem(electricMenuItem, menuId);
				deleteItem(obj, oldElectricMenu.getId());
			}else{
				if(StringUtils.isEmpty(electricMenuItem.getPicPath())){
					Update update=new Update();
					update.set("items.$.title", electricMenuItem.getTitle()).set("items.$.price", electricMenuItem.getPrice()).set("items.$.unit", electricMenuItem.getUnit());
					mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(menuId).and("items.id").is(electricMenuItem.getId())), update, ElectricMenu.class);
				}else{
					Update update=new Update();
					update.set("items.$", electricMenuItem);
					mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(menuId).and("items.id").is(electricMenuItem.getId())), update, ElectricMenu.class);
				}
			}
		
	}

	public void addMenu(ElectricMenu electricMenu, ObjectId merchantId) {
		electricMenu.setMerchantId(merchantId);
		mongoTemplate.insert(electricMenu);
	}

	public void updateMenu(ElectricMenu electricMenu ,ObjectId merchantId) {
		Update update=new Update();
		update.set("name", electricMenu.getName());
		mongoTemplate.updateFirst(new Query(Criteria.where("merchantId").is(merchantId).and("_id").is(electricMenu.getId())), update, ElectricMenu.class);
	}

	public void deleteMenu(ObjectId menuId) {
		mongoTemplate.remove(new Query(Criteria.where("_id").is(menuId)), ElectricMenu.class);
	}

	public ElectricMenuItem findItemByTitle(ObjectId menuId, String itemTitle) {
		ElectricMenu electricMenu = mongoTemplate.findOne(new Query(Criteria.where("_id").is(menuId)), ElectricMenu.class);
		List<ElectricMenuItem> electricMenuItems = electricMenu.getItems();
		for (ElectricMenuItem electricMenuItem : electricMenuItems) {
			if (electricMenuItem.getTitle().equals(itemTitle)) {
				return electricMenuItem;
			}
		}
		return null;
	}

	/**
	 * 只增加menu或者修改menu的基本属性,不修改item
	 */
	public String addMenu(List<ElectricMenu> menus, ObjectId merchantId) {
		for (ElectricMenu electricMenu : menus) {
			if(electricMenu.getName()==null){
				return "类别名称不能为空";
			}
			List<ElectricMenu> electricMenus=mongoTemplate.find(new Query(Criteria.where("name").is(electricMenu.getName()).and("merchantId").is(merchantId)), ElectricMenu.class);
			if(electricMenus.size()!=0){
				return "类别名称已存在";
			}
			electricMenu.setMerchantId(merchantId);
			mongoTemplate.insert(electricMenu);
		}
		return "添加成功";
	}

	public Boolean titleValidate(ObjectId menuId, String title) {
		Boolean result=true;
		ElectricMenu electricMenu = mongoTemplate.findOne(new Query(Criteria.where("_id").is(menuId)), ElectricMenu.class);
		List<ElectricMenuItem> electricMenuItems = electricMenu.getItems();
		if(electricMenuItems==null){
			return result;
		}
		for (ElectricMenuItem e : electricMenuItems) {
			if(e.getTitle().equals(title)){
				result=false;
				return result;
			}
		}
		return result;
	}

}
