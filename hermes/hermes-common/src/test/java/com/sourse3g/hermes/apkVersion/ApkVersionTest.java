package com.sourse3g.hermes.apkVersion;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class ApkVersionTest {

	@Test
	public void entity() throws JsonGenerationException, JsonMappingException, IOException {
		ApkVersion apkVersion = new ApkVersion();
		apkVersion.setApkVersion("1");
		apkVersion.setCode(2);
		apkVersion.setDescribe("本次更新了新内容");
		apkVersion.setUrl("http://www.baidu.com");
		ObjectMapper mapper=new ObjectMapper();
		String str=mapper.writeValueAsString(apkVersion);
		System.out.println(str);
		Assert.assertNotNull(str);
	}
}
