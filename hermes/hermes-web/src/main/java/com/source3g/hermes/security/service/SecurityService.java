package com.source3g.hermes.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.security.entity.Account;
import com.source3g.hermes.security.entity.Resource;
import com.source3g.hermes.security.entity.Role;
import com.source3g.hermes.utils.Page;

@Service
public class SecurityService {

	@Autowired
	private MongoTemplate mongoTemplate;

	public void addAccount(Account account) {
		mongoTemplate.insert(account);
	}

	public Page listAccount(int pageNoInt, String account) {
		Query query = new Query();
		if (StringUtils.isNotEmpty(account)) {
			Pattern pattern = Pattern.compile("^.*" + account + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("account").is(pattern));
		}
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, Account.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNoInt);
		List<Account> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), Account.class);
		page.setData(list);
		return page;
	}

	public void addResource(Resource resource) {
		mongoTemplate.insert(resource);
	}

	public Page listResource(int pageNoInt, String name, String code) {
		Query query = new Query();
		if (StringUtils.isNotEmpty(name)) {
			Pattern pattern = Pattern.compile("^.*" + name + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("name").is(pattern));
		}
		if (StringUtils.isNotEmpty(code)) {
			Pattern pattern = Pattern.compile("^.*" + code + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("code").is(pattern));
		}
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, Resource.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNoInt);
		List<Resource> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), Resource.class);
		page.setData(list);
		return page;
	}

	public void deleteResource(String id) {
		mongoTemplate.remove(new Query(Criteria.where("_id").is(new ObjectId(id))), Resource.class);
	}

	public Resource findResourceByCode(String code) {
		return mongoTemplate.findOne(new Query(Criteria.where("code").is(code)), Resource.class);
	}

	public void addRole(String name, String[] resourceIds) {
		Role role = new Role();
		role.setId(ObjectId.get());
		role.setName(name);
		List<Resource> resources = new ArrayList<Resource>();
		for (String id : resourceIds) {
			Resource resource = new Resource();
			resource.setId(id);
			resources.add(resource);
		}
		role.setResources(resources);
		mongoTemplate.save(role);
	}

	public Role getById(String id) {
		return mongoTemplate.findById(new ObjectId(id), Role.class);
	}

	public List<Role> listRole() {
		return mongoTemplate.findAll(Role.class);
	}

	public void update(Role role) {
		mongoTemplate.save(role);
	}

	public Account findAccountById(String id) {
		return mongoTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(id))), Account.class);
	}

}
