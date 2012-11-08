package com.source3g.wcb.merchant.api;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import com.source3g.wcb.entity.merchant.Merchant;


public class MerchantApiTest{
	@Test
	public void testAdd() throws JsonGenerationException, JsonMappingException, IOException {
		String uri = "http://localhost:8080/api/merchant/";
		RestTemplate template = new RestTemplate();
		Merchant merchant=new Merchant();
		merchant.setName("1234");
		merchant.setAddr("343");
		HttpEntity<Merchant> entity = new HttpEntity<Merchant>(merchant);

		String result = template.postForObject(uri, entity, String.class);
		System.out.println(result);
	}
	
}