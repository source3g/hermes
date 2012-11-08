package com.source3g.hermes.merchant.api;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.source3g.hermes.entity.merchant.MerchantGroup;


public class MerchantGroupApiTest{
	@Test
	public void testAdd() throws JsonGenerationException, JsonMappingException, IOException {
		String uri = "http://localhost:8080/api/merchantGroup/509881abf83dd803d31b5681/";
		RestTemplate template = new RestTemplate();
		try{
			
			template.delete(uri);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testListAll(){
		String uri = "http://localhost:8080/api/merchantGroup/";
		RestTemplate restTemplate = new RestTemplate();
		MerchantGroup[] merchantGroups = restTemplate.getForObject(uri, MerchantGroup[].class);
		System.out.println(merchantGroups.length);
	}
	
}