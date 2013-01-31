package com.source3g.hermes.entity.log;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.jms.Destination;

import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class FailedJms extends AbstractEntity {
	private static final long serialVersionUID = -8007041559951819341L;

	private Destination destination;
	private Serializable message;
	private Map<String, String> properties;
	private Date failedTime;

	public Date getFailedTime() {
		return failedTime;
	}

	public void setFailedTime(Date failedTime) {
		this.failedTime = failedTime;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public Serializable getMessage() {
		return message;
	}

	public void setMessage(Serializable message) {
		this.message = message;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}
