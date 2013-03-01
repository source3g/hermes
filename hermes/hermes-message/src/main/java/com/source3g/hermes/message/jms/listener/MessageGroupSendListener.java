package com.source3g.hermes.message.jms.listener;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.enums.MessageType;
import com.source3g.hermes.message.GroupSendMsg;
import com.source3g.hermes.message.service.MessageService;
import com.source3g.hermes.utils.JmsUtils;

@Component
public class MessageGroupSendListener implements MessageListener {

	@Autowired
	private MessageService messageService;

	@Override
	public void onMessage(Message message) {
		try {
			GroupSendMsg groupSendMsg = JmsUtils.getObject(message, GroupSendMsg.class);
			if (groupSendMsg.getIds() != null && groupSendMsg.getIds().length > 0) {
				List<Customer> customerList = messageService.findCustomerByGroupIds(groupSendMsg.getIds());
				for (Customer c : customerList) {
					messageService.sendMessage(c, groupSendMsg.getContent(), MessageType.群发,groupSendMsg.getGroupLogId());
				}
			}
			if (groupSendMsg.getPhoneArray() != null && groupSendMsg.getPhoneArray().length > 0) {
				for (String phone : groupSendMsg.getPhoneArray()) {
					messageService.sendMessage(groupSendMsg.getMerchantId(), phone, groupSendMsg.getContent(), MessageType.群发,groupSendMsg.getGroupLogId());
					//System.out.println("发了一个短信"+phone);
				}
			}

		} catch (JMSException e) {
			e.printStackTrace();
		}

		System.out.println(message);
	}

}
