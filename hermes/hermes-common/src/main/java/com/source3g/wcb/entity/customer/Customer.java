package com.source3g.wcb.entity.customer;

import java.util.List;

import com.source3g.wcb.entity.AbstractEntity;
import com.source3g.wcb.enums.Sex;
public class Customer extends AbstractEntity{
	private String name;
	private Sex sex;
	private List<Contact> contact;
	
	
	public Sex getSex() {
		return sex;
	}


	public void setSex(Sex sex) {
		this.sex = sex;
	}


	public List<Contact> getContact() {
		return contact;
	}


	public void setContact(List<Contact> contact) {
		this.contact = contact;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
}
