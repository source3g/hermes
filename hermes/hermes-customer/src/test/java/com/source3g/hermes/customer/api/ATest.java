package com.source3g.hermes.customer.api;

import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class ATest {
	
	@Test
	public void testApi(){
		RestTemplate restTemplate=new RestTemplate();
		MultiValueMap<String, String> map=new LinkedMultiValueMap<String, String>();
		map.add("a", "a");
		restTemplate.postForObject("http://localhost:8866/hermes-api/customer/importA",map , String.class);
	}

}
