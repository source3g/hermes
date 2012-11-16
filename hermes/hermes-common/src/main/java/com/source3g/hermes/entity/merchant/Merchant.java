package com.source3g.hermes.entity.merchant;

import java.util.List;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;

import com.source3g.hermes.entity.AbstractEntity;

public class Merchant extends AbstractEntity {
	@NotEmpty(message = "name can not be null")
	private String name;
	@NotEmpty(message = "addr can not be null")
	private String addr;
	@NotEmpty(message = "account can not be null")
	private String  account;
	@NotEmpty(message = "password can not be null")
	private String password;
	
	private ObjectId merchantGroupId;
	
	private List<ObjectId> deviceIds;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public ObjectId getMerchantGroupId() {
		return merchantGroupId;
	}

	public void setMerchantGroupId(ObjectId merchantGroupId) {
		this.merchantGroupId = merchantGroupId;
	}

	public List<ObjectId> getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(List<ObjectId> deviceIds) {
		this.deviceIds = deviceIds;
	}

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
