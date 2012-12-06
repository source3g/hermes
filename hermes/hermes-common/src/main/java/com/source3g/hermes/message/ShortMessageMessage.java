package com.source3g.hermes.message;

import java.io.Serializable;
import java.util.List;

import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.enums.MessageType;

public class ShortMessageMessage implements Serializable {

	private static final long serialVersionUID = 4430012271716760588L;
	
	private List<Customer> customers;
	private String content;
	private MessageType messageType;
	public List<Customer> getCustomers() {
		return customers;
	}
	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public MessageType getMessageType() {
		return messageType;
	}
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	
	
	
}
