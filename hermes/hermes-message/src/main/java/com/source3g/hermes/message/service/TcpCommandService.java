package com.source3g.hermes.message.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.hongxun.pub.tcptrans.CommReceiveEvent;
import com.hongxun.pub.tcptrans.CommReceiveListener;
import com.hongxun.pub.tcptrans.TcpCommTrans;
import com.source3g.hermes.enums.MessageStatus;

@Service
public class TcpCommandService implements ApplicationContextAware {

	private static TcpCommTrans tcp = null;
	public static boolean isLogin = false;
	public static List<byte[]> list = new ArrayList<byte[]>();
	private static ApplicationContext applicationContext;

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
					// report
					// msgid=03251325236560000009&commandid=7a7cf8&statemsg:=CCE1BDBBB3C9B9A6&state=0&areacode=0577,�㽭,����,100&gatename=unicomgzDXYD&itemid=10253901&sloginservers=smdispatch1,&feetype=1&usernumber=13057707697
					String result = t.poll();
					String msgId = result.substring(result.indexOf("msgid=") + "msgid=".length(), result.indexOf("&"));
					String statemsg = result.substring(result.indexOf("statemsg:=") + "statemsg:=".length(), result.indexOf("&"));
					MessageService messageService = (MessageService) applicationContext.getBean("messageService");
					if ("CCE1BDBBB3C9B9A6".equals(statemsg)) {
						messageService.updateShortMessageStatus(msgId, MessageStatus.发送成功);
					} else {
						messageService.updateShortMessageStatus(msgId, MessageStatus.提交失败);
					}
					System.out.println(o);
					System.out.println(t.poll());
					System.out.println("333");
				}
			});
		}
		return tcp;
	}

	/*
	 * public static void main(String[] args) { String report =
	 * "report msgid=03251325236560000009&commandid=7a7cf8&statemsg:=CCE1BDBBB3C9B9A6&state=0&areacode=0577,�㽭,����,100&gatename=unicomgzDXYD&itemid=10253901&sloginservers=smdispatch1,&feetype=1&usernumber=13057707697"
	 * ; String msgId = report.substring(report.indexOf("msgid=") +
	 * "msgid=".length(), report.indexOf("&")); String statemsg =
	 * report.substring(report.indexOf("statemsg:=") + "statemsg:=".length(),
	 * report.indexOf("&", report.indexOf("statemsg:=")));
	 * System.out.println(statemsg); System.out.println(msgId); }
	 */

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}

}
