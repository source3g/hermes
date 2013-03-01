package com.source3g.hermes.main;

import java.io.IOException;
import java.util.Random;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class MainTest implements Runnable {

	// private static RestTemplate restTemplate=new RestTemplate();
	private HttpClient httpClient = new HttpClient();

	public MainTest() {
		super();
	}

	public void run() {
		Random rnd = new Random();
		String uri = "http://localhost:8080/hermes/login/";
		PostMethod postMethod = new PostMethod(uri);
		// 填入各个表单域的值
		NameValuePair[] data = { new NameValuePair("username", "003"),
				new NameValuePair("password", "003") };
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
		for (int i = 0; i < 500; i++) {
			String uri1="http://58.68.229.178/hermes/merchant/customer/add/";
			PostMethod postMethod1 = new PostMethod(uri1);
			//随机生成字符串名字
			  String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";   //生成字符串从此序列中取
			  String p="0123456789";
			   StringBuffer sb = new StringBuffer();   
			   StringBuffer sb1 = new StringBuffer();   
			  for(int k=0;k<3;k++){
				   int number = rnd.nextInt(base.length());   
			        sb.append(base.charAt(number));   
			  }
			  String name=sb.toString();   
			  sb1.append("3");
			  for(int n=0;n<10;n++){
				   int number1 = rnd.nextInt(p.length());   
				   sb1.append(p.charAt(number1));   
			  }
			  String phone=sb1.toString();
			// 填入各个表单域的值
			NameValuePair[] data1 = { new NameValuePair("name", name),
					new NameValuePair("phone",phone ),new NameValuePair("customerGroup.id", "50ee335630ff9cfc19554b6a"),};
			// 将表单的值放入postMethod中
			postMethod1.setRequestBody(data1);
			// 执行postMethod
			int statusCode1;
			try {
				System.out.println("开始");
				statusCode1 = httpClient.executeMethod(postMethod1);
				System.out.println(Thread.currentThread().getName()+""+i);
				System.out.println("结束");
				// HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
				// 301或者302
				if (statusCode1 == HttpStatus.SC_MOVED_PERMANENTLY
						|| statusCode1 == HttpStatus.SC_MOVED_TEMPORARILY) {
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
			
				System.out.println(statusCode1);
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Runnable r1 = new MainTest();
		Runnable r2 = new MainTest();
		Runnable r3 = new MainTest();
		Runnable r4 = new MainTest();
		Runnable r5 = new MainTest();
		Runnable r6 = new MainTest();
		Runnable r7 = new MainTest();
		Runnable r8 = new MainTest();
		Runnable r9 = new MainTest();
		Runnable r10 = new MainTest();
		Runnable r11= new MainTest();
		Runnable r12 = new MainTest();
		Runnable r13 = new MainTest();
		Runnable r14 = new MainTest();
		Runnable r15 = new MainTest();
		Runnable r16 = new MainTest();
		Runnable r17 = new MainTest();
		Runnable r18 = new MainTest();
		Runnable r19 = new MainTest();
		Runnable r20 = new MainTest();
		Thread t1 = new Thread(r1, "t1_name");
		Thread t2 = new Thread(r2, "t2_name");
		Thread t3 = new Thread(r3, "t3_name");
		Thread t4 = new Thread(r4, "t4_name");
		Thread t5 = new Thread(r5, "t5_name");
		Thread t6 = new Thread(r6, "t6_name");
		Thread t7 = new Thread(r7, "t7_name");
		Thread t8 = new Thread(r8, "t8_name");
		Thread t9 = new Thread(r9, "t9_name");
		Thread t10 = new Thread(r10, "10_name");
		Thread t11 = new Thread(r11, "t11_name");
		Thread t12 = new Thread(r12, "t12name");
		Thread t13 = new Thread(r13, "t13_name");
		Thread t14 = new Thread(r14, "t14_name");
		Thread t15 = new Thread(r15, "t15_name");
		Thread t16 = new Thread(r16, "t16_name");
		Thread t17 = new Thread(r17, "t17_name");
		Thread t18 = new Thread(r18, "t18_name");
		Thread t19 = new Thread(r19, "t19_name");
		Thread t20 = new Thread(r20, "t20_name");
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		t7.start();
		t8.start();
		t9.start();
		t10.start();
		t11.start();
		t12.start();
		t13.start();
		t14.start();
		t15.start();
		t16.start();
		t17.start();
		t18.start();
		t19.start();
		t20.start();
/*		System.out.println("t1_name：                                  商户数量"+i++);
		System.out.println("t2_name：                                  商户数量"+k++);
		System.out.println("t3_name：                                  商户数量"+k++);
		System.out.println("t4_name：                                  商户数量"+k++);
		System.out.println("t5_name：                                  商户数量"+k++);
		System.out.println("t6_name：                                  商户数量"+k++);
		System.out.println("t7_name：                                  商户数量"+k++);
		System.out.println("t8_name：                                  商户数量"+k++);
		System.out.println("t9_name：                                  商户数量"+k++);
		System.out.println("t10_name：                                  商户数量"+k++);
		System.out.println("t11_name：                                  商户数量"+k++);
		System.out.println("t12_name：                                  商户数量"+k++);
		System.out.println("t13_name：                                  商户数量"+k++);
		System.out.println("t14_name：                                  商户数量"+k++);
		System.out.println("t15_name：                                  商户数量"+k++);
		System.out.println("t16_name：                                  商户数量"+k++);
		System.out.println("t17_name：                                  商户数量"+k++);*/
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

}
