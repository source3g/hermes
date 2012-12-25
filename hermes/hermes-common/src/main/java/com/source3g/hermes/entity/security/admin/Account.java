package com.source3g.hermes.entity.security.admin;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.security.AbstractAccount;

@Document
public class Account extends AbstractAccount {
	private static final long serialVersionUID = -1538678453736395846L;
	private String name;
	private String note;
	@DBRef
	private List<Role> roles;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}
