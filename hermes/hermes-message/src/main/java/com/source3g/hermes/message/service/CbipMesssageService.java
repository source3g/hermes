package com.source3g.hermes.message.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lxt2.javaapi.ActiveSubmitSender;
import com.lxt2.javaapi.ClientEngine;
import com.lxt2.javaapi.util.MsgConstant;
import com.lxt2.protocol.cbip20.CbipSubmit;
import com.source3g.hermes.enums.PhoneOperator;
import com.source3g.hermes.message.utils.DeliverReceiver;
import com.source3g.hermes.message.utils.ReportReceiver;
import com.source3g.hermes.message.utils.RespReceiver;

@Service
public class CbipMesssageService {
	private Logger logger = LoggerFactory.getLogger(CbipMesssageService.class);
	private ActiveSubmitSender activeSubmitSender;
	private ClientEngine client;

	@Value(value = "${cbip.loginName}")
	private String loginName;
	@Value(value = "${cbip.password}")
	private String password;
	@Value(value = "${cbip.serverIp}")
	private String serverIp;
	@Value(value = "${cbip.serverPort}")
	private Integer serverPort;
	@Value(value = "${cbip.clientId}")
	private Integer clientId;
	@Value(value = "${cbip.productId}")
	private Integer productId;
	@Value(value = "${cbip.test}")
	private Boolean isTest;

	public void init() {
		MsgConstant.clientId = clientId;
		MsgConstant.loginName = loginName;
		MsgConstant.password = password;
		MsgConstant.serverIp = serverIp;
		MsgConstant.serverPort = serverPort;
		MsgConstant.connectNum = 5;
		MsgConstant.inBufferSize = 10240;
		MsgConstant.outBufferSize = 10240;
		MsgConstant.IdleTime = 10;
		MsgConstant.controlWindowsSize = 10;
		MsgConstant.maxSendTime = 3;
		MsgConstant.clearTimeOut = 1000;
		MsgConstant.clearSleepTime = 10;
		MsgConstant.reconnectTime = 3;
		client = new ClientEngine(new RespReceiver(), new ReportReceiver(), new DeliverReceiver());
		logger.debug("loginName" + MsgConstant.loginName);
		System.out.println("loginName" + MsgConstant.loginName);
		System.out.println("启动引擎");
		logger.debug("启动引擎");
		client.start();
		System.out.println("启动完成");
		logger.debug("启动完成");
		// 初始化主动发送器
		activeSubmitSender = new ActiveSubmitSender();
	}

	public String send(String msgId, String phoneNumber, String content, PhoneOperator operator) throws Exception {
		// long clientSeq = Standard_SeqNum.computeSeqNoErr(1);
		Long msgIdL=Long.parseLong(msgId);
		if (isTest) {
			System.out.println("摸拟向" + phoneNumber + "发送了内容：" + content);
			logger.debug("摸拟向" + phoneNumber + "发送了内容：" + content);
			return msgId;
		}
		if (client == null) {
			init();
		}
		// CbipSubmitMms mmsSubmit = GetData.getMmsSubmit();
		System.out.println("主动发送短信" + phoneNumber);
		logger.debug("主动发送短信" + phoneNumber);
		// System.out.println(mmsSubmit);
		CbipSubmit smsSubmit = new CbipSubmit();
		smsSubmit.setClientSeq(msgIdL);
		smsSubmit.setSrcNumber("");
		smsSubmit.setMessagePriority((byte) 1);
		smsSubmit.setReportType((short) 1);
		smsSubmit.setMessageFormat((byte) 15);
		// submit.setOverTime(System.currentTimeMillis());
		smsSubmit.setSendGroupID(0);
		smsSubmit.setProductID(productId);
		smsSubmit.setMessageType((byte) 0);
		// 如果手机号码组包个数超出限制，可能抛出异常
		smsSubmit.setDestMobiles(phoneNumber);
		// 如果短信内容超出长度限制，可能抛出异常
		smsSubmit.setContentString(content);
		smsSubmit.setSendTime(System.currentTimeMillis());
		System.out.println("client.isConnected()" + client.isConnected());
		logger.debug("client.isConnected()" + client.isConnected());
		activeSubmitSender.sendSubmit(smsSubmit);
		return msgId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public Integer getServerPort() {
		return serverPort;
	}

	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Boolean getIsTest() {
		return isTest;
	}

	public void setIsTest(Boolean isTest) {
		this.isTest = isTest;
	}
}
