package com.source3g.hermes.message.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy(value = false)
public class PositiveRecvService implements ApplicationContextAware {
	private static final Logger logger = LoggerFactory.getLogger(PositiveRecvService.class);

	private AbstractPositiveMessageService abstractPositiveMessageService;

	private static ApplicationContext applicationContext;
	@Value(value = "${message.channel}")
	private String channel;

	@PostConstruct
	public void init() {
		Object service = applicationContext.getBean(channel);
		if (service instanceof AbstractPositiveMessageService) {
			abstractPositiveMessageService = (AbstractPositiveMessageService) service;
			// for (int i = 0; i < 5; i++) {
			Thread thread = new Thread(new MessagePickThread());
			thread.start();
			// }
		}
	}

	class MessagePickThread implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					// logger.debug("开始接收短信返回:");
					AbstractRecvService recvService = abstractPositiveMessageService.getRecvService();
					if (recvService != null) {
						recvService.recv();
					}
					Thread.sleep(5000L);
				} catch (Exception e) {
					logger.debug("短信接收失败，原因:" + e.getMessage());
				}
			}
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}
}
