package com.source3g.hermes.message.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hongxun.pub.DataCommand;
import com.hongxun.pub.tcptrans.TcpCommTrans;
import com.source3g.hermes.message.service.TcpCommandService;

@Component
public class MessageTestSchduler {
	
	private static final Logger LOGGER=LoggerFactory.getLogger(MessageTestSchduler.class);
	private static int i=111111;

	public void messageTest() {
		TcpCommTrans tcp = null;
		try {
			tcp = TcpCommandService.getTcp();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		DataCommand command = new DataCommand("ActiveTest");
		command.AddNewItem("commandId", String.valueOf(i));
		i++;
		System.out.println(command.getCommand());
		
		try {
			tcp.SendCommand(command);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("OK");
		LOGGER.debug("发送了一条测试消息");
	}
}
