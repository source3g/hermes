package com.source3g.hermes.entity.security;

import com.source3g.hermes.entity.AbstractEntity;

public abstract class AbstractAccount extends AbstractEntity {
	private static final long serialVersionUID = -5421464769220114108L;
	private String account;
	private String password;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}