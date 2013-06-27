package com.source3g.hermes.message.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.message.ShortMessage;

@Service
public class PositiveSenderService implements ApplicationContextAware {
	private static final Logger logger = LoggerFactory.getLogger(PositiveSenderService.class);
	@Autowired
	private MessageQueueService messageQueueService;

	private AbstractPositiveMessageService abstractPositiveMessageService;

	private static ApplicationContext applicationContext;
	@Value(value = "${message.channel}")
	private String channel;

	@PostConstruct
	public void init() {
		Object service = applicationContext.getBean(channel);
		if (service instanceof AbstractPositiveMessageService) {
			abstractPositiveMessageService = (AbstractPositiveMessageService) service;
			for (int i = 0; i < 5; i++) {
				Thread thread = new Thread(new MessagePickThread());
				thread.start();
			}
		}
	}

	class MessagePickThread implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					ShortMessage shortMessage = messageQueueService.poll();
					logger.debug("开始检测短信:" + shortMessage);
					if (shortMessage != null) {
						abstractPositiveMessageService.sendMessage(shortMessage);
					}
					Thread.sleep(1000L);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}
}
