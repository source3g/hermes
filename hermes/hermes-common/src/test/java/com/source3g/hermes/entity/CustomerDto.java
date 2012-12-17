package com.source3g.hermes.entity;

import java.util.List;

import com.source3g.hermes.entity.customer.Remind;

public class CustomerDto{
		private String name;
		private String sex;
		private String birthday;
		private String phone;
		private boolean blackList;
		private String address;
		private String qq;
		private String email;
		private List<Remind> reminds;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
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
		public List<Remind> getReminds() {
			return reminds;
		}
		public void setReminds(List<Remind> reminds) {
			this.reminds = reminds;
		}
	}