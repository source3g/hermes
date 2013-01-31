package com.source3g.hermes.dto.jms;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.bson.types.ObjectId;

public class FailedJmsDto {
	private ObjectId id;
	private String destination;
	private Map<String, String> properties;
	private Date failedTime;
	private Serializable message;
	
	
	public Serializable getMessage() {
		return message;
	}
	public void setMessage(Serializable message) {
		this.message = message;
	}
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public Map<String, String> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	public Date getFailedTime() {
		return failedTime;
	}
	public void setFailedTime(Date failedTime) {
		this.failedTime = failedTime;
	}
	
}
