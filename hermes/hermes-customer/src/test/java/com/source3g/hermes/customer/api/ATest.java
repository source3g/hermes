package com.source3g.hermes.customer.api;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class ATest {

	@Test
	public void testApi() {
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("a", "a");

		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
		formData.add("type", "add");
		formData.add("count", "100");

		/*
		 * HttpHeaders httpHeaders = new HttpHeaders();
		 * httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		 */
	//	HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(formData,);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, httpHeaders);
		
		// http://localhost:8866/hermes-api/merchant/chargeMsg/50aed7f50cbb487fbf00c259/
		restTemplate.postForObject("http://localhost:8866/hermes-api/merchant/chargeMsg/50aed7f50cbb487fbf00c259/", requestEntity, String.class);
		// restTemplate.postForObject("http://localhost:8866/hermes-api/customer/importA",map
		// , String.class);
	}

}
