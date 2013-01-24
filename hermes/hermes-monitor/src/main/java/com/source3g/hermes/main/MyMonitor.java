package com.source3g.hermes.main;

import java.util.Collection;

import org.apache.activemq.broker.jmx.TopicViewMBean;
import org.apache.activemq.web.RemoteJMXBrokerFacade;
import org.apache.activemq.web.config.SystemPropertiesConfiguration;

public class MyMonitor {
	public static void main(String[] args) throws Exception {

		RemoteJMXBrokerFacade createConnector = new RemoteJMXBrokerFacade();
		// 填写链接属性
		System.setProperty("webconsole.jmx.url", "service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi");
		System.setProperty("webconsole.jmx.user", "admin");
		System.setProperty("webconsole.jmx.password", "activemq");
		// 创建配置
		SystemPropertiesConfiguration configuration = new SystemPropertiesConfiguration();
		// 创建链接
		createConnector.setConfiguration(configuration);
		System.out.println("=============Topic =================");
		Collection<TopicViewMBean> topicViewList = createConnector.getTopics();
		for (TopicViewMBean topicViewMBean : topicViewList) {
			System.out.println("beanName =" + topicViewMBean.getName());
			System.out.println("ConsumerCount =" + topicViewMBean.getConsumerCount());
			System.out.println("DequeueCount =" + topicViewMBean.getDequeueCount());
			System.out.println("EnqueueCount =" + topicViewMBean.getEnqueueCount());
			System.out.println("DispatchCount =" + topicViewMBean.getDispatchCount());
			System.out.println("ExpiredCount =" + topicViewMBean.getExpiredCount());
			System.out.println("MaxEnqueueTime =" + topicViewMBean.getMaxEnqueueTime());
			System.out.println("ProducerCount =" + topicViewMBean.getProducerCount());
			System.out.println("MemoryPercentUsage =" + topicViewMBean.getMemoryPercentUsage());
			System.out.println("MemoryLimit =" + topicViewMBean.getMemoryLimit());
		}
	}
}
