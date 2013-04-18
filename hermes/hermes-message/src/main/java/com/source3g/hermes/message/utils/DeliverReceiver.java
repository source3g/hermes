package com.source3g.hermes.message.utils;

import com.lxt2.javaapi.IReceiver;
import com.lxt2.protocol.cbip20.CbipDeliver;

public class DeliverReceiver implements IReceiver<CbipDeliver> {

	@Override
	public void receive(CbipDeliver deliver) {
		System.out.println("cbipMessage:收到deliver:" + deliver);
	}

}
