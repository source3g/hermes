package com.source3g.hermes.entity.log;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class ErrorLogTest {

	@Test
	public void testToJson() throws JsonGenerationException, JsonMappingException, IOException {
		ErrorLog errorLog=new ErrorLog();
		errorLog.setContent("老毛病又犯了");
		errorLog.setReportTime(new Date());
		errorLog.setContent("001");
		ObjectMapper objectMapper=new ObjectMapper();
		String str=objectMapper.writeValueAsString(errorLog);
		System.out.println(str);
		Assert.assertNotNull(str);
	}

}
