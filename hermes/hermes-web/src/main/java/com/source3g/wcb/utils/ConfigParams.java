package com.source3g.wcb.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigParams {

	private static ConfigParams api;

	public ConfigParams() {
		api = this;
	}

	@Value(value = "${baseApiUrl}")
	private String baseUrl;

	public static String getBaseUrl() {
		if (api == null) {
			return null;
		}
		return api.baseUrl;
	}
}
