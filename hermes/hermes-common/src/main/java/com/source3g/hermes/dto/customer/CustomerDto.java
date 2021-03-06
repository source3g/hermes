package com.source3g.hermes.dto.customer;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.source3g.hermes.entity.customer.CallRecord;
import com.source3g.hermes.enums.Sex;

public class CustomerDto {
	private String name;
	private Sex sex;
	private String birthday;
	private String phone;
	private boolean blackList;
	private String address;
	private String qq;
	private String email;
	private String note;

	private List<CallRecord> callRecords;

	private String favorite;

	private Date lastCallInTime; // 最后通电话时间
	private Integer lastCallInDuration; // 最后通话时长
	private ObjectId groupId;
	private Date operateTime;

	private List<RemindDto> reminds;

	public String getCallInCount() {
		if (callRecords == null) {
			return "0";
		}
		return String.valueOf(callRecords.size());
	}

	public void setCallInCount(String callInCount) {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isBlackList() {
		return blackList;
	}

	public void setBlackList(boolean blackList) {
		this.blackList = blackList;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<CallRecord> getCallRecords() {
		return callRecords;
	}

	public void setCallRecords(List<CallRecord> callRecords) {
		this.callRecords = callRecords;
	}

	public Date getLastCallInTime() {
		return lastCallInTime;
	}

	public void setLastCallInTime(Date lastCallInTime) {
		this.lastCallInTime = lastCallInTime;
	}

	public ObjectId getGroupId() {
		return groupId;
	}

	public void setGroupId(ObjectId groupId) {
		this.groupId = groupId;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public List<RemindDto> getReminds() {
		return reminds;
	}

	public void setReminds(List<RemindDto> reminds) {
		this.reminds = reminds;
	}

	public String getFavorite() {
		return favorite;
	}

	public void setFavorite(Boolean favorite) {
		if (favorite == null) {
			this.favorite = "false";
		} else {
			this.favorite = favorite.toString();
		}
	}

	public Integer getLastCallInDuration() {
		return lastCallInDuration;
	}

	public void setLastCallInDuration(Integer lastCallInDuration) {
		this.lastCallInDuration = lastCallInDuration;
	}


}