package com.source3g.hermes.message.utils;

import com.lxt2.javaapi.IRespReceiver;
import com.lxt2.protocol.IApiSubmit;
import com.lxt2.protocol.IApiSubmitResp;

public class RespReceiver implements IRespReceiver {

	@Override
	public void receive(IApiSubmit submit, IApiSubmitResp resp) {
		System.out.println("收到响应:" + "submit:" + submit + "resp" + resp);
	}

}
