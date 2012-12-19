package com.source3g.hermes.message.service;

import com.hongxun.pub.tcptrans.CommReceiveEvent;
import com.hongxun.pub.tcptrans.CommReceiveListener;
import com.hongxun.pub.tcptrans.TcpCommTrans;

public class TcpCommandService {

	private static TcpCommTrans tcp = null;
	public static boolean isLogin=false;

	public TcpCommandService() {
		super();

	}

	public static TcpCommTrans getTcp() throws InterruptedException {
		if (tcp == null) {
			tcp = new TcpCommTrans("60.28.194.246", 8011, "Q3h2O6XY", "ATN2XX5Y", 0);
			tcp.start(1000);
			tcp.SetCommReceiveListener(new CommReceiveListener() {
				@Override
				public void TransLogined(CommReceiveEvent commreceiveevent) {
					System.out.println("1111");
					isLogin = true;
				}

				@Override
				public void TransExit(CommReceiveEvent commreceiveevent) {
					System.out.println("222");
				}

				@Override
				public void CommReceive(CommReceiveEvent commreceiveevent) {
					Object o = commreceiveevent.getSource();
					TcpCommTrans t = (TcpCommTrans) o;
					System.out.println(o);
					System.out.println(t.poll());
					System.out.println("333");
				}
			});
		}
		return tcp;
	}

}
