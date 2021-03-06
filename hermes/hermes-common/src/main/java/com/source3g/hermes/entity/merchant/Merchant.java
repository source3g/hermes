package com.source3g.hermes.entity.merchant;

import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;
import com.source3g.hermes.entity.dictionary.MerchantTagNode;
import com.source3g.hermes.entity.note.Note;
import com.source3g.hermes.enums.MerchantMessageType;

@Document
public class Merchant extends AbstractEntity {
	private static final long serialVersionUID = -9148277944381440756L;
	@NotEmpty(message = "{merchant.name.not.null}")
	@Indexed(unique = true)
	private String name;
	private String addr;
	@NotEmpty(message = "{merchant.account.not.null}")
	@Indexed(unique = true)
	private String account;
	@NotEmpty(message = "{merchant.password.not.null}")
	private String password;

	private ObjectId merchantGroupId;

	private Set<ObjectId> deviceIds;

	private MessageBalance messageBalance = new MessageBalance();
	private MerchantResource merchantResource;
	@DBRef
	private List<MerchantTagNode> merchantTagNodes;
	private List<Note> notes;
	// 是否被删除
	private boolean canceled;
	private Setting setting = new Setting();
	private ObjectId salerId;

	private MerchantMessageType merchantMessageType = MerchantMessageType.普通商户;

	public ObjectId getSalerId() {
		return salerId;
	}

	public void setSalerId(ObjectId salerId) {
		this.salerId = salerId;
	}

	public List<MerchantTagNode> getMerchantTagNodes() {
		return merchantTagNodes;
	}

	public void setMerchantTagNodes(List<MerchantTagNode> merchantTagNodes) {
		this.merchantTagNodes = merchantTagNodes;
	}

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

	public MessageBalance getMessageBalance() {
		return messageBalance;
	}

	public void setMessageBalance(MessageBalance messageBalance) {
		this.messageBalance = messageBalance;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public Setting getSetting() {
		return setting;
	}

	public void setSetting(Setting setting) {
		this.setting = setting;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public MerchantResource getMerchantResource() {
		return merchantResource;
	}

	public void setMerchantResource(MerchantResource merchantResource) {
		this.merchantResource = merchantResource;
	}

	public Set<ObjectId> getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(Set<ObjectId> deviceIds) {
		this.deviceIds = deviceIds;
	}

	public MerchantMessageType getMerchantMessageType() {
		return merchantMessageType;
	}

	public void setMerchantMessageType(MerchantMessageType merchantMessageType) {
		this.merchantMessageType = merchantMessageType;
	}
}
