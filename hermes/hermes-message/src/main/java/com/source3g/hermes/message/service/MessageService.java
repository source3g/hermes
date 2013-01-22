package com.source3g.hermes.message.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.jms.Destination;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.hongxun.pub.DataCommand;
import com.hongxun.pub.tcptrans.TcpCommTrans;
import com.mongodb.CommandResult;
import com.source3g.hermes.constants.JmsConstants;
import com.source3g.hermes.dto.message.MessageStatisticsDto;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.Remind;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.merchant.MerchantRemindTemplate;
import com.source3g.hermes.entity.merchant.RemindTemplate;
import com.source3g.hermes.entity.message.GroupSendLog;
import com.source3g.hermes.entity.message.MessageAutoSend;
import com.source3g.hermes.entity.message.MessageSendLog;
import com.source3g.hermes.entity.message.MessageTemplate;
import com.source3g.hermes.enums.MessageStatus;
import com.source3g.hermes.enums.MessageType;
import com.source3g.hermes.enums.PhoneOperator;
import com.source3g.hermes.enums.Sex;
import com.source3g.hermes.message.PhoneInfo;
import com.source3g.hermes.message.ShortMessageMessage;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.service.JmsService;
import com.source3g.hermes.utils.DateFormateUtils;
import com.source3g.hermes.utils.Page;
import com.source3g.hermes.utils.PhoneUtils;

@Service
public class MessageService extends BaseService {

	@Autowired
	private JmsService jmsService;
	@Autowired
	private Destination messageDestination;

	@SuppressWarnings("unused")
	@Value(value = "message.ip}")
	private String messageIp;
	@Value(value = "message.name")
	@SuppressWarnings("unused")
	private String messageName;
	@Value(value = "message.pass")
	@SuppressWarnings("unused")
	private String messagePass;
	@SuppressWarnings("unused")
	@Value(value = "message.msgcode")
	private String msgCode;
	@SuppressWarnings("unused")
	@Value(value = "message.itemid")
	private String itemId;
	@SuppressWarnings("unused")
	@Value(value = "message.msgid")
	private String msgId;
	@SuppressWarnings("unused")
	@Value(value = "message.gatename.cm")
	private String cmGateName;
	@SuppressWarnings("unused")
	@Value(value = "message.gatename.cm.spnumber")
	private String spnumber;
	@SuppressWarnings("unused")
	@Value(value = "message.gatename.cu")
	private String cuGateName;

	/**
	 * 短信群发
	 * 
	 * @param merchantId
	 * @param ids
	 * @param content
	 */
	public void messageGroupSend(ObjectId merchantId, String[] ids, String customerPhones, String content) throws Exception {
		Set<String> phones = new HashSet<String>();
		if (ids != null) {
			Query query = new Query();
			List<ObjectId> customerGroupIds = new ArrayList<ObjectId>();
			for (String id : ids) {
				ObjectId ObjId = new ObjectId(id);
				customerGroupIds.add(ObjId);
			}
			query.addCriteria(Criteria.where("customerGroup.$id").in(customerGroupIds));
			String command="db.customer.find({\"customerGroup.$id\":ObjectId(\""+customerGroupIds.get(0)+"\")})";
			//String command1="{\"customerGroup.$id|:ObjectId(\""+customerGroupIds.get(0)+"\")}";
			CommandResult commandResult=mongoTemplate.executeCommand(command);
			System.out.println(commandResult);
			List<Customer> customers = mongoTemplate.find(query, Customer.class);
			for (Customer customer : customers) {
				phones.add(customer.getPhone());
			}
		}
		if (customerPhones != null) {
			String customerPhoneArray[] = customerPhones.split(";");
			for (String phone : customerPhoneArray) {
				phones.add(phone);
			}
		}
		String[] phoneArray = new String[phones.size()];
		phones.toArray(phoneArray);
		sendMessages(merchantId, phoneArray, content);
	}

	public MessageSendLog genMessageSendLog(String name, String customerGroupName, ObjectId merchantId, String phone, int sendCount, String content, MessageType type, MessageStatus status) {
		MessageSendLog log = new MessageSendLog();
		log.setId(ObjectId.get());
		log.setContent(content);
		log.setSendTime(new Date());
		log.setCustomerName(name);
		log.setCustomerGroupName(customerGroupName);
		log.setMerchantId(merchantId);
		log.setPhone(phone);
		log.setSendCount(sendCount);
		log.setType(type);
		log.setStatus(status);
		mongoTemplate.insert(log);
		return log;
	}

	private void genGroupSendLog(String[] customerPhoneArray, String content, ObjectId merchantId) {
		GroupSendLog groupSendLog = new GroupSendLog();
		groupSendLog.setMerchantId(merchantId);
		groupSendLog.setContent(content);
		groupSendLog.setSendCount(customerPhoneArray.length);
		groupSendLog.setSendTime(new Date());
		groupSendLog.setId(ObjectId.get());
		mongoTemplate.insert(groupSendLog);

	}

	private MessageSendLog genMessageSendLog(String phone, ObjectId merchantId, String content, MessageType type) {
		String customerName = null;
		String customerGroupName = null;
		Customer c = mongoTemplate.findOne(new Query(Criteria.where("phone").is(phone)), Customer.class);
		if (c != null) {
			customerName = c.getName();
			if (c.getCustomerGroup() != null) {
				customerGroupName = c.getCustomerGroup().getName();
			}
		}
		MessageSendLog log = genMessageSendLog(customerName, customerGroupName, merchantId, phone, 1, content, type, MessageStatus.发送中);
		return log;
	}

	public List<MessageTemplate> listAll(String merchantId) {
		return mongoTemplate.find(new Query(Criteria.where("merchantId").is(new ObjectId(merchantId))).with(new Sort(Direction.DESC, "_id")), MessageTemplate.class);
	}

	public void save(MessageTemplate messageTemplate) {
		mongoTemplate.save(messageTemplate);
	}
	
	public void sendMessage(ObjectId merchantId,String phone,String content) throws Exception{
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("_id").is(merchantId)), Merchant.class);
		if(merchant.getShortMessage().getSurplusMsgCount()-1<0) {
			throw new Exception("余额不足");
		}
		ShortMessageMessage message = new ShortMessageMessage();
		PhoneInfo phoneInfo = genPhoneInfo(merchantId, phone, content, MessageType.短信发送);
		List<PhoneInfo> phoneInfos =Arrays.asList(phoneInfo);
		
		message.setContent(content);
		message.setPhoneInfos(phoneInfos);
		message.setMessageType(MessageType.短信发送);
		message.setMerchantId(merchantId);
		jmsService.sendObject(messageDestination, message, JmsConstants.TYPE, JmsConstants.SEND_MESSAGE);
	}

	public void sendMessages(ObjectId merchantId, String[] customerPhoneArray, String content) throws Exception {
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("_id").is(merchantId)), Merchant.class);
		if (customerPhoneArray.length > merchant.getShortMessage().getSurplusMsgCount()) {
			throw new Exception("余额不足");
		}
		ShortMessageMessage message = new ShortMessageMessage();
		List<PhoneInfo> phoneInfos = genPhoneInfos(merchantId, customerPhoneArray, content, MessageType.群发);
		message.setContent(content);
		message.setPhoneInfos(phoneInfos);
		message.setMessageType(MessageType.群发);
		message.setMerchantId(merchantId);
		jmsService.sendObject(messageDestination, message, JmsConstants.TYPE, JmsConstants.SEND_MESSAGE);
	}

	/**
	 * 生成电话信息，并生成发送日志
	 * 
	 * @param merchantId
	 * @param messageSendLogId
	 * @param customerPhoneArray
	 * @param content
	 * @param type
	 * @return
	 */
	private PhoneInfo genPhoneInfo(ObjectId merchantId, String phone, String content, MessageType type) {
			// 生成发送记录
			MessageSendLog log = genMessageSendLog(phone, merchantId, content, type);
			// 发送记录生成完成
			PhoneInfo phoneInfo = new PhoneInfo(phone, content, log.getId());
		// 生成短信群发记录
		genGroupSendLog(phone, content, merchantId);
		return phoneInfo;
	}
	
	private void genGroupSendLog(String phone, String content, ObjectId merchantId) {
		GroupSendLog groupSendLog = new GroupSendLog();
		groupSendLog.setMerchantId(merchantId);
		groupSendLog.setContent(content);
		groupSendLog.setSendCount(1);
		groupSendLog.setSendTime(new Date());
		groupSendLog.setId(ObjectId.get());
		mongoTemplate.insert(groupSendLog);
	}

	/**
	 * 生成电话信息，并生成发送日志
	 * 
	 * @param merchantId
	 * @param messageSendLogId
	 * @param customerPhoneArray
	 * @param content
	 * @param type
	 * @return
	 */
	private List<PhoneInfo> genPhoneInfos(ObjectId merchantId, String[] customerPhoneArray, String content, MessageType type) {
		List<PhoneInfo> result = new ArrayList<PhoneInfo>();
		HashSet<String> hashSet = new HashSet<String>(Arrays.asList(customerPhoneArray));
		for (String phone : hashSet) {
			// 生成发送记录
			MessageSendLog log = genMessageSendLog(phone, merchantId, content, type);
			// 发送记录生成完成
			PhoneInfo phoneInfo = new PhoneInfo(phone, content, log.getId());
			result.add(phoneInfo);
		}
		// 生成短信群发记录
		genGroupSendLog(customerPhoneArray, content, merchantId);
		return result;
	}

	// 查找群发记录
	public List<GroupSendLog> groupSendLogList(ObjectId merchantId) {
		Query query = new Query();
		Sort sort = new Sort(Direction.DESC, "_id");
		query.with(sort);
		return mongoTemplate.find(query.skip(0).limit(5), GroupSendLog.class);

	}

	public Page list(int pageNoInt, ObjectId merchantId, Date startTime, Date endTime, String phone, String customerGroupName) {
		Query query = new Query();
		Criteria criteria = Criteria.where("merchantId").is(merchantId);
		if (StringUtils.isNotEmpty(phone)) {
			Pattern pattern = Pattern.compile("^.*" + phone + ".*$", Pattern.CASE_INSENSITIVE);
			criteria.and("phone").is(pattern);
		}

		if (StringUtils.isNotEmpty(customerGroupName)) {
			Pattern pattern1 = Pattern.compile("^.*" + customerGroupName + ".*$", Pattern.CASE_INSENSITIVE);
			// query.addCriteria(Criteria.where("customerGroupName").is(pattern1));
			criteria.and("customerGroupName").is(pattern1);
		}

		if (startTime != null && endTime != null) {
			criteria.and("sendTime").gte(startTime).lte(endTime);
		} else if (startTime != null) {
			criteria.and("sendTime").gte(startTime);
		} else if (endTime != null) {
			criteria.and("sendTime").lte(endTime);
		}
		query.addCriteria(criteria);
		query.with(new Sort(Direction.DESC, "_id"));
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, MessageSendLog.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNoInt);
		List<MessageSendLog> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), MessageSendLog.class);
		page.setData(list);
		return page;
	}

	public void addLog(MessageSendLog log) {
		mongoTemplate.insert(log);
	}

	public void updateLog(ObjectId id, Date date, MessageStatus status) {
		Update update = new Update();
		update.set("sendTime", date);
		update.set("status", status);
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(id)), update, MessageSendLog.class);
	}

	public MessageStatus send(String phoneNumber, String content) {
		if (PhoneOperator.移动.equals(PhoneUtils.getOperatior(phoneNumber))) {
			return sendByCm(phoneNumber, content);
		}
		if (PhoneOperator.联通.equals(PhoneUtils.getOperatior(phoneNumber))) {
			return sendByCu(phoneNumber, content);
		}
		if (PhoneOperator.电信.equals(PhoneUtils.getOperatior(phoneNumber))) {
			return sendByCt(phoneNumber, content);
		}
		return MessageStatus.电话号码有误;
	}

	private MessageStatus sendByCt(String phoneNumber, String content) {
		System.out.println("通过电信向" + phoneNumber + "发送" + content);
		sendByCu(phoneNumber, content);
		return MessageStatus.已发送;
	}

	private MessageStatus sendByCu(String phoneNumber, String content) {
		System.out.println("通过联通向" + phoneNumber + "发送" + content);

		TcpCommTrans tcp = null;
		try {
			tcp = TcpCommandService.getTcp();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		DataCommand command = new DataCommand("submit");
		command.AddNewItem("msgcode", "15");
		command.AddNewItem("itemid", "10253901");
		command.AddNewItem("msgid", "03251325236560000009");
		command.AddNewItem("gatename", "unicomgzDXYD");
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
			System.out.println("OK");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return MessageStatus.已发送;
	}

	private MessageStatus sendByCm(String phoneNumber, String content) {
		System.out.println("通过移动向" + phoneNumber + "发送" + content);
		sendByCu(phoneNumber, content);
		return MessageStatus.已发送;
	}

	public void saveMessageAutoSend(MessageAutoSend messageAutoSend) {
		Update update = new Update();
		update.set("newMessageCotent", messageAutoSend.getNewMessageCotent());
		update.set("oldMessageCotent", messageAutoSend.getOldMessageCotent());
		mongoTemplate.upsert(new Query(Criteria.where("merchantId").is(messageAutoSend.getMerchantId())), update, MessageAutoSend.class);
	}

	public MessageAutoSend getMessageAutoSend(ObjectId merchantId) {
		return mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchantId)), MessageAutoSend.class);
	}

	public String processContent(Merchant merchant, String phoneNumber, String content) {
		Customer customer = mongoTemplate.findOne(new Query(Criteria.where("phone").is(phoneNumber)), Customer.class);
		return processContent(merchant, customer, content);
	}

	public MessageStatisticsDto findMessageStastics(ObjectId merchantId) {
		MessageStatisticsDto messageStatisticsDto = new MessageStatisticsDto();
		messageStatisticsDto.setHandUpMessageSentCountAWeek(findMessageSentCountFromToday(merchantId, 7, MessageType.挂机短信));
		messageStatisticsDto.setMessageGroupSentCountAWeek(findMessageSentCountFromToday(merchantId, 7, MessageType.群发));
		messageStatisticsDto.setHandUpMessageSentCountThreeDay(findMessageSentCountFromToday(merchantId, 3, MessageType.挂机短信));
		messageStatisticsDto.setMessageGroupSentCountThreeDay(findMessageSentCountFromToday(merchantId, 3, MessageType.群发));
		return messageStatisticsDto;
	}

	public long findMessageSentCountFromToday(ObjectId merchantId, int dayCount, MessageType messageType) {
		Date endTime = new Date();
		Date startTime = DateUtils.addDays(endTime, 0 - dayCount);
		// 获取开始时间的0点0分0秒
		startTime = DateFormateUtils.getStartDateOfDay(startTime);
		return findMessageSentCount(merchantId, messageType, startTime, endTime);
	}

	public long findMessageSentCount(ObjectId merchantId, MessageType messageType, Date startTime, Date endTime) {
		Query query = new Query();
		Criteria criteria = Criteria.where("merchantId").is(merchantId).and("status").is(MessageStatus.已发送);
		Date date = new Date();
		if (startTime == null) {
			startTime = DateFormateUtils.getStartDateOfDay(date);
		}
		if (endTime == null) {
			endTime = date;
		}
		criteria.and("sendTime").gte(startTime).lte(endTime);
		if (messageType != null) {
			criteria.and("type").is(messageType);
		}
		query.addCriteria(criteria);
		return mongoTemplate.count(query, MessageSendLog.class);
	}

	public String processContent(Merchant merchant, Customer customer, String content) {
		if (customer == null) {
			return content;
		}
		String[] suffix = { "先生", "女士", "小姐" };
		assert (merchant != null);
		boolean isHasSuffix = false;
		String customerName = customer.getName();
		if (merchant.getSetting().isNameMatch() == true && customer != null) {
			for (String s : suffix) {
				if (customer.getName() == null) {
					return content;
				}
				if (customer.getName().endsWith(s)) {
					isHasSuffix = true;
					break;
				}
			}
			if (!isHasSuffix) {
				if (customer.getSex() == null) {
					customerName += "先生/女士";
				} else if (Sex.FEMALE.equals(customer.getSex())) {
					customerName += "女士";
				} else if (Sex.MALE.equals(customer.getSex())) {
					customerName += "先生";
				}
			}
		}
		return "尊敬的" + customerName + ":" + content;
	}

	public void remindSend(String title, ObjectId merchantId) throws Exception {
		RemindTemplate remindTemplate = mongoTemplate.findOne(new Query(Criteria.where("title").is(title)), RemindTemplate.class);
		if (remindTemplate == null) {
			return;
		}
		MerchantRemindTemplate merchantRemindTemplate = mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchantId).and("remindTemplate.$id").is(remindTemplate.getId())), MerchantRemindTemplate.class);
		String content = merchantRemindTemplate.getMessageContent();
		List<Customer> customers=findCustomerByMerchantRemindTemplate(title, merchantId,remindTemplate,merchantRemindTemplate);
		Set<String> phones = new HashSet<String>();
		for (Customer c : customers) {
			phones.add(c.getPhone());
		}
		String[] Arrayphone = new String[phones.size()];
		phones.toArray(Arrayphone);
		sendMessages(merchantId, Arrayphone, content);
	}
	
	public void ignoreSendMessages(String title, ObjectId merchantId) {
		RemindTemplate remindTemplate = mongoTemplate.findOne(new Query(Criteria.where("title").is(title)), RemindTemplate.class);
		if (remindTemplate == null) {
			return;
		}
		MerchantRemindTemplate merchantRemindTemplate = mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchantId).and("remindTemplate.$id").is(remindTemplate.getId())), MerchantRemindTemplate.class);
		@SuppressWarnings("unused")
		List<Customer> customers=findCustomerByMerchantRemindTemplate(title, merchantId,remindTemplate,merchantRemindTemplate);
	}
	
	public List<Customer> findCustomerByMerchantRemindTemplate(String title, ObjectId merchantId,RemindTemplate remindTemplate,MerchantRemindTemplate merchantRemindTemplate ){
		Query query = new Query();
		Criteria criteria = Criteria.where("merchantId").is(merchantId);
		Date startTime = new Date();
		Date endTime = DateFormateUtils.calEndTime(startTime, merchantRemindTemplate.getAdvancedTime());
		criteria.and("reminds.remindTime").gte(startTime).lte(endTime);
		criteria.and("reminds.merchantRemindTemplate.$id").is(merchantRemindTemplate.getId());
		query.addCriteria(criteria);
		List<Customer> customers = mongoTemplate.find(query, Customer.class);
		for (Customer c : customers) {
			for (Remind r : c.getReminds()) {
				if (r.getMerchantRemindTemplate().equals(merchantRemindTemplate) && r.isAlreadyRemind() == false) {
					r.setAlreadyRemind(true);
					mongoTemplate.save(c);
				}
			}

		}
		return customers;
	}

}
