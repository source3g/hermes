package com.source3g.hermes.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.source3g.hermes.entity.merchant.Merchant;

@Component
public class IncrementScheduler {
	@Autowired
	private MongoTemplate mongoTemplate;

	public void packageIncrement() {
		List<Merchant> merchantList=mongoTemplate.findAll(Merchant.class);
		for (Merchant merchant:merchantList){
			packageIncrement(merchant);
		}
	}
	
	private void packageIncrement(Merchant merchant){
		
	}

}
