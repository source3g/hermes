package com.source3g.hermes.message.service;

import org.springframework.stereotype.Service;

import com.lxt2.javaapi.ActiveSubmitSender;
import com.lxt2.javaapi.ClientEngine;
import com.lxt2.javaapi.util.MsgConstant;
import com.lxt2.protocol.cbip20.CbipSubmit;
import com.lxt2.protocol.common.Standard_SeqNum;
import com.source3g.hermes.enums.PhoneOperator;
import com.source3g.hermes.message.utils.DeliverReceiver;
import com.source3g.hermes.message.utils.ReportReceiver;
import com.source3g.hermes.message.utils.RespReceiver;

@Service
public class CbipMesssageService {
	private ActiveSubmitSender activeSubmitSender;
	private ClientEngine client;

	public CbipMesssageService() {
		MsgConstant.clientId = 9023;
		MsgConstant.loginName = "xy3g";
		MsgConstant.password = "123456";
		MsgConstant.serverIp = "58.68.247.137";
		MsgConstant.serverPort = 1236;
		MsgConstant.connectNum = 5;
		MsgConstant.inBufferSize = 10240;
		MsgConstant.outBufferSize = 10240;
		MsgConstant.IdleTime = 10;
		MsgConstant.controlWindowsSize = 10;
		MsgConstant.maxSendTime = 3;
		MsgConstant.clearTimeOut = 10;
		MsgConstant.clearSleepTime = 10;
		MsgConstant.reconnectTime = 3;
		client = new ClientEngine(new RespReceiver(), new ReportReceiver(), new DeliverReceiver());
		System.out.println("loginName" + MsgConstant.loginName);
		System.out.println("启动引擎");
		client.start();
		System.out.println("启动完成");
		// 初始化主动发送器
		activeSubmitSender = new ActiveSubmitSender();
	}

	public void send(String msgId, String phoneNumber, String content, PhoneOperator operator) throws Exception {
		// CbipSubmitMms mmsSubmit = GetData.getMmsSubmit();
		System.out.println("主动发送短信" + phoneNumber);
		// System.out.println(mmsSubmit);
		// IApiSubmit submit=GetData.getSmsSubmit();
		CbipSubmit smsSubmit = new CbipSubmit();
		smsSubmit.setClientSeq(Standard_SeqNum.computeSeqNoErr(1));
		smsSubmit.setSrcNumber("");
		smsSubmit.setMessagePriority((byte) 1);
		smsSubmit.setReportType((short) 1);
		smsSubmit.setMessageFormat((byte) 15);
		// submit.setOverTime(System.currentTimeMillis());
		smsSubmit.setSendGroupID(0);
		smsSubmit.setProductID(20406101);
		smsSubmit.setMessageType((byte) 0);
		// 如果手机号码组包个数超出限制，可能抛出异常
		smsSubmit.setDestMobiles(phoneNumber);
		// 如果短信内容超出长度限制，可能抛出异常
		smsSubmit.setContentString(content);
		smsSubmit.setSendTime(System.currentTimeMillis());
		System.out.println("client.isConnected()" + client.isConnected());
		activeSubmitSender.sendSubmit(smsSubmit);
	}

}
