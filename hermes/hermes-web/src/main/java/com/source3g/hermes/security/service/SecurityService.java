package com.source3g.hermes.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.source3g.hermes.security.entity.Account;

@Service
public class SecurityService {

	@Autowired
	private MongoTemplate mongoTemplate;

	public void addAccount(Account account) {
		mongoTemplate.insert(account);
	}

}
