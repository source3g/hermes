package com.source3g.wcb.main;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class MainTest {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "classpath*:spring/applicationContext*.xml", "classpath*:spring/applicationContext-mongo.xml" });
		ActiveMQConnectionFactory user=(ActiveMQConnectionFactory)context.getBean("jmsFactory");
		System.out.println(user);
	}
	
}
