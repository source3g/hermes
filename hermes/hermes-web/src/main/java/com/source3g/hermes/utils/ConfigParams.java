package com.source3g.hermes.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class 	ConfigParams {

	private static ConfigParams params;

	public ConfigParams() {
		params = this;
	}

	@Value(value = "${baseApiUrl}")
	private String baseUrl;

	public static String getBaseUrl() {
		if (params == null) {
			return null;
		}
		return params.baseUrl;
	}
}
