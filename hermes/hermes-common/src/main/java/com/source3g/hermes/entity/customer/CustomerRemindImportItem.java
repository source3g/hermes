package com.source3g.hermes.entity.customer;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class CustomerRemindImportItem extends AbstractEntity {
	private static final long serialVersionUID = -1666811215606533985L;
	// @DBRef
	private ObjectId importLogId;

	private String phone;
	private String remindTitle;
	private Date remindTime;

	public ObjectId getImportLogId() {
		return importLogId;
	}

	public void setImportLogId(ObjectId importLogId) {
		this.importLogId = importLogId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRemindTitle() {
		return remindTitle;
	}

	public void setRemindTitle(String remindTitle) {
		this.remindTitle = remindTitle;
	}

	public Date getRemindTime() {
		return remindTime;
	}

	public void setRemindTime(Date remindTime) {
		this.remindTime = remindTime;
	}

}
