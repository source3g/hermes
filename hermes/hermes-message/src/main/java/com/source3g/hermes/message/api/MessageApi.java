package com.source3g.hermes.message.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lxt2.protocol.cbip20.CbipReport;
import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.message.AutoSendMessageTemplate;
import com.source3g.hermes.entity.message.GroupSendLog;
import com.source3g.hermes.entity.message.MessageTemplate;
import com.source3g.hermes.enums.MessageType;
import com.source3g.hermes.message.service.MessageService;
import com.source3g.hermes.message.utils.ReportReceiver;
import com.source3g.hermes.service.CommonBaseService;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping(value = "/shortMessage")
public class MessageApi {

	@Autowired
	private MessageService messageService;
	@Autowired
	private CommonBaseService commonBaseService;

	@RequestMapping(value = "/template/add", method = RequestMethod.POST)
	@ResponseBody
	public String addTemplate(@RequestBody MessageTemplate messageTemplate) {
		messageService.add(messageTemplate);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/template/save", method = RequestMethod.POST)
	@ResponseBody
	public String saveTemplate(@RequestBody MessageTemplate messageTemplate) {
		messageService.save(messageTemplate);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/template/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String saveTemplate(@PathVariable String id) {
		messageService.deleteById(id, MessageTemplate.class);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/template/list/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public List<MessageTemplate> listTemplate(@PathVariable String merchantId) {
		return messageService.listAll(merchantId);
	}

	@RequestMapping(value = "/statistics/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> findCustomerStatistics(@PathVariable String merchantId) {
		return messageService.findMessageStastics(new ObjectId(merchantId));
	}

	@RequestMapping(value = "/statistics/sn/{sn}", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> findCustomerStatisticsBySn(@PathVariable String sn) throws Exception {
		Merchant merchant = commonBaseService.findMerchantByDeviceSn(sn);
		return messageService.findMessageStastics(merchant.getId());
	}

	/**
	 * 短信群发
	 * 
	 * @param merchantId
	 * @param ids
	 * @param customerPhones
	 * @param content
	 * @return
	 */
	// @RequestMapping(value = "/messageSend/{merchantId}", method =
	// RequestMethod.POST)
	// @ResponseBody
	// @Deprecated
	// //TODO 要删除
	// public String messageSend(@PathVariable String merchantId, String[] ids,
	// String customerPhones, String content) {
	// try {
	// messageService.groupSend(new ObjectId(merchantId), ids, customerPhones,
	// content);
	// } catch (Exception e) {
	// return e.getMessage();
	// }
	// return ReturnConstants.SUCCESS;
	// }

	/**
	 * 短信群发
	 * 
	 * @param merchantId
	 * @param ids
	 * @param customerPhones
	 * @param content
	 * @return
	 */
	@RequestMapping(value = "/groupSend/{merchantId}", method = RequestMethod.POST)
	@ResponseBody
	public String groupSend(@PathVariable String merchantId, String[] ids, String[] customerPhones, String content) {
		try {
			messageService.groupSend(new ObjectId(merchantId), ids, customerPhones, content);
		} catch (Exception e) {
			return e.getMessage();
		}
		return ReturnConstants.SUCCESS;
	}

	/**
	 * 短信单发
	 * 
	 * @param sn
	 * @param customerMessageDto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/messageSend/sn/{sn}", method = RequestMethod.POST)
	@ResponseBody
	public String messageSendBySn(@PathVariable String sn, @RequestBody CustomerMessageDto customerMessageDto) throws Exception {
		Merchant merchant = commonBaseService.findMerchantByDeviceSn(sn);
		try {
			messageService.singleSend(merchant.getId(), customerMessageDto.getCustomerPhone(), customerMessageDto.getContent(), MessageType.短信发送);
		} catch (Exception e) {
			return e.getMessage();
		}
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/quicklySend", method = RequestMethod.POST)
	@ResponseBody
	public String quicklySend(String merchantId, String content, String phone) throws Exception {
		ObjectId obj = new ObjectId(merchantId);
		try {
			messageService.singleSend(obj, phone, content, MessageType.快捷发送);
		} catch (Exception e) {
			return e.getMessage();
		}
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/groupSendLogList/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public List<GroupSendLog> groupSendLogList(@PathVariable String merchantId) {
		return messageService.groupSendLogList(new ObjectId(merchantId));
	}

	@RequestMapping(value = "/messageSendLog/{merchantId}/list", method = RequestMethod.GET)
	@ResponseBody
	public Page listMessageSendLog(@PathVariable String merchantId, String pageNo, Date startTime, Date endTime, String phone, String customerGroupName) {
		int pageNoInt = Integer.valueOf(pageNo);
		return messageService.list(pageNoInt, new ObjectId(merchantId), startTime, endTime, phone, customerGroupName);
	}

	@RequestMapping(value = "/autoSend/messageInfo/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public AutoSendMessageTemplate getMessageAutoSend(@PathVariable String merchantId) {
		return messageService.getMessageAutoSend(new ObjectId(merchantId));
	}

	@RequestMapping(value = "/autoSend/messageInfo", method = RequestMethod.POST)
	@ResponseBody
	public String autoSend(@RequestBody AutoSendMessageTemplate AutoSendMessageTemplate) {
		messageService.saveMessageAutoSend(AutoSendMessageTemplate);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/remindSend/{title}/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public String remindSend(@PathVariable String title, @PathVariable ObjectId merchantId) throws Exception {
		messageService.remindSend(title, merchantId);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/remindSend/sn/{sn}/{title}", method = RequestMethod.GET)
	@ResponseBody
	public String remindSend(@PathVariable String title, @PathVariable String sn) throws Exception {
		Merchant merchant = commonBaseService.findMerchantByDeviceSn(sn);
		messageService.remindSend(title, merchant.getId());
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/remindIgnore/sn/{sn}/{title}", method = RequestMethod.GET)
	@ResponseBody
	public String remindIgnore(@PathVariable String title, @PathVariable String sn) throws Exception {
		Merchant merchant = commonBaseService.findMerchantByDeviceSn(sn);
		messageService.ignoreSendMessages(title, merchant.getId());
		return ReturnConstants.SUCCESS;
	}
	

	// TODO 方法名要改
	@RequestMapping(value = "/ignoreSendMessages/{title}/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public String ignoreSendMessages(@PathVariable String title, @PathVariable ObjectId merchantId) {
		messageService.ignoreSendMessages(title, merchantId);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/failedMessagelist", method = RequestMethod.GET)
	@ResponseBody
	public Page failedMessagelist(String pageNo, Date startTime, Date endTime, String status) {
		int pageNoInt = Integer.valueOf(pageNo);
		return messageService.failedMessagelist(pageNoInt, startTime, endTime, status);
	}

	@RequestMapping(value = "/failed/resend/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Boolean failedMessageSendAgain(@PathVariable String id) {
		return	messageService.failedMessageResend(id);
	}

	@RequestMapping(value = "/failed/resendAll/{startTime}/{endTime}", method = RequestMethod.GET)
	@ResponseBody
	public Boolean allFailedMessagesSendAgain( @PathVariable Date startTime,@PathVariable Date endTime, String status) {
		return	messageService.allFailedMessagesResend(startTime,endTime,status);
	}



	@RequestMapping(value = "/reportReceiverTest", method = RequestMethod.GET)
	@ResponseBody
	public String reportReceiverTest() {
		CbipReport cbipReport = new CbipReport();
		cbipReport.setClientSeq(1305021229440002L);
		cbipReport.setStatus(0);
		cbipReport.setErrorCode("0");
		ReportReceiver reportReceiver=new ReportReceiver();
		reportReceiver.receive(cbipReport);
		return ReturnConstants.SUCCESS;
	}

	public static class CustomerMessageDto {
		private String customerPhone;
		private String content;

		public String getCustomerPhone() {
			return customerPhone;
		}

		public void setCustomerPhone(String customerPhone) {
			this.customerPhone = customerPhone;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}

}
