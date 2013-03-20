package com.source3g.hermes.message.utils;

import com.lxt2.javaapi.IReceiver;
import com.lxt2.protocol.cbip20.CbipReport;

public class ReportReceiver implements IReceiver<CbipReport> {

	@Override
	public void receive(CbipReport report) {
		System.out.println("收到report:" + report);
//		report.getTotalLength()
	}

}
