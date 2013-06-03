package com.source3g.hermes.message.utils;

import java.util.LinkedList;

import com.lxt2.javaapi.IPassiveSubmitSender;
import com.lxt2.protocol.IApiSubmit;

public class SmsPassiveSubmitSender implements IPassiveSubmitSender {

	private static LinkedList<IApiSubmit> messageList = new LinkedList<IApiSubmit>();

	@Override
	public IApiSubmit getSubmit() {
		IApiSubmit smsSubmit = null;
		try {
			if (messageList.size() > 0) {
				smsSubmit = messageList.pop();
			}
		} catch (Exception e) {
			System.out.println("被动发送失败 " + e);
		}
		return smsSubmit;
	}

	public static void push(IApiSubmit apiSubmit) {
		messageList.push(apiSubmit);
	}

}
