package com.source3g.hermes.entity.sim;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class SimInfo extends AbstractEntity {
	private static final long serialVersionUID = 3387120404561117471L;

	@Indexed(unique = true)
	private String serviceNumber;
	private String username;
	private String simUimCardNo;
	private String imsiNo;

	public String getServiceNumber() {
		return serviceNumber;
	}

	public void setServiceNumber(String serviceNumber) {
		this.serviceNumber = serviceNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSimUimCardNo() {
		return simUimCardNo;
	}

	public void setSimUimCardNo(String simUimCardNo) {
		this.simUimCardNo = simUimCardNo;
	}

	public String getImsiNo() {
		return imsiNo;
	}

	public void setImsiNo(String imsiNo) {
		this.imsiNo = imsiNo;
	}
	

}
