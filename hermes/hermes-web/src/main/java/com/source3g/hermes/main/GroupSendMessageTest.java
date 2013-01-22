package com.source3g.hermes.main;

import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class GroupSendMessageTest {
	private static HttpClient httpClient = new HttpClient();
public static void  main(String[] args) {
		String uri = "http://localhost:8080/hermes/login/";
		PostMethod postMethod = new PostMethod(uri);
		// 填入各个表单域的值
		NameValuePair[] data = { new NameValuePair("username", "cjcjcj"),
				new NameValuePair("password", "cjcjcj") };
		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);
		// 执行postMethod

		int statusCode;
		try {
			statusCode = httpClient.executeMethod(postMethod);
			// HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
			// 301或者302
			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
					|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
				// 从头中取出转向的地址
				Header locationHeader = postMethod
						.getResponseHeader("location");
				String location = null;
				if (locationHeader != null) {
					location = locationHeader.getValue();
					System.out.println("The page was redirected to:"
							+ location);
				} else {
					System.err.println("Location field value is null.");
				}
				//return;
			}
			//postMethod.releaseConnection();
		} catch (IOException  e) {
			e.printStackTrace();
		}
		
		String uri1 = "http://localhost:8080/hermes/merchant/message/messageSend/";
		PostMethod postMethod1 = new PostMethod(uri1);
		// 填入各个表单域的值
		NameValuePair[] data1 = { new NameValuePair("ids", "50f37613a4fd309a8c4abfe9"),new NameValuePair("ids", "50f8f68aa4fd98711331b4e5"),
				new NameValuePair("content", "Happy new year!") };
		// 将表单的值放入postMethod中
		postMethod1.setRequestBody(data1);
		// 执行postMethod

		int statusCode1;
		try {
			statusCode1 = httpClient.executeMethod(postMethod1);
			// HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
			// 301或者302
			if (statusCode1 == HttpStatus.SC_MOVED_PERMANENTLY
					|| statusCode1 == HttpStatus.SC_MOVED_TEMPORARILY) {
				// 从头中取出转向的地址
				Header locationHeader1 = postMethod1
						.getResponseHeader("location");
				String location = null;
				if (locationHeader1 != null) {
					location = locationHeader1.getValue();
					System.out.println("The page was redirected to:"
							+ location);
				} else {
					System.err.println("Location field value is null.");
				}
				//return;
			}
			//postMethod.releaseConnection();
			System.out.println(statusCode1);
		} catch (IOException  e) {
			e.printStackTrace();
		}
}
}