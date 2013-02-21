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

import com.hongxun.pub.DataCommand;
import com.hongxun.pub.tcptrans.TcpCommTrans;
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
import com.source3g.hermes.enums.MessageStatus;
import com.source3g.hermes.enums.MessageType;
import com.source3g.hermes.enums.PhoneOperator;
import com.source3g.hermes.enums.Sex;
import com.source3g.hermes.message.ShortMessageRecord;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.service.JmsService;
import com.source3g.hermes.utils.DateFormateUtils;
import com.source3g.hermes.utils.Page;
import com.source3g.hermes.utils.PhoneUtils;

@Service
public class MessageService extends BaseService {
	private static final Logger logger=LoggerFactory.getLogger(MessageService.class);

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
		String customerPhoneArray[] = null;
		if (customerPhones != null) {
			customerPhoneArray = customerPhones.split(";");
		}
		Set<Customer> customerSet = findCustomerByGroupIdAndPhones(ids, customerPhoneArray);
		sendMessages(merchantId, customerSet, content);
	}

	private Set<Customer> findCustomerByGroupIdAndPhones(String[] ids, String[] customerPhones) {
		Set<Customer> customersSet = new HashSet<Customer>();
		if (ids != null) {
			List<Customer> customers = (findCustomerByGroupIds(ids));
			customersSet.addAll(customers);
		}
		if (customerPhones != null && customerPhones.length > 0) {
			List<Customer> customers = mongoTemplate.find(new Query(Criteria.where("phone").in(Arrays.asList(customerPhones))), Customer.class);
			customersSet.addAll(customers);
		}
		return customersSet;
	}

	private List<Customer> findCustomerByGroupIds(String[] ids) {
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
				customer.setId((ObjectId) obj.get("_id"));
				customer.setMerchantId((ObjectId)obj.get("merchantId"));
				DBRef dbRef = (DBRef) obj.get("customerGroup");
				customer.setCustomerGroup(new CustomerGroup((ObjectId) dbRef.getId()));
				return customer;
			}

		});
		return customers;
	}

	public MessageSendLog genMessageSendLog(Customer customer, ObjectId merchantId, int sendCount, String content, MessageType type, MessageStatus status) {
		MessageSendLog log = new MessageSendLog();
		log.setId(ObjectId.get());
		log.setContent(content);
		log.setSendTime(new Date());
		log.setCustomerName(customer.getName());
		log.setCustomerGroup(customer.getCustomerGroup());
		log.setMerchantId(merchantId);
		log.setPhone(customer.getPhone());
		log.setSendCount(sendCount);
		log.setType(type);
		log.setStatus(status);
		mongoTemplate.insert(log);
		return log;
	}

	private GroupSendLog genGroupSendLog(Set<Customer> customers, String content, ObjectId merchantId) {
		GroupSendLog groupSendLog = new GroupSendLog();
		groupSendLog.setId(ObjectId.get());
		groupSendLog.setMerchantId(merchantId);
		groupSendLog.setContent(content);
		groupSendLog.setSendCount(customers.size());
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

	public void sendMessage(ObjectId merchantId, String phone, String content) throws Exception {
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("_id").is(merchantId)), Merchant.class);
		if (merchant.getMessageBalance().getSurplusMsgCount() - 1 < 0) {
			throw new Exception("余额不足");
			
		}
		Customer c = mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchantId).and("phone").is(phone)), Customer.class);
		String proceedContent = processContent(merchant, c, content);
		genMessageSendLog(c, merchantId, 1, proceedContent, MessageType.短信发送, MessageStatus.发送中);
		sendMessage(c, proceedContent, MessageType.短信发送,null);
	}

	public void sendMessage(Customer c, String content, MessageType messageType,ObjectId groupLogId) {
		ShortMessageRecord message = new ShortMessageRecord();
		message.setContent(content);
		message.setPhone(c.getPhone());
		message.setMessageType(messageType);
		message.setMerchantId(c.getMerchantId());
		if(groupLogId!=null){
			message.setGroupLogId(groupLogId);
		}
		jmsService.sendObject(messageDestination, message, JmsConstants.TYPE, JmsConstants.SEND_MESSAGE);
	}

	public void sendMessages(ObjectId merchantId, Set<Customer> customersSet, String content) throws Exception {
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("_id").is(merchantId)), Merchant.class);
		if (customersSet.size() > merchant.getMessageBalance().getSurplusMsgCount()) {
			throw new Exception("余额不足");
		}
		// 生成群发记录
		GroupSendLog groupSendLog=genGroupSendLog(customersSet, content, merchant.getId());
		for (Customer c : customersSet) {
			sendMessage(c, processContent(merchant, c, content), MessageType.群发, groupSendLog.getId());
		}
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

	public MessageStatus send(ShortMessageRecord shortMessageRecord) {
		String phoneNumber=shortMessageRecord.getPhone();
		String content=shortMessageRecord.getContent();
		String msgId=shortMessageRecord.getMsgId();
		return send(msgId,phoneNumber,content);
	}
	
	public MessageStatus send(String msgId, String phoneNumber, String content) {
		if (PhoneOperator.移动.equals(PhoneUtils.getOperatior(phoneNumber))) {
			return sendByCm( msgId,phoneNumber, content);
		}
		if (PhoneOperator.联通.equals(PhoneUtils.getOperatior(phoneNumber))) {
			return sendByCu(msgId,phoneNumber, content);
		}
		if (PhoneOperator.电信.equals(PhoneUtils.getOperatior(phoneNumber))) {
			return sendByCt(msgId,phoneNumber, content);
		}
		logger.debug("向" + phoneNumber + "发送消息" + content+"失败，电话号码有误");
		return MessageStatus.电话号码有误;
	}
	private MessageStatus sendByCt(String msgId,String phoneNumber, String content) {
		logger.debug("通过电信向" + phoneNumber + "发送" + content+"msgId:"+msgId);
		sendByCu(msgId,phoneNumber, content);
		return MessageStatus.已发送;
	}

	private MessageStatus sendByCu(String msgId,String phoneNumber, String content) {
		logger.debug("通过联通向" + phoneNumber + "发送" + content+"msgId:"+msgId);
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
		System.out.println(tcp.getSndQueueSize());
		System.out.println(tcp.getUnSend().size());
		for (String str:tcp.getUnSend()){
			System.out.println(str);
		}
		return MessageStatus.已发送;
	}

	private MessageStatus sendByCm(String msgId,String phoneNumber, String content) {
		logger.debug("通过移动向" + phoneNumber + "发送" + content+"msgId:"+msgId);
		sendByCu(msgId,phoneNumber, content);
		return MessageStatus.已发送;
	}

	public void saveMessageAutoSend(AutoSendMessageTemplate messageAutoSend) {
		Update update = new Update();
		update.set("newMessageCotent", messageAutoSend.getNewMessageCotent());
		update.set("oldMessageCotent", messageAutoSend.getOldMessageCotent());
		mongoTemplate.upsert(new Query(Criteria.where("merchantId").is(messageAutoSend.getMerchantId())), update, AutoSendMessageTemplate.class);
	}

	public AutoSendMessageTemplate getMessageAutoSend(ObjectId merchantId) {
		return mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchantId)), AutoSendMessageTemplate.class);
	}

	public MessageStatisticsDto findMessageStastics(ObjectId merchantId) {
		MessageStatisticsDto messageStatisticsDto = new MessageStatisticsDto();
		messageStatisticsDto.setHandUpMessageSentCountAWeek(new StatisticObjectDto("一周挂机短信发送数量：",findMessageSentCountFromToday(merchantId, 7, MessageType.挂机短信)));
		messageStatisticsDto.setMessageGroupSentCountAWeek(new StatisticObjectDto("一周短信群发数量：",findMessageSentCountFromToday(merchantId, 7, MessageType.群发)));
		messageStatisticsDto.setHandUpMessageSentCountThreeDay(new StatisticObjectDto("三天挂机短信群发数量：",findMessageSentCountFromToday(merchantId, 3, MessageType.挂机短信)));
		messageStatisticsDto.setMessageGroupSentCountThreeDay(new StatisticObjectDto("三天短信群发数量：",findMessageSentCountFromToday(merchantId, 3, MessageType.群发)));
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
		List<Customer> customers = findCustomerByMerchantRemindTemplate(title, merchantId, remindTemplate, merchantRemindTemplate);
		Set<Customer> customersSet = new HashSet<Customer>(customers);
		sendMessages(merchantId, customersSet, content);
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

	public void updateMessageSendLog(ShortMessageRecord shortMessageMessage, MessageStatus status) {
		if (shortMessageMessage.getMessageSendLogId() != null) {
			updateLog(shortMessageMessage.getMessageSendLogId(), new Date(), status);
		}
	}

	public void updateShortMessageRecord(String msgId, MessageStatus messageStatus) {
		Update update=new Update();
		update.set("status", messageStatus);
		mongoTemplate.updateFirst(new Query(Criteria.where("msgId").is(msgId)),update, ShortMessageRecord.class);
	}

	public Page failedMessagelist(int pageNoInt) {
		Query query = new Query();
		query.with(new Sort(Direction.DESC, "_id"));
		query.addCriteria(Criteria.where("status").is(MessageStatus.发送失败));
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, ShortMessageRecord.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNoInt);
		List<ShortMessageRecord> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), ShortMessageRecord.class);
		page.setData(list);
		return page;
	}

}
