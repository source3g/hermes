package com.source3g.hermes.message.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.hongxun.pub.DataCommand;
import com.hongxun.pub.tcptrans.CommReceiveEvent;
import com.hongxun.pub.tcptrans.CommReceiveListener;
import com.hongxun.pub.tcptrans.TcpCommTrans;
import com.source3g.hermes.enums.MessageStatus;
import com.source3g.hermes.enums.PhoneOperator;

@Service
public class TcpCommandService implements ApplicationContextAware {

	private static Logger logger = LoggerFactory.getLogger(TcpCommandService.class);

	@Value(value = "${message.msgcode}")
	private String msgCode;
	@Value(value = "${message.itemid}")
	private String itemId;
	@Value(value = "${message.gatename.cm}")
	private String cmGateName;
	// @Value(value = "message.gatename.cm.spnumber")
	// private String spnumber;
	@Value(value = "${message.gatename.cu}")
	private String cuGateName;
	@Value(value = "${message.gatename.ct}")
	private String ctGateName;

	private static TcpCommTrans tcp = null;
	public static boolean isLogin = false;
	public static List<byte[]> list = new ArrayList<byte[]>();
	private static ApplicationContext applicationContext;

	public TcpCommandService() {
		super();
	}

	public void send(String msgId, String phoneNumber, String content, PhoneOperator operator) {
		TcpCommTrans tcp = null;
		try {
			tcp = TcpCommandService.getTcp();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		// unicomgz_wxtl
		DataCommand command = new DataCommand("submit");
		command.AddNewItem("msgcode", msgCode);
		command.AddNewItem("itemid", itemId);
		command.AddNewItem("msgid", msgId);
		command.AddNewItem("gatename", getGateNameByOperator(operator));
		// command.AddNewItem("gatename", "mobile0025");
		// command.AddNewItem("spnumber", "10660025");
		command.AddNewItem("feetype", "1");
		command.AddNewItem("usernumber", phoneNumber);
		try {
			byte bytes[] = content.getBytes("GBK");//
			command.AddNewItem("msg", bytes, true, "GBK");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		try {
			tcp.SendCommand(command);
			logger.debug("发送短信");
			System.out.println("OK");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("command:" + command.getCommand());
		logger.debug("queueSize:" + tcp.getSndQueueSize() + "unsendSize" + tcp.getUnSend().size() + "command:" + command.getCommand());
		System.out.println(tcp.getSndQueueSize());
		System.out.println(tcp.getUnSend().size());
	}

	private String getGateNameByOperator(PhoneOperator operator) {
		String result = "";
		switch (operator) {
		case 移动:
			result = cmGateName;
			break;
		case 联通:
			result = cuGateName;
			break;
		case 电信:
			result = ctGateName;
		default:
			result = cuGateName;
			break;
		}
		return result;
	}

	public static TcpCommTrans getTcp() throws InterruptedException {
		if (tcp == null) {
			tcp = new TcpCommTrans("60.28.194.246", 8011, "Q3h2O6XY", "ATN2XX5Y", 0);
			tcp.start(1000);
			tcp.SetCommReceiveListener(new CommReceiveListener() {
				@Override
				public void TransLogined(CommReceiveEvent commreceiveevent) {
					System.out.println("1111");
					System.out.println("sms logined In");
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
					try {
						String msgId = result.substring(result.indexOf("msgid=") + "msgid=".length(), result.indexOf("&"));
						String statemsg = result.substring(result.indexOf("statemsg:=") + "statemsg:=".length(), result.indexOf("&"));
						MessageService messageService = (MessageService) applicationContext.getBean("messageService");
						if ("CCE1BDBBB3C9B9A6".equals(statemsg)) {
							messageService.updateShortMessageStatus(msgId, MessageStatus.发送成功);
						} else {
							messageService.updateShortMessageStatus(msgId, MessageStatus.提交失败);
						}
					} catch (Exception e) {
						System.out.println("解析返回失败" + e.getMessage());
					}
					System.out.println(o);
					System.out.println(result);
					System.out.println("333");
				}
			});
		}
		return tcp;
	}

	/*
	 * public static void main(String[] args) { String report =
	 * 
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

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getCmGateName() {
		return cmGateName;
	}

	public void setCmGateName(String cmGateName) {
		this.cmGateName = cmGateName;
	}

	public String getCuGateName() {
		return cuGateName;
	}

	public void setCuGateName(String cuGateName) {
		this.cuGateName = cuGateName;
	}

	public String getCtGateName() {
		return ctGateName;
	}

	public void setCtGateName(String ctGateName) {
		this.ctGateName = ctGateName;
	}
}
