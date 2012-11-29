package com.source3g.hermes.service;

import junit.framework.Assert;
import com.source3g.hermes.sync.utils.TarGZipUtils;

public class TarGzipTest {
	public static void main(String[] args) throws Exception {
	//	File[] filepaths = { new File("D:/temp/testGzip/1/1.sh"), new File("D:/temp/testGzip/1/1.sql") };
	//	File[] sources = { new File("D:/temp/testGzip/1/1.sh"), new File("D:/temp/testGzip/1/1.sql") };
	//	File target = new File("D:/temp/testGzip/3.tar.gz");
	//	GZIPUtil.pack(sources, target);

	 	//TarUtils.tarGzip("1/", filepaths, "D:/temp/testGzip/123");
		TarGZipUtils.unTarGzip("D:/temp/testGzip/123.tar.gz","D:/temp/testGzip/123");
		//TarUtils.tarGzip("D:/temp/testGzip/1");
		Assert.assertEquals(true, true);
	}
}
