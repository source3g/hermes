package com.source3g.wcb.api.config;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {
	
	/**
	 * 去掉json中的空
	 * @return
	 * @throws Exception
	 */
	public @Bean ObjectMapper objectMapper() throws Exception{
		ObjectMapper obj=new ObjectMapper();
//		obj.getSerializationConfig().withSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
		obj.getSerializationConfig().setSerializationInclusion(org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL);
		return obj;
	}
}
