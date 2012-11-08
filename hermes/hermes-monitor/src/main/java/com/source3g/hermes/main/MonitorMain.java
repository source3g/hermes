package com.source3g.hermes.main;

import javax.jms.JMSException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class MonitorMain {

	public static void main(String[] args) throws JMSException {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] { "classpath*:spring/applicationContext-monitor-jms.xml" });
		/*CachingConnectionFactory monitorTopicListenerConnectionFactory = (CachingConnectionFactory) applicationContext.getBean("monitorTopicListenerConnectionFactory");
		Session session = monitorTopicListenerConnectionFactory.createConnection().createSession(true, Session.CLIENT_ACKNOWLEDGE);
		Destination destination = (Destination) applicationContext.getBean("monitorDestination");
		Destination advisoryDestination = AdvisorySupport.getConsumerAdvisoryTopic(destination);
		MessageConsumer messageConsumer = session.createConsumer(advisoryDestination);
		JmsMessageListener jmsMessageListener = (JmsMessageListener)applicationContext.getBean("jmsMessageListener");
		messageConsumer.setMessageListener(jmsMessageListener);*/
		System.out.println(applicationContext);
	}

}
