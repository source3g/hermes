package com.source3g.hermes.message.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.lxt2.protocol.common.Standard_SeqNum;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.source3g.hermes.dto.customer.CustomerRemindDto;
import com.source3g.hermes.dto.customer.CustomerRemindDto.CustomerInfo;
import com.source3g.hermes.dto.message.MessageStatisticsDto;
import com.source3g.hermes.dto.message.StatisticObjectDto;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.CustomerGroup;
import com.source3g.hermes.entity.customer.Remind;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.merchant.MerchantRemindTemplate;
import com.source3g.hermes.entity.message.AutoSendMessageTemplate;
import com.source3g.hermes.entity.message.GroupSendLog;
import com.source3g.hermes.entity.message.MessageSendLog;
import com.source3g.hermes.entity.message.MessageTemplate;
import com.source3g.hermes.entity.message.ShortMessage;
import com.source3g.hermes.enums.MessageStatus;
import com.source3g.hermes.enums.MessageType;
import com.source3g.hermes.enums.Sex;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.service.JmsService;
import com.source3g.hermes.utils.FormateUtils;
import com.source3g.hermes.utils.Page;
import com.source3g.hermes.utils.PhoneUtils;

@Service
public class MessageService extends BaseService {
	private static final Logger logger = LoggerFactory.getLogger(MessageService.class);
	@Autowired
	private JmsService jmsService;
	@Autowired
	private MessageQueueService messageQueueService;
	@Autowired
	private PositiveSenderService positiveSenderService;
	@Autowired
	private CbipMesssageService cbipMesssageService;
	@Autowired
	private TcpCommandService tcpCommandService;
	@Autowired
	private XuntongMessageService xuntongMessageService;

	/**
	 * 短信群发
	 * 
	 * @param merchantId
	 * @param ids
	 * @param content
	 */
	public void groupSend(ObjectId merchantId, String[] ids, String[] customerPhones, String content) throws Exception {
		String customerPhoneArray[] = {};
		if (customerPhones != null) {
			customerPhoneArray = getMobilePhone(customerPhones);
		}
		// 生成群发记录
		GroupSendLog groupSendLog = genGroupSendLog(0, content, merchantId);
		logger.debug("开始群发消息:" + content);
		Set<Customer> customerSet = new HashSet<Customer>();
		if (ids != null && ids.length > 0) {
			List<Customer> customerList = findCustomerByGroupIds(ids);
			customerSet.addAll(customerList);
		}
		Set<String> phoneSet = new HashSet<String>();
		if (customerPhoneArray != null && customerPhoneArray.length > 0) {
			for (String phone : customerPhoneArray) {
				Customer c = mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchantId).and("phone").is(phone)), Customer.class);
				if (c != null) {
					customerSet.add(c);
				} else {
					phoneSet.add(phone);
				}
			}
		}
		Set<ShortMessage> shortMessageSet = processGroupMessage(merchantId, customerSet, phoneSet, content, MessageType.群发, groupSendLog.getId());
		checkSurplus(merchantId, shortMessageSet.size());
		groupSendLog.setSendCount(shortMessageSet.size());
		mongoTemplate.insert(groupSendLog);
		for (ShortMessage message : shortMessageSet) {
			send(message);
		}
	}

	private Set<ShortMessage> processGroupMessage(ObjectId merchantId, Set<Customer> customerSet, Set<String> phoneSet, String content,
			MessageType messageType, ObjectId groupLogId) {
		Set<ShortMessage> result = new HashSet<ShortMessage>();
		Merchant merchant = super.findOne(new Query(Criteria.where("_id").is(merchantId)), Merchant.class);
		int priority = customerSet.size() + phoneSet.size();
		for (Customer c : customerSet) {
			if (PhoneUtils.isMobile(c.getPhone())) {
				String proceedContent = processContent(merchant, c, content);
				result.add(genShortMessage(proceedContent, c.getPhone(), messageType, merchant.getId(), groupLogId, priority));
			}
		}
		for (String phone : phoneSet) {
			result.add(genShortMessage(content, phone, messageType, merchantId, groupLogId, priority));
		}
		return result;
	}

	private ShortMessage genShortMessage(String content, String phone, MessageType type, ObjectId merchantId, ObjectId logId, int priority) {
		ShortMessage shortMessage = new ShortMessage();
		shortMessage.setContent(content);
		shortMessage.setMessageType(type);
		shortMessage.setId(ObjectId.get());
		shortMessage.setMerchantId(merchantId);
		shortMessage.setPhone(phone);
		shortMessage.setSendId(logId);
		shortMessage.setStatus(MessageStatus.发送中);
		shortMessage.setPriority(priority);
		return shortMessage;
	}

	public String[] getMobilePhone(String[] customerPhones) {
		if (customerPhones == null || customerPhones.length == 0) {
			return new String[0];
		}
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < customerPhones.length; i++) {
			if (PhoneUtils.isMobile(customerPhones[i])) {
				list.add(customerPhones[i]);
			}
		}
		String[] length = new String[list.size()];
		return list.toArray(length);
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
		return groupSendLog;
	}

	public List<MessageTemplate> listAll(String merchantId) {
		return mongoTemplate.find(new Query(Criteria.where("merchantId").is(new ObjectId(merchantId))).with(new Sort(Direction.DESC, "_id")),
				MessageTemplate.class);
	}

	public void save(MessageTemplate messageTemplate) {
		mongoTemplate.save(messageTemplate);
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
	private void sendByOriginalContent(ObjectId merchantId, String phone, String content, MessageType messageType, ObjectId logId) {
		send(genShortMessage(content, phone, messageType, merchantId, logId, 0));
	}

	public void singleSend(Customer c, String content, MessageType type) {
		String proceedContent = processContent(c.getMerchantId(), c, content);
		MessageSendLog messageSendLog = genMessageSendLog(c, 1, proceedContent, type, MessageStatus.发送中);
		sendByOriginalContent(c.getMerchantId(), c.getPhone(), proceedContent, type, messageSendLog.getId());
	}

	public void singleSend(ObjectId merchantId, String customerPhone, String content, MessageType messageType) {
		if (!PhoneUtils.isMobile(customerPhone)) {
			return;
		}
		Customer c = mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchantId).and("phone").is(customerPhone)), Customer.class);
		if (c == null) {
			MessageSendLog messageSendLog = genMessageSendLog(merchantId, customerPhone, 1, content, messageType, MessageStatus.发送中);
			sendByOriginalContent(merchantId, customerPhone, content, messageType, messageSendLog.getId());
		} else {
			singleSend(c, content, messageType);
		}
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
			CustomerGroup customerGroup = mongoTemplate.findOne(
					new Query(Criteria.where("name").is(customerGroupName).and("merchantId").is(merchantId)), CustomerGroup.class);
			criteria.and("customerGroup").is(customerGroup);
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

	public void addGroupSuccess(ObjectId groupId) {
		Update updateGroupLog = new Update();
		updateGroupLog.inc("successCount", 1);
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(groupId)), updateGroupLog, GroupSendLog.class);
	}

	public MessageStatus send(ShortMessage message) {
		message.setMsgId(String.valueOf(Standard_SeqNum.computeSeqNoErr(1)));
		MessageStatus status = MessageStatus.发送中;
		try {
			Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("_id").is(message.getMerchantId())), Merchant.class);
			if (merchant == null) {
				throw new Exception("商户不存在");
			}
			int messageCount = (message.getContent().length() / 70) + 1;
			if (merchant.getMessageBalance().getSurplusMsgCount() - messageCount <= 0) {
				throw new Exception("余额不足发送失败");
			}
			if (!MessageStatus.重新发送.equals(message.getStatus())) {
				logger.debug("发送到消息缓冲区:" + message.getContent() + " 电话:" + message.getPhone() + " 类型:" + message.getMessageType());
				Update updateSurplus = new Update();
				updateSurplus.inc("messageBalance.surplusMsgCount", 0 - messageCount).inc("messageBalance.totalCount", 0 - messageCount)
						.inc("messageBalance.sentCount", messageCount);
				mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(merchant.getId())), updateSurplus, Merchant.class);
				if (MessageType.群发.equals(message.getMessageType())) {
					// addGroupSuccess(message.getSendId());
				} else {
					updateLog(message.getSendId(), new Date(), MessageStatus.发送中);
				}
			}
			message.setStatus(status);
			message.setSendTime(new Date());
			messageQueueService.add(message);
			save(message);
		} catch (Exception e) {
			if (message != null) {
				try {
					status = MessageStatus.valueOf(e.getMessage());
				} catch (Exception e2) {
					status = MessageStatus.发送失败;
				}
				Update update = new Update();
				update.set("status", status);
				mongoTemplate.upsert(new Query(Criteria.where("_id").is(message.getSendId())), update, MessageSendLog.class);
				message.setSendTime(new Date());
				message.setStatus(status);
				save(message);
			}
		}
		return status;
	}

	public void saveMessageAutoSend(AutoSendMessageTemplate AutoSendMessageTemplate) {
		Update update = new Update();
		update.set("newMessageCotent", AutoSendMessageTemplate.getNewMessageCotent());
		update.set("oldMessageCotent", AutoSendMessageTemplate.getOldMessageCotent());
		mongoTemplate.upsert(new Query(Criteria.where("merchantId").is(AutoSendMessageTemplate.getMerchantId())), update,
				AutoSendMessageTemplate.class);
	}

	public AutoSendMessageTemplate getMessageAutoSend(ObjectId merchantId) {
		return mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchantId)), AutoSendMessageTemplate.class);
	}

	public List<Map<String, Object>> findMessageStastics(ObjectId merchantId) {
		MessageStatisticsDto messageStatisticsDto = new MessageStatisticsDto();
		messageStatisticsDto.setHandUpMessageSentCountAWeek(new StatisticObjectDto("一周内挂机短信发送数量：", findMessageSentCountFromToday(merchantId, 7,
				MessageType.挂机短信)));
		messageStatisticsDto.setMessageGroupSentCountAWeek(new StatisticObjectDto("一周内短信群发数量：", findMessageSentCountFromToday(merchantId, 7,
				MessageType.群发)));
		messageStatisticsDto.setHandUpMessageSentCountThreeDay(new StatisticObjectDto("三天内挂机短信群发数量：", findMessageSentCountFromToday(merchantId, 3,
				MessageType.挂机短信)));
		messageStatisticsDto.setMessageGroupSentCountThreeDay(new StatisticObjectDto("三天内短信群发数量：", findMessageSentCountFromToday(merchantId, 3,
				MessageType.群发)));
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
		startTime = FormateUtils.getStartDateOfDay(startTime);
		return findMessageSentCount(merchantId, messageType, startTime, endTime);
	}

	public long findMessageSentCount(ObjectId merchantId, MessageType messageType, Date startTime, Date endTime) {
		Query query = new Query();
		Criteria criteria = Criteria.where("merchantId").is(merchantId).and("status").is(MessageStatus.已发送);
		Date date = new Date();
		if (startTime == null) {
			startTime = FormateUtils.getStartDateOfDay(date);
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
		if (!merchant.getSetting().isTitle()) {
			return content;
		}

		String title = "尊敬的";
		String[] suffix = { "先生", "女士", "小姐" };
		assert (merchant != null);
		boolean isHasSuffix = false;
		String customerName;
		if (customer.getName() == null) {
			customerName = customer.getPhone();
		} else {
			customerName = customer.getName();
		}

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
		title += customerName;
		return title + ":\n" + content;
	}

	public void remindSend(String title, ObjectId merchantId) throws Exception {
//		RemindTemplate remindTemplate = mongoTemplate.findOne(new Query(Criteria.where("title").is(title)), RemindTemplate.class);
//		if (remindTemplate == null) {
//			return;
//		}
		MerchantRemindTemplate merchantRemindTemplate = mongoTemplate.findOne(
				new Query(Criteria.where("merchantId").is(merchantId).and("title").is(title)),
				MerchantRemindTemplate.class);
		String content = merchantRemindTemplate.getMessageContent();
		Date startTime = new Date();
		Date endTime = FormateUtils.calEndTime(startTime, merchantRemindTemplate.getAdvancedTime() + 1);
		List<Customer> customers = findTodayRemindCustomers(merchantId, merchantRemindTemplate, startTime, endTime);// findCustomerByMerchantRemindTemplate(title,
		for (Customer c : customers) {
			singleSend(c, content, MessageType.提醒短信);
			setAlreadyRemind(c, merchantRemindTemplate, true);
		}
	}

	private void setAlreadyRemind(Customer c, MerchantRemindTemplate merchantRemindTemplate, boolean alreadyRemind) {
		Update update = new Update();
		update.set("reminds.$.alreadyRemind", alreadyRemind);
		Date startTime = new Date();
		Date endTime = FormateUtils.calEndTime(startTime, merchantRemindTemplate.getAdvancedTime() + 1);
		mongoTemplate.updateFirst(
				new Query(Criteria
						.where("_id")
						.is(c.getId())
						.and("reminds")
						.elemMatch(
								Criteria.where("merchantRemindTemplate.$id").is(merchantRemindTemplate.getId()).and("remindTime").gte(startTime)
										.lte(endTime).and("alreadyRemind").is(false))), update, Customer.class);
	}

	public void ignoreSendMessages(String title, ObjectId merchantId) {
//		RemindTemplate remindTemplate = mongoTemplate.findOne(new Query(Criteria.where("title").is(title)), RemindTemplate.class);
//		if (remindTemplate == null) {
//			return;
//		}
		MerchantRemindTemplate merchantRemindTemplate = mongoTemplate.findOne(
				new Query(Criteria.where("merchantId").is(merchantId).and("title").is(title)),
				MerchantRemindTemplate.class);
		Date startTime = new Date();
		Date endTime = FormateUtils.calEndTime(startTime, merchantRemindTemplate.getAdvancedTime() + 1);
		List<Customer> customers = findTodayRemindCustomers(merchantId, merchantRemindTemplate, startTime, endTime);
		for (Customer c : customers) {
			setAlreadyRemind(c, merchantRemindTemplate, true);
		}
	}

	public List<CustomerRemindDto> findTodayReminds(ObjectId merchantId) {
		List<CustomerRemindDto> result = new ArrayList<CustomerRemindDto>();
		List<MerchantRemindTemplate> merchantRemindTemplates = mongoTemplate.find(new Query(Criteria.where("merchantId").is(merchantId)),
				MerchantRemindTemplate.class);
		Date startTime = new Date();

		for (MerchantRemindTemplate merchantRemindTemplate : merchantRemindTemplates) {
			Date endTime = FormateUtils.calEndTime(startTime, merchantRemindTemplate.getAdvancedTime() + 1);
			List<Customer> customers = findTodayRemindCustomers(merchantId, merchantRemindTemplate, startTime, endTime);
			if (CollectionUtils.isEmpty(customers)) {
				continue;
			}
			CustomerRemindDto customerRemindDto = new CustomerRemindDto();
			customerRemindDto.setAdvancedTime(merchantRemindTemplate.getAdvancedTime());
			customerRemindDto.setContent(merchantRemindTemplate.getMessageContent());
			customerRemindDto.setTitle(merchantRemindTemplate.getTitle());
			customerRemindDto.setMerchantRemindId(merchantRemindTemplate.getId());
			for (Customer customer : customers) {
				for (Remind remind : customer.getReminds()) {
					if (remind.getMerchantRemindTemplate().getId().equals(merchantRemindTemplate.getId())
							&& remind.getRemindTime().getTime() > startTime.getTime() && remind.getRemindTime().getTime() < endTime.getTime()
							&& remind.isAlreadyRemind() == false) {
						CustomerInfo customerInfo = new CustomerInfo(customer.getName(), customer.getPhone(), remind.getRemindTime());
						customerRemindDto.addRemind(customerInfo);
					}
				}
			}
			result.add(customerRemindDto);
		}
		return result;
	}

	public List<Customer> findTodayRemindCustomers(ObjectId merchantId, MerchantRemindTemplate merchantRemindTemplate, Date startTime, Date endTime) {
		Query query = new Query();
		Criteria criteria = Criteria.where("merchantId").is(merchantId);
		criteria.and("reminds").elemMatch(
				Criteria.where("remindTime").gte(startTime).lte(endTime).and("merchantRemindTemplate.$id").is(merchantRemindTemplate.getId())
						.and("alreadyRemind").is(false));
		query.addCriteria(criteria);
		List<Customer> customers = mongoTemplate.find(query, Customer.class);
		if (CollectionUtils.isEmpty(customers)) {
			return null;
		}
		return customers;
	}

	public void updateShortMessageStatus(String msgId, MessageStatus messageStatus) {
		Update update = new Update();
		update.set("status", messageStatus);
		mongoTemplate.updateFirst(new Query(Criteria.where("msgId").is(msgId)), update, ShortMessage.class);
	}

	public Page failedMessagelist(int pageNoInt, Date startTime, Date endTime, String status) {
		Query query = new Query();
		query.with(new Sort(Direction.DESC, "sendTime"));
		Criteria criteria = new Criteria();
		if (startTime != null && endTime != null) {
			criteria.and("sendTime").gte(startTime).lte(endTime);
		} else if (startTime != null) {
			criteria.and("sendTime").gte(startTime);
		} else if (endTime != null) {
			criteria.and("sendTime").lte(endTime);
		}
		if (StringUtils.isNotEmpty(status)) {
			criteria.and("status").is(status);
		}
		// query.addCriteria(Criteria.where("status").is(MessageStatus.提交失败));
		query.addCriteria(criteria);
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, ShortMessage.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNoInt);
		List<ShortMessage> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), ShortMessage.class);
		page.setData(list);
		return page;
	}

	public Boolean failedMessageResend(String id) {
		Boolean result = true;
		ShortMessage message = mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), ShortMessage.class);
		MessageStatus status = failedMessageResend(message);
		if (MessageStatus.已发送.equals(status)) {
			return result;
		}
		return false;
	}

	public MessageStatus failedMessageResend(ShortMessage message) {
		message.setStatus(MessageStatus.重新发送);
		send(message);
		return message.getStatus();
	}

	public Boolean allFailedMessagesResend(Date startTime, Date endTime, String status) {
		Boolean result = true;
		Query query = new Query();
		Criteria criteria = new Criteria();
		Date yesterday = DateUtils.addDays(new Date(), -1);
		if (startTime == null || startTime.getTime() < yesterday.getTime()) {
			startTime = yesterday;
		}

		if (startTime != null && endTime != null) {
			criteria.and("sendTime").gte(startTime).lte(endTime);
		} else if (startTime != null) {
			criteria.and("sendTime").gte(startTime);
		} else if (endTime != null) {
			criteria.and("sendTime").lte(endTime);
		}
		if (status != null) {
			criteria.and("status").is(status);
		}
		criteria.and("status").ne(MessageStatus.发送成功);

		query.addCriteria(criteria);
		List<ShortMessage> list = mongoTemplate.find(query, ShortMessage.class);
		for (ShortMessage shortMessage : list) {
			failedMessageResend(shortMessage);
		}
		return result;
	}
}
