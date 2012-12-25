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

import com.source3g.hermes.entity.security.admin.Account;
import com.source3g.hermes.entity.security.admin.Resource;
import com.source3g.hermes.entity.security.admin.Role;
import com.source3g.hermes.utils.Page;

@Service
public class SecurityService {

	@Autowired
	private MongoTemplate mongoTemplate;

	public void addAccount(Account account) throws Exception {
		List<Account> list=mongoTemplate.find(new Query(Criteria.where("account").is(account.getAccount())), Account.class);
		if(list.size()==0){
			mongoTemplate.insert(account);
		}else{
		throw new Exception("账号已存在");
		}
	}

	public Boolean accountValidate(String account) {
		List<Account> list=mongoTemplate.find(new Query(Criteria.where("account").is(account)), Account.class);
		if(list.size()==0){
			return true;
		}
			return false;
			
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
		List<Resource> nameList=mongoTemplate.find(new Query(Criteria.where("name").is(resource.getName())), Resource.class);
		List<Resource> codeList=mongoTemplate.find(new Query(Criteria.where("code").is(resource.getCode())), Resource.class);
		if(nameList.size()==0&codeList.size()==0){
			mongoTemplate.insert(resource);
		}else{
		return;
		}
	}
	
	public Boolean resourceValidate(String name, String code) {
		List<Resource> nameList=mongoTemplate.find(new Query(Criteria.where("name").is(name)), Resource.class);
		List<Resource> codeList=mongoTemplate.find(new Query(Criteria.where("code").is(code)), Resource.class);
		if(nameList.size()==0&codeList.size()==0){
			return true;
		}else{
			return false;
		}
	}
	
	public Boolean nameValidate(String name) {
		List<Role> list=mongoTemplate.find(new Query(Criteria.where("name").is(name)), Role.class);
		if(list.size()==0){
			return true;
		}
			return false;
		
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

	public void addRole(String name, String[] resourceIds) throws Exception {
		List<Role> list=mongoTemplate.find(new Query(Criteria.where("name").is(name)), Role.class);
		if(list.size()==0){
			Role role = new Role();
			role.setId(ObjectId.get());
			role.setName(name);
			if(resourceIds!=null){
			List<Resource> resources = new ArrayList<Resource>();
			for (String id : resourceIds) {
				Resource resource = new Resource();
				resource.setId(id);
				resources.add(resource);
			}
			role.setResources(resources);
			}
			mongoTemplate.save(role);
		}else{
		throw new Exception("账号已存在");
		}
	}


	public Role getRoleById(String id) {
		return mongoTemplate.findById(new ObjectId(id), Role.class);
	}

	public List<Role> listRole() {
		return mongoTemplate.findAll(Role.class);
	}

	public void update(Role role) {
		mongoTemplate.save(role);
	}

	public Account getAccountById(String id) {
		return mongoTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(id))), Account.class);
	}

	public void grant(String id, String[] roleIdArray) {
		Account account = getAccountById(id);
		List<Role> roles = null;
		if (account.getRoles() != null) {
			roles = account.getRoles();
		} else {
			roles = new ArrayList<Role>();
		}

		for (String roleId : roleIdArray) {
			Role role = new Role();
			role.setId(roleId);
			roles.add(role);
		}
		account.setRoles(roles);
		mongoTemplate.save(account);
	}

	public void recoverRole(String accountId, String id) {
		Account account = getAccountById(accountId);
		if (account.getRoles() != null) {
			Role roleToRemove = null;
			for (Role role : account.getRoles()) {
				if (role.getId().equals(id)) {
					roleToRemove = role;
				}
			}
			account.getRoles().remove(roleToRemove);
			mongoTemplate.save(account);
		}
	}

	public void deleteAccount(String accountId) {
		mongoTemplate.remove(new Query(Criteria.where("_id").is(new ObjectId(accountId))), Account.class);
	}

	public Account login(String username, String password) {
		return mongoTemplate.findOne(new Query(Criteria.where("account").is(username).and("password").is(password)), Account.class);
	}

	public Account findUserByLoginName(String loginName) {
		return mongoTemplate.findOne(new Query(Criteria.where("account").is(loginName)), Account.class);
	}
}
