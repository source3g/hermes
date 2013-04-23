package com.source3g.hermes.monitor.service;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	private String hostName = "smtp.163.com";
	private String from = "bin5356@163.com";
	private String fromName = "fff";
	private String to = "303844828@qq.com";
	private String toName = "zhangsan";
	private String username = "bin5356";
	private String password = "19880818";
	private String subject = "面试通知";
	private String msgContent = "你好,面试通过";

	public void sendEmail() throws EmailException {
		SimpleEmail email = new SimpleEmail();
		// 设置发送主机的服务器地址
		email.setHostName(hostName);
		// 设置收件人邮箱
		email.addTo(to, toName);
		// 发件人邮箱
		email.setFrom(from, fromName);
		// 如果要求身份验证，设置用户名、密码，分别为发件人在邮件服务器上注册的用户名和密码
		email.setAuthentication(username, password);
		// 设置邮件的主题
		email.setSubject(subject);
		// 邮件正文消息
		email.setContent(msgContent, "text/plain;charset=UTF-8");
		email.send();
		System.out.println("The SimpleEmail send sucessful!!!");
	}
}
