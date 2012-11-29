package com.source3g.hermes.service;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ResourceTest {

	@Test
	public void testResource() throws IOException {
		Resource resource = new ClassPathResource("taskfiles/1.sh");
		File resourceFile = resource.getFile();
		System.out.println(resourceFile.getAbsolutePath());
		Assert.assertEquals(true, resourceFile.exists());
	}
}
