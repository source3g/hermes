package com.source3g.hermes.merchant.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.utils.ConfigParams;

@Service
public class MerchantSecurityService {

	@Autowired
	private RestTemplate restTemplate;

	public Merchant login(String username, String password) {
		String url=ConfigParams.getBaseUrl()+"merchant/login";
		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
		formData.add("username", username);
		formData.add("password", password);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, httpHeaders);
		Merchant merchant=restTemplate.postForObject(url, requestEntity, Merchant.class);
		return merchant;
	}

}
