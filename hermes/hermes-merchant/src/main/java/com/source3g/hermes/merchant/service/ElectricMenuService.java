package com.source3g.hermes.merchant.service;

import java.util.List;

import javax.jms.Destination;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.source3g.hermes.constants.JmsConstants;
import com.source3g.hermes.entity.merchant.ElectricMenu;
import com.source3g.hermes.entity.merchant.ElectricMenuItem;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.service.JmsService;

@Component
public class ElectricMenuService extends BaseService {
	@Autowired
	private JmsService jmsService;
	@Autowired
	private Destination syncDestination;
	@Autowired
	private MongoTemplate mongoTemplate;

	public List<ElectricMenu> findByMerchantId(ObjectId merchantId) {
		List<ElectricMenu> electricMenus = mongoTemplate.find(new Query(Criteria.where("merchantId").is(merchantId)), ElectricMenu.class);
		return electricMenus;
	}

	public void deleteItem(ObjectId itemId, ObjectId menuId) {
		Update update = new Update();
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.append("_id", itemId);
		update.pull("items", basicDBObject);
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(menuId)), update, ElectricMenu.class);
	}

	public void addItem(ElectricMenuItem electricMenuItem, ObjectId menuId) throws Exception {
		ElectricMenu electricMenu = mongoTemplate.findOne(new Query(Criteria.where("_id").is(menuId).and("items.title").is(electricMenuItem.getTitle())), ElectricMenu.class);
		if (electricMenu != null) {
			throw new Exception("名称已存在");
		}
		Update update = new Update();
		update.addToSet("items", electricMenuItem);
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(menuId)), update, ElectricMenu.class);
	}

	public void updateItem(ElectricMenuItem electricMenuItem, ObjectId menuId) throws Exception {
		ElectricMenu electricMenu = mongoTemplate.findOne(new Query(Criteria.where("_id").is(menuId).and("items.id").is(electricMenuItem.getId())), ElectricMenu.class);
		// 如果menu下没有electricMenu，那么就是从别的菜单转过来的
		if (electricMenu == null) {
			ObjectId itemId = electricMenuItem.getId();
			ElectricMenu oldElectricMenu = mongoTemplate.findOne(new Query(Criteria.where("items.id").is(itemId)), ElectricMenu.class);
			if (electricMenuItem.getPicPath() == null) {
				List<ElectricMenuItem> electricMenuItems = oldElectricMenu.getItems();
				for (ElectricMenuItem e : electricMenuItems) {
					if (e.getId().equals(electricMenuItem.getId())) {
						electricMenuItem.setPicPath(e.getPicPath());
					}
				}
			}
			deleteItem(itemId, oldElectricMenu.getId());
			addItem(electricMenuItem, menuId);
		} else {
			if (StringUtils.isEmpty(electricMenuItem.getPicPath())) {
				Update update = new Update();
				update.set("items.$.title", electricMenuItem.getTitle()).set("items.$.price", electricMenuItem.getPrice()).set("items.$.unit", electricMenuItem.getUnit());
				mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(menuId).and("items.id").is(electricMenuItem.getId())), update, ElectricMenu.class);
			} else {
				Update update = new Update();
				update.set("items.$", electricMenuItem);
				mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(menuId).and("items.id").is(electricMenuItem.getId())), update, ElectricMenu.class);
			}
		}
	}

	public void addMenu(ElectricMenu electricMenu, ObjectId merchantId) {
		electricMenu.setMerchantId(merchantId);
		mongoTemplate.insert(electricMenu);
	}

	public void updateMenu(ElectricMenu electricMenu, ObjectId merchantId) throws Exception {
		Update update = new Update();
		List<ElectricMenu> electricMenus = mongoTemplate.find(new Query(Criteria.where("merchantId").is(merchantId).and("name").is(electricMenu.getName())), ElectricMenu.class);
		if (electricMenus.size() != 0) {
			throw new Exception("菜单名称已存在");
		}
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
	 * 
	 * @throws Exception
	 */
	public void addMenu(List<ElectricMenu> menus, ObjectId merchantId) throws Exception {
		for (ElectricMenu electricMenu : menus) {
			if (electricMenu.getName() == null) {
				throw new Exception("类别名称不能为空");
			}
			List<ElectricMenu> electricMenus = mongoTemplate.find(new Query(Criteria.where("name").is(electricMenu.getName()).and("merchantId").is(merchantId)), ElectricMenu.class);
			if (electricMenus.size() != 0) {
				throw new Exception("类别名称已存在");
			}
			electricMenu.setMerchantId(merchantId);
			mongoTemplate.insert(electricMenu);
		}
	}

	public Boolean hasTitle(ObjectId menuId, String title) {
		ElectricMenu electricMenu = mongoTemplate.findOne(new Query(Criteria.where("_id").is(menuId).and("items.title").is(title)), ElectricMenu.class);
		if (electricMenu != null) {
			return true;
		}
		return false;
	}

	public void sync(ObjectId merchantId) {
		jmsService.sendString(syncDestination, merchantId.toString(), JmsConstants.TYPE, JmsConstants.PACKAGE_ELECTRIC);
	}

}
