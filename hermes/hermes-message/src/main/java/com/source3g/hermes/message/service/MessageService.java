package com.source3g.hermes.message.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.jms.Destination;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.source3g.hermes.constants.JmsConstants;
import com.source3g.hermes.dto.message.MessageStatisticsDto;
import com.source3g.hermes.dto.message.StatisticObjectDto;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.CustomerGroup;
import com.source3g.hermes.entity.customer.Remind;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.merchant.MerchantRemindTemplate;
import com.source3g.hermes.entity.merchant.RemindTemplate;
import com.source3g.hermes.entity.message.AutoSendMessageTemplate;
import com.source3g.hermes.entity.message.GroupSendLog;
import com.source3g.hermes.entity.message.MessageSendLog;
import com.source3g.hermes.entity.message.MessageTemplate;
import com.source3g.hermes.entity.message.ShortMessage;
import com.source3g.hermes.enums.MessageStatus;
import com.source3g.hermes.enums.MessageType;
import com.source3g.hermes.enums.PhoneOperator;
import com.source3g.hermes.enums.Sex;
import com.source3g.hermes.message.GroupSendMsg;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.service.JmsService;
import com.source3g.hermes.utils.DateFormateUtils;
import com.source3g.hermes.utils.Page;
import com.source3g.hermes.utils.PhoneUtils;

@Service
public class MessageService extends BaseService {
	private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

	@Autowired
	private JmsService jmsService;
	@Autowired
	private Destination messageDestination;
	@Autowired
	private CbipMesssageService cbipMesssageService;
	@Autowired
	private TcpCommandService tcpCommandService;

	@Value(value = "${message.channel.select}")
	private int channel;

	/**
	 * 短信群发
	 * 
	 * @param merchantId
	 * @param ids
	 * @param content
	 */
	public void groupSend(ObjectId merchantId, String[] ids, String customerPhones, String content) throws Exception {
		String customerPhoneArray[] = {};
		if (customerPhones != null) {
			customerPhoneArray = isNumeric(customerPhones);
		}
		Long count = findCustomerCountByGroupIds(ids);
		count += customerPhoneArray.length;
		checkSurplus(merchantId, count);
		// 生成群发记录
		GroupSendLog groupSendLog = genGroupSendLog(count.intValue(), content, merchantId);
		GroupSendMsg groupSendMsg = new GroupSendMsg(customerPhoneArray, ids, content, merchantId, groupSendLog.getId());
		jmsService.sendObject(messageDestination, groupSendMsg, JmsConstants.TYPE, JmsConstants.GROUP_SEND_MESSAGE);
	}

	public String[] isNumeric(String customerPhones) {
		Pattern pattern = Pattern.compile("\\d{11}");
		// Matcher isNum =null;
		String customerPhoneArray[] = customerPhones.split(";");
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < customerPhoneArray.length; i++) {
			if (pattern.matcher(customerPhoneArray[i]).matches()) {
				list.add(customerPhoneArray[i]);
			}
		}
		String[] length = new String[list.size()];
		return list.toArray(length);
	}

	private long findCustomerCountByGroupIds(String[] ids) {
		if (ids == null || ids.length == 0) {
			return 0L;
		}
		List<ObjectId> customerGroupIds = new ArrayList<ObjectId>();
		for (String id : ids) {
			ObjectId ObjId = new ObjectId(id);
			customerGroupIds.add(ObjId);
		}
		BasicDBObject parameter = new BasicDBObject();
		parameter.put("customerGroup.$id", new BasicDBObject("$in", customerGroupIds));
		return findCountByBasicDBObject(Customer.class, parameter);
	}

	public List<Customer> findCustomerByGroupIds(String[] ids) {
		List<ObjectId> customerGroupIds = new ArrayList<ObjectId>();
		for (String id : ids) {
			ObjectId ObjId = new ObjectId(id);
			customerGroupIds.add(ObjId);
		}
		BasicDBObject parameter = new BasicDBObject();
		parameter.put("customerGroup.$id", new BasicDBObject("$in", customerGroupIds));
		List<Customer> customers = findByBasicDBObject(Customer.class, parameter, new ObjectMapper<Customer>() {
			@Override
			public Customer mapping(DBObject obj) {
				Customer customer = new Customer();
				customer.setName((String) obj.get("name"));
				customer.setPhone((String) obj.get("phone"));
				Object sexObj = obj.get("sex");
				if (sexObj != null) {
					customer.setSex(Sex.valueOf((String) sexObj));
				}
				customer.setId((ObjectId) obj.get("_id"));
				customer.setMerchantId((ObjectId) obj.get("merchantId"));
				DBRef dbRef = (DBRef) obj.get("customerGroup");
				customer.setCustomerGroup(new CustomerGroup((ObjectId) dbRef.getId()));
				return customer;
			}

		});
		return customers;
	}

	public MessageSendLog genMessageSendLog(Customer customer, int sendCount, String content, MessageType type, MessageStatus status) {
		MessageSendLog log = new MessageSendLog();
		log.setId(ObjectId.get());
		log.setContent(content);
		log.setSendTime(new Date());
		log.setCustomerName(customer.getName());
		log.setCustomerGroup(customer.getCustomerGroup());
		log.setMerchantId(customer.getMerchantId());
		log.setPhone(customer.getPhone());
		log.setSendCount(sendCount);
		log.setType(type);
		log.setStatus(status);
		mongoTemplate.insert(log);
		return log;
	}

	public MessageSendLog genMessageSendLog(ObjectId merchantId, String phone, int sendCount, String content, MessageType type, MessageStatus status) {
		MessageSendLog log = new MessageSendLog();
		log.setId(ObjectId.get());
		log.setContent(content);
		log.setSendTime(new Date());
		log.setMerchantId(merchantId);
		log.setPhone(phone);
		log.setSendCount(sendCount);
		log.setType(type);
		log.setStatus(status);
		mongoTemplate.insert(log);
		return log;
	}

	private GroupSendLog genGroupSendLog(int sendCount, String content, ObjectId merchantId) {
		GroupSendLog groupSendLog = new GroupSendLog();
		groupSendLog.setId(ObjectId.get());
		groupSendLog.setMerchantId(merchantId);
		groupSendLog.setContent(content);
		groupSendLog.setSendCount(sendCount);
		groupSendLog.setSendTime(new Date());
		groupSendLog.setId(ObjectId.get());
		groupSendLog.setSuccessCount(0);
		mongoTemplate.save(groupSendLog);
		return groupSendLog;
	}

	public List<MessageTemplate> listAll(String merchantId) {
		return mongoTemplate.find(new Query(Criteria.where("merchantId").is(new ObjectId(merchantId))).with(new Sort(Direction.DESC, "_id")), MessageTemplate.class);
	}

	public void save(MessageTemplate messageTemplate) {
		mongoTemplate.save(messageTemplate);
	}

	public void sendMessage(ObjectId merchantId, String phone, String content, MessageType messageType, ObjectId logId) {
		Customer c = mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchantId).and("phone").is(phone)), Customer.class);
		if (c == null) {
			sendByPhone(merchantId, phone, content, messageType, logId);
		} else {
			sendMessage(c, content, messageType, logId);
		}

	}

	/**
	 * 私有，调用这个方法时，phone为库中没有的
	 * 
	 * @param merchantId
	 * @param phone
	 * @param content
	 * @param messageType
	 * @param logId
	 */
	private void sendByPhone(ObjectId merchantId, String phone, String content, MessageType messageType, ObjectId logId) {
		ShortMessage shortMessage = new ShortMessage();
		shortMessage.setContent(content);
		shortMessage.setMessageType(messageType);
		shortMessage.setId(ObjectId.get());
		shortMessage.setMerchantId(merchantId);
		shortMessage.setPhone(phone);
		shortMessage.setSendId(logId);
		jmsService.sendObject(messageDestination, shortMessage, JmsConstants.TYPE, JmsConstants.SEND_MESSAGE);
	}

	/**
	 * 
	 * @param c
	 * @param content
	 * @param messageType
	 * @throws Exception
	 */
	public void sendMessage(Customer c, String content, MessageType messageType, ObjectId logId) {
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("_id").is(c.getMerchantId())), Merchant.class);
		String proceedContent = processContent(merchant, c, content);
		sendMessageWithProceedContent(c, proceedContent, messageType, logId);
	}

	/**
	 * 
	 * @param c
	 * @param content
	 * @param messageType
	 * @throws Exception
	 */
	private void sendMessageWithProceedContent(Customer c, String proceedContent, MessageType messageType, ObjectId logId) {
		ShortMessage shortMessage = new ShortMessage();
		shortMessage.setContent(proceedContent);
		shortMessage.setMessageType(messageType);
		shortMessage.setId(ObjectId.get());
		shortMessage.setMerchantId(c.getMerchantId());
		shortMessage.setPhone(c.getPhone());
		shortMessage.setSendId(logId);
		jmsService.sendObject(messageDestination, shortMessage, JmsConstants.TYPE, JmsConstants.SEND_MESSAGE);
	}

	public void singleSend(Customer c, String content, MessageType type) {
		String proceedContent = processContent(c.getMerchantId(), c, content);
		MessageSendLog messageSendLog = genMessageSendLog(c, 1, proceedContent, type, MessageStatus.发送中);
		sendMessageWithProceedContent(c, proceedContent, MessageType.提醒短信, messageSendLog.getId());
	}

	public void singleSend(ObjectId merchantId, String customerPhone, String content, MessageType messageType) {
		Customer c = mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchantId).and("phone").is(customerPhone)), Customer.class);
		if (c == null) {
			MessageSendLog messageSendLog = genMessageSendLog(c, 1, content, messageType, MessageStatus.发送中);
			sendByPhone(merchantId, customerPhone, content, messageType, messageSendLog.getId());
		} else {
			singleSend(c, content, messageType);
		}
	}

	/**
	 * 暂时测试用
	 */
	public void groupSend() {
		String a = "a";
		jmsService.sendObject(messageDestination, a, JmsConstants.TYPE, JmsConstants.GROUP_SEND_MESSAGE);
	}

	private void checkSurplus(ObjectId merchantId, long count) throws Exception {
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("_id").is(merchantId)), Merchant.class);
		if (count > merchant.getMessageBalance().getSurplusMsgCount()) {
			throw new Exception("余额不足");
		}
	}

	// 查找群发记录
	public List<GroupSendLog> groupSendLogList(ObjectId merchantId) {
		Query query = new Query();
		Criteria criteria = Criteria.where("merchantId").is(merchantId);
		query.addCriteria(criteria);
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

	public MessageStatus send(ShortMessage message) {
		MessageStatus status = sendByOperator(message.getMsgId(), message.getPhone(), message.getContent(), PhoneUtils.getOperatior(message.getPhone()));
		System.out.println("submit");
		message.setStatus(status);
		message.setSendTime(new Date());
		save(message);
		return status;
	}

	@Deprecated
	public MessageStatus send(String msgId, String phoneNumber, String content) {
		return sendByOperator(msgId, phoneNumber, content, PhoneUtils.getOperatior(phoneNumber));
	}

	private MessageStatus sendByOperator(String msgId, String phoneNumber, String content, PhoneOperator operator) {
		logger.debug("通过" + operator.toString() + "向" + phoneNumber + "发送" + content + "msgId:" + msgId);
		try {
			if (channel == 1) {
				cbipMesssageService.send(msgId, phoneNumber, content, operator);
			} else {
				tcpCommandService.send(msgId, phoneNumber, content, operator);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return MessageStatus.发送失败;
		}
		return MessageStatus.已发送;
	}

	public void saveMessageAutoSend(AutoSendMessageTemplate AutoSendMessageTemplate) {
		Update update = new Update();
		update.set("newMessageCotent", AutoSendMessageTemplate.getNewMessageCotent());
		update.set("oldMessageCotent", AutoSendMessageTemplate.getOldMessageCotent());
		mongoTemplate.upsert(new Query(Criteria.where("merchantId").is(AutoSendMessageTemplate.getMerchantId())), update, AutoSendMessageTemplate.class);
	}

	public AutoSendMessageTemplate getMessageAutoSend(ObjectId merchantId) {
		return mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchantId)), AutoSendMessageTemplate.class);
	}

	public List<Map<String, Object>> findMessageStastics(ObjectId merchantId) {
		MessageStatisticsDto messageStatisticsDto = new MessageStatisticsDto();
		messageStatisticsDto.setHandUpMessageSentCountAWeek(new StatisticObjectDto("一周内挂机短信发送数量：", findMessageSentCountFromToday(merchantId, 7, MessageType.挂机短信)));
		messageStatisticsDto.setMessageGroupSentCountAWeek(new StatisticObjectDto("一周内短信群发数量：", findMessageSentCountFromToday(merchantId, 7, MessageType.群发)));
		messageStatisticsDto.setHandUpMessageSentCountThreeDay(new StatisticObjectDto("三天内挂机短信群发数量：", findMessageSentCountFromToday(merchantId, 3, MessageType.挂机短信)));
		messageStatisticsDto.setMessageGroupSentCountThreeDay(new StatisticObjectDto("三天内短信群发数量：", findMessageSentCountFromToday(merchantId, 3, MessageType.群发)));
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> handUpMessageSentCountThreeDay = new HashMap<String, Object>();
		Map<String, Object> handUpMessageSentCountAWeek = new HashMap<String, Object>();
		Map<String, Object> messageGroupSentCountThreeDay = new HashMap<String, Object>();
		Map<String, Object> messageGroupSentCountAWeek = new HashMap<String, Object>();

		handUpMessageSentCountThreeDay.put("handUpMessageSentCountThreeDay", messageStatisticsDto.getHandUpMessageSentCountThreeDay());
		handUpMessageSentCountAWeek.put("handUpMessageSentCountAWeek", messageStatisticsDto.getHandUpMessageSentCountAWeek());
		messageGroupSentCountThreeDay.put("messageGroupSentCountThreeDay", messageStatisticsDto.getMessageGroupSentCountThreeDay());
		messageGroupSentCountAWeek.put("messageGroupSentCountAWeek", messageStatisticsDto.getMessageGroupSentCountAWeek());

		list.add(handUpMessageSentCountThreeDay);
		list.add(handUpMessageSentCountAWeek);
		list.add(messageGroupSentCountThreeDay);
		list.add(messageGroupSentCountAWeek);
		return list;
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

	public String processContent(ObjectId merchantId, Customer customer, String content) {
		Merchant merchant = mongoTemplate.findById(merchantId, Merchant.class);
		return processContent(merchant, customer, content);
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
		List<Customer> customers = findCustomerByMerchantRemindTemplate(title, merchantId, remindTemplate, merchantRemindTemplate);
		Set<Customer> customersSet = new HashSet<Customer>(customers);
		for (Customer c : customersSet) {
			singleSend(c, content, MessageType.提醒短信);
		}
	}

	public void ignoreSendMessages(String title, ObjectId merchantId) {
		RemindTemplate remindTemplate = mongoTemplate.findOne(new Query(Criteria.where("title").is(title)), RemindTemplate.class);
		if (remindTemplate == null) {
			return;
		}
		MerchantRemindTemplate merchantRemindTemplate = mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchantId).and("remindTemplate.$id").is(remindTemplate.getId())), MerchantRemindTemplate.class);
		@SuppressWarnings("unused")
		List<Customer> customers = findCustomerByMerchantRemindTemplate(title, merchantId, remindTemplate, merchantRemindTemplate);
	}

	public List<Customer> findCustomerByMerchantRemindTemplate(String title, ObjectId merchantId, RemindTemplate remindTemplate, MerchantRemindTemplate merchantRemindTemplate) {
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

	public void updateShortMessageStatus(String msgId, MessageStatus messageStatus) {
		Update update = new Update();
		update.set("status", messageStatus);
		mongoTemplate.updateFirst(new Query(Criteria.where("msgId").is(msgId)), update, ShortMessage.class);
	}

	public Page failedMessagelist(int pageNoInt) {
		Query query = new Query();
		query.with(new Sort(Direction.DESC, "_id"));
		query.addCriteria(Criteria.where("status").is(MessageStatus.提交失败));
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, ShortMessage.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNoInt);
		List<ShortMessage> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), ShortMessage.class);
		page.setData(list);
		return page;
	}

	public void failedMessageResend(String id) {
		ShortMessage message = mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), ShortMessage.class);
		failedMessageResend(message);
	}

	public void failedMessageResend(ShortMessage message) {
		message.setStatus(MessageStatus.重新发送);
		jmsService.sendObject(messageDestination, message, JmsConstants.TYPE, JmsConstants.SEND_MESSAGE);
	}

	public void allFailedMessagesResend() {
		List<ShortMessage> list = mongoTemplate.find(new Query(Criteria.where("status").is(MessageStatus.提交失败)), ShortMessage.class);
		for (ShortMessage shortMessage : list) {
			failedMessageResend(shortMessage);
		}
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public TcpCommandService getTcpCommandService() {
		return tcpCommandService;
	}

	public void setTcpCommandService(TcpCommandService tcpCommandService) {
		this.tcpCommandService = tcpCommandService;
	}

}
