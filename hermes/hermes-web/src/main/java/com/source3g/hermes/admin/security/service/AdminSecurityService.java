package com.source3g.hermes.admin.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.source3g.hermes.entity.security.admin.Account;
import com.source3g.hermes.utils.ConfigParams;

@Service
public class AdminSecurityService {
	@Autowired
	private RestTemplate restTemplate;

	public Account login(String username, String password) {
		String url = ConfigParams.getBaseUrl() + "admin/security/login";
		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
		formData.add("username", username);
		formData.add("password", password);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, httpHeaders);
		Account account = restTemplate.postForObject(url, requestEntity, Account.class);
		if (account == null || account.getAccount() == null || account.getPassword() == null) {
			return null;
		}
		return account;
	}
}
