package com.source3g.hermes.message.service;

import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.message.ShortMessage;
import com.source3g.hermes.enums.PhoneOperator;

@Service
public class XuntongMessageService extends AbstractPositiveMessageService {

	// private static final String SERVICE_NAME="xuntongMessageService";

	@Value(value = "${message.xuntong.username}")
	private String username;
	@Value(value = "${message.xuntong.password}")
	private String password;

	public String send(String msgId, String phoneNumber, String content, PhoneOperator operator) throws Exception {
		String url = "http://www.112088.net:8088/sendsms_txt.asp?";
		url += "us_name=" + username;
		url += "&us_pwd=" + password;
		url += "&send_phone=" + phoneNumber;
		url += "&send_content=" + URLEncoder.encode(content, "GB2312");
		// String result = restTemplate.getforObject(url, String.class);
		GetMethod getMethod = new GetMethod(url);
		try {
			HttpClient httpClient = new HttpClient();
			httpClient.executeMethod(getMethod);
		} catch (Exception e) {

		} finally {
			getMethod.releaseConnection();
		}
		return msgId;
	}

	@Override
	protected String send(ShortMessage shortMessage) throws Exception {
		send(shortMessage.getMsgId(), shortMessage.getPhone(), shortMessage.getContent(), PhoneOperator.移动);
		return shortMessage.getMsgId();
	}
}
