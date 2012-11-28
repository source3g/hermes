package com.source3g.hermes.sync.scheduler;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.service.TaskService;

@Component
public class IncrementScheduler {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private TaskService taskService;
	

	public void packageIncrement() {
		List<Merchant> merchantList=mongoTemplate.findAll(Merchant.class);
		for (Merchant merchant:merchantList){
			packageIncrement(merchant);
		}
	}
	
	private void packageIncrement(Merchant merchant){
		try {
			taskService.packageIncrement(merchant);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
