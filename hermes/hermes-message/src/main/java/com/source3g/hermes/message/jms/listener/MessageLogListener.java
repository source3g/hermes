package com.source3g.hermes.message.jms.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.source3g.hermes.entity.message.MessageSendLog;
import com.source3g.hermes.message.service.MessageService;

@Component
public class MessageLogListener implements MessageListener{
	
	@Autowired
	private MessageService messageService;
	
	@Override
	public void onMessage(Message message) {
		if (message instanceof ObjectMessage) {
			ObjectMessage objectMessage = (ObjectMessage) message;
			try {
				Object obj=objectMessage.getObject();
				if (obj instanceof MessageSendLog[]) {
					MessageSendLog[] logs = (MessageSendLog[]) obj;
					for (MessageSendLog log: logs) {
						messageService.addLog(log);
					}
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
			
		}

	}
}
