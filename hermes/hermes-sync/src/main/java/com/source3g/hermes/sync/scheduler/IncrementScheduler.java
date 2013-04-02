package com.source3g.hermes.sync.scheduler;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.service.TaskService;

@Component
public class IncrementScheduler {
	private static Logger logger = LoggerFactory.getLogger(IncrementScheduler.class);
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private TaskService taskService;

	public void packageIncrement() {
		logger.debug("开始增量任务Log");
		List<Merchant> merchantList = mongoTemplate.findAll(Merchant.class);
		for (Merchant merchant : merchantList) {
			packageIncrement(merchant);
		}
		logger.debug("增量任务完成");
	}

	private void packageIncrement(Merchant merchant) {
		try {
			taskService.packageIncrement(merchant);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
