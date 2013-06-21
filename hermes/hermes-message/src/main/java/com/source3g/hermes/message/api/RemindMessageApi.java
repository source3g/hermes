package com.source3g.hermes.message.api;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.source3g.hermes.dto.customer.CustomerRemindDto;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.message.ShortMessage;
import com.source3g.hermes.enums.PhoneOperator;
import com.source3g.hermes.message.service.MessageQueueService;
import com.source3g.hermes.message.service.MessageService;
import com.source3g.hermes.message.service.XuntongMessageService;
import com.source3g.hermes.service.CommonBaseService;

@Controller
public class RemindMessageApi {

	@Autowired
	private MessageService messageService;
	@Autowired
	private CommonBaseService commonBaseService;
	@Autowired
	private XuntongMessageService xuntongMessageService;
	@Autowired
	private MessageQueueService messageQueueService;

	@RequestMapping(value = "/customer/todayReminds/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public List<CustomerRemindDto> findTodayReminds(@PathVariable String merchantId) {
		return messageService.findTodayReminds(new ObjectId(merchantId));
	}

	@RequestMapping(value = "/customer/todayReminds/sn/{sn}", method = RequestMethod.GET)
	@ResponseBody
	public List<CustomerRemindDto> findTodayRemindsBySn(@PathVariable String sn) throws Exception {
		Merchant merchant = commonBaseService.findMerchantByDeviceSn(sn);
		return messageService.findTodayReminds(merchant.getId());
	}

	// TODO: delete
	@RequestMapping(value = "/xuntong", method = RequestMethod.GET)
	@ResponseBody
	public String sendByXuntong() throws Exception {
		return xuntongMessageService.send("123", "18600217379", "你好", PhoneOperator.移动);
	}

	// TODO: delete
	@RequestMapping(value = "/message/poll", method = RequestMethod.GET)
	@ResponseBody
	public ShortMessage poll() throws Exception {
		return messageQueueService.poll();
	}

}
