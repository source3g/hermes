package com.source3g.hermes.main;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.advisory.AdvisorySupport;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.command.RemoveInfo;
import org.springframework.stereotype.Component;

@Component
public class JmsMessageListener implements MessageListener {
	
	@Override
	public void onMessage(Message message) {
		if(message instanceof ActiveMQMessage){
			ActiveMQMessage mqMessage=(ActiveMQMessage)message;
			if(mqMessage.getDataStructure() instanceof ConsumerInfo){
				ConsumerInfo consumerInfo=(ConsumerInfo)mqMessage.getDataStructure();
				System.out.println((consumerInfo.getDestination().getPhysicalName())+"上来了一个客户端："+consumerInfo.getSubscriptionName());
			}else if(mqMessage.getDataStructure() instanceof RemoveInfo){
//				RemoveInfo removeInfo=(RemoveInfo)mqMessage.getDataStructure();
				if(mqMessage.getDestination().getPhysicalName().startsWith(AdvisorySupport.CONSUMER_ADVISORY_TOPIC_PREFIX)){
					System.out.println(mqMessage.getDestination().getPhysicalName().substring(AdvisorySupport.CONSUMER_ADVISORY_TOPIC_PREFIX.length(),mqMessage.getDestination().getPhysicalName().length())+"掉了一个客户端");
				}
				
			
			}
		}
	}

}
