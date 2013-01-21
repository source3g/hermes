package com.source3g.hermes.entity.merchant;

import java.util.List;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;
import com.source3g.hermes.entity.dictionary.MerchantTagNode;
import com.source3g.hermes.entity.note.Note;

@Document
public class Merchant extends AbstractEntity {
	private static final long serialVersionUID = -9148277944381440756L;
	@NotEmpty(message = "{merchant.name.not.null}")
	private String name;
	private String addr;
	@NotEmpty(message = "{merchant.account.not.null}")
	private String account;
	@NotEmpty(message = "{merchant.password.not.null}")
	private String password;

	private ObjectId merchantGroupId;

	private List<ObjectId> deviceIds;
	
	private ShortMessage shortMessage=new ShortMessage();
	private MerchantResource merchantResource;
	@DBRef
	private List<MerchantTagNode> merchantTagNodes;
	private List<Note> notes;
	//是否被删除
	private boolean canceled;
	
	private Setting setting=new Setting();
	
	
	public List<MerchantTagNode> getMerchantTagNodes() {
		return merchantTagNodes;
	}

	public void setMerchantTagNodes(List<MerchantTagNode> merchantTagNodes) {
		this.merchantTagNodes=merchantTagNodes;
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

	public ShortMessage getShortMessage() {
		return shortMessage;
	}

	public void setShortMessage(ShortMessage shortMessage) {
		this.shortMessage = shortMessage;
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
	
}
