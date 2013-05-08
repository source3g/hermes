package com.source3g.hermes.api.install;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.sourse3g.hermes.apkVersion.OnlineVersion;

public class InitDb {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
		MongoTemplate mongoTemplate = (MongoTemplate) applicationContext.getBean("mongoTemplate");
		OnlineVersion onlineVersion = new OnlineVersion();
		onlineVersion.setCode(0);
		mongoTemplate.save(onlineVersion);
	}
}
