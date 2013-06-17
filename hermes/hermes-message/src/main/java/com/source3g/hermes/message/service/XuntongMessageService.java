package com.source3g.hermes.message.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.source3g.hermes.enums.PhoneOperator;

@Service
public class XuntongMessageService {

	@Autowired
	private RestTemplate restTemplate;

	@Value(value = "${message.xuntong.username}")
	private int username;
	@Value(value = "${message.xuntong.password}")
	private int password;

	public void send(String msgId, String phoneNumber, String content, PhoneOperator operator) throws Exception {
		String url = "http://www.112088.net:8088/sendsms_txt.asp?";
		url+="us_name="+username;
		url+="&us_pwd="+password;
		url+="&send_phone="+phoneNumber;
		url+="&send_content="+content;
		
	}
}
