package com.source3g.hermes.customer.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.jms.Destination;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.source3g.hermes.constants.JmsConstants;
import com.source3g.hermes.dto.customer.CustomerDto;
import com.source3g.hermes.dto.customer.CustomerRemindDto;
import com.source3g.hermes.dto.customer.CustomerRemindDto.CustomerInfo;
import com.source3g.hermes.dto.customer.CustomerStatisticsDto;
import com.source3g.hermes.dto.message.StatisticObjectDto;
import com.source3g.hermes.entity.ObjectValue;
import com.source3g.hermes.entity.customer.CallRecord;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.CustomerGroup;
import com.source3g.hermes.entity.customer.CustomerImportLog;
import com.source3g.hermes.entity.customer.Remind;
import com.source3g.hermes.entity.device.Device;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.merchant.MerchantRemindTemplate;
import com.source3g.hermes.enums.CallStatus;
import com.source3g.hermes.enums.ImportStatus;
import com.source3g.hermes.enums.Sex;
import com.source3g.hermes.enums.TypeEnum.CustomerType;
import com.source3g.hermes.message.CallInMessage;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.service.JmsService;
import com.source3g.hermes.utils.FormateUtils;
import com.source3g.hermes.utils.EntityUtils;
import com.source3g.hermes.utils.Page;
import com.source3g.hermes.utils.PhoneUtils;
import com.source3g.hermes.vo.CallInStatisticsCount;

@Service
public class CustomerService extends BaseService {

	@Value(value = "${temp.import.log.dir}")
	private String tempDir;
	@Value(value = "${customer.export.temp.dir}")
	private String exportDir;
	@Value(value = "${local.url}")
	private String localUrl;

	@Autowired
	private Destination customerDestination;

	@Autowired
	private JmsService jmsService;

	public void add(Customer customer) throws Exception {
		customer.setId(ObjectId.get());
		customer.setOperateTime(new Date());
		if (customer.getCustomerGroup() == null) {
			throw new Exception("顾客组不能为空");
		}
		mongoTemplate.insert(customer);
	}

	public Boolean phoneValidate(String phone, ObjectId merchantId) {
		List<Customer> customer = mongoTemplate.find(new Query(Criteria.where("phone").is(phone).and("merchantId").is(merchantId)), Customer.class);
		if (customer.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public void updateExcludeProperties(Customer customer, String... properties) {
		super.updateExcludeProperties(customer, properties);
	}

	// 短信群发页面显示顾客信息
	public Object customerListBycustomerGroupId(ObjectId customerGroupId) throws Exception {
		List<ObjectId> customerGroupIds = new ArrayList<ObjectId>();
		customerGroupIds.add(customerGroupId);
		BasicDBObject parameter = new BasicDBObject();
		parameter.put("customerGroup.$id", new BasicDBObject("$in", customerGroupIds));
		long count = mongoTemplate.count(new Query(Criteria.where("customerGroup.$id").in(customerGroupIds)), Customer.class);
		if (count > 5000) {
			throw new Exception("客户组数据过多,最多显示5000位顾客信息");
		}
		List<Customer> customers = findByBasicDBObject(Customer.class, parameter, new ObjectMapper<Customer>() {
			@Override
			public Customer mapping(DBObject obj) {
				Customer customer = new Customer();
				customer.setName((String) obj.get("name"));
				customer.setPhone((String) obj.get("phone"));
				customer.setId((ObjectId) obj.get("_id"));
				customer.setMerchantId((ObjectId) obj.get("merchantId"));
				DBRef dbRef = (DBRef) obj.get("customerGroup");
				customer.setCustomerGroup(new CustomerGroup((ObjectId) dbRef.getId()));
				return customer;
			}
		});
		return customers;
	}

	/**
	 * 顾客列表
	 * 
	 * @param pageNo
	 * @param customer
	 * @param customerType
	 *            顾客类型，分为新顾客，老顾客，全部顾客
	 * @param direction
	 *            排序规则
	 * @param properties
	 *            排序属性
	 * @return
	 */
	public Page list(int pageNo, Customer customer, CustomerType customerType, Direction direction, String... properties) {
		Query query = new Query();
		Criteria criteria = null;
		if (customer.getMerchantId() == null) {
			return null;
		} else {
			criteria = Criteria.where("merchantId").is(customer.getMerchantId());
		}

		switch (customerType) {
		case newCustomer:
			criteria.and("name").is(null);
			break;
		case oldCustomer:
			if (StringUtils.isNotEmpty(customer.getName())) {
				Pattern pattern = Pattern.compile("^.*" + customer.getName() + ".*$", Pattern.CASE_INSENSITIVE);
				criteria.and("name").is(pattern);
			} else {
				criteria.and("name").ne(null);
			}
			if (StringUtils.isNotEmpty(customer.getPhone())) {
				Pattern pattern = Pattern.compile("^.*" + customer.getPhone() + ".*$", Pattern.CASE_INSENSITIVE);
				criteria.and("phone").is(pattern);
			}
			if (StringUtils.isNotEmpty(customer.getCustomerGroup().getName())) {
				Pattern groupPattern = Pattern.compile("^.*" + customer.getCustomerGroup().getName() + ".*$", Pattern.CASE_INSENSITIVE);
				List<CustomerGroup> customerGroups = mongoTemplate.find(new Query(Criteria.where("name").is(groupPattern).and("merchantId").is(customer.getMerchantId())), CustomerGroup.class);
				if (customerGroups != null) {
					criteria.and("customerGroup").in(customerGroups);
				}
			}
			break;
		case allCustomer:
			if (StringUtils.isNotEmpty(customer.getName())) {
				Pattern pattern = Pattern.compile("^.*" + customer.getName() + ".*$", Pattern.CASE_INSENSITIVE);
				criteria.and("name").is(pattern);
			}
			if (StringUtils.isNotEmpty(customer.getPhone())) {
				Pattern pattern = Pattern.compile("^.*" + customer.getPhone() + ".*$", Pattern.CASE_INSENSITIVE);
				criteria.and("phone").is(pattern);
			}
			if (StringUtils.isNotEmpty(customer.getCustomerGroup().getName())) {
				Pattern groupPattern = Pattern.compile("^.*" + customer.getCustomerGroup().getName() + ".*$", Pattern.CASE_INSENSITIVE);
				List<CustomerGroup> customerGroups = mongoTemplate.find(new Query(Criteria.where("name").is(groupPattern).and("merchantId").is(customer.getMerchantId())), CustomerGroup.class);
				if (customerGroups != null) {
					criteria.and("customerGroup").in(customerGroups);
				}
			}
			break;
		}
		query.addCriteria(criteria);
		query.with(new Sort(direction, properties));
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, Customer.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNo);
		List<Customer> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), Customer.class);
		page.setData(list);
		return page;
	}

	/**
	 * 来电顾客列表
	 * 
	 * @param pageNo
	 * @param customer
	 * @param customerType
	 *            顾客类型，分为新顾客，老顾客，全部顾客
	 * @param direction
	 *            排序规则 排序属性
	 * @param properties
	 * @return
	 */
	public Page callInList(int pageNo, Customer customer, CustomerType customerType) {
		Query query = new Query();
		Criteria criteria = null;
		if (customer.getMerchantId() == null) {
			return null;
		} else {
			criteria = Criteria.where("merchantId").is(customer.getMerchantId());
		}

		switch (customerType) {
		case newCustomer:
			criteria.and("name").is(null);
			break;
		case oldCustomer:
			if (StringUtils.isNotEmpty(customer.getName())) {
				Pattern pattern = Pattern.compile("^.*" + customer.getName() + ".*$", Pattern.CASE_INSENSITIVE);
				criteria.and("name").is(pattern);
			} else {
				criteria.and("name").ne(null);
			}
			if (StringUtils.isNotEmpty(customer.getPhone())) {
				Pattern pattern = Pattern.compile("^.*" + customer.getPhone() + ".*$", Pattern.CASE_INSENSITIVE);
				criteria.and("phone").is(pattern);
			}
			break;
		case allCustomer:
			if (StringUtils.isNotEmpty(customer.getName())) {
				Pattern pattern = Pattern.compile("^.*" + customer.getName() + ".*$", Pattern.CASE_INSENSITIVE);
				criteria.and("name").is(pattern);
			}
			if (StringUtils.isNotEmpty(customer.getPhone())) {
				Pattern pattern = Pattern.compile("^.*" + customer.getPhone() + ".*$", Pattern.CASE_INSENSITIVE);
				criteria.and("phone").is(pattern);
			}
			break;
		}
		criteria.and("lastCallInTime").ne(null);
		// criteria.and("lastCallInTime").gt(DateFormateUtils.getStartDateOfDay(new
		// Date()));
		query.addCriteria(criteria);
		query.with(new Sort(Direction.DESC, "lastCallInTime"));
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, Customer.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNo);
		List<Customer> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), Customer.class);
		page.setData(list);
		return page;
	}

	/**
	 * 顾客列表
	 * 
	 * @param pageNo
	 * @param customer
	 * @param isNew
	 *            是否为新顾客
	 * @return
	 */
	public Page list(int pageNo, Customer customer, CustomerType customerType) {
		return list(pageNo, customer, customerType, Direction.DESC, "_id");
	}

	public Page listByPage(int pageNo, Customer customer, Direction direction, String property) {
		return list(pageNo, customer, CustomerType.allCustomer, direction, property);
	}

	public Page ascendingList(int pageNo, Customer customer, boolean isNew) {
		return list(pageNo, customer, CustomerType.allCustomer, Direction.ASC, "name");
	}

	public Page descendingList(int pageNo, Customer customer, boolean isNew) {
		return list(pageNo, customer, CustomerType.allCustomer, Direction.DESC, "name");
	}

	public List<Customer> list(Customer customer, CustomerType customerType) {
		Query query = new Query();
		if (customer.getMerchantId() == null) {
			return null;
		} else {
			query.addCriteria(Criteria.where("merchantId").is(customer.getMerchantId()));
		}
		switch (customerType) {
		case newCustomer:
			query.addCriteria(Criteria.where("name").is(null));
			break;
		case oldCustomer:
			if (StringUtils.isNotEmpty(customer.getName())) {
				Pattern pattern = Pattern.compile("^.*" + customer.getName() + ".*$", Pattern.CASE_INSENSITIVE);
				query.addCriteria(Criteria.where("name").is(pattern));
			}
			if (StringUtils.isNotEmpty(customer.getPhone())) {
				Pattern pattern = Pattern.compile("^.*" + customer.getPhone() + ".*$", Pattern.CASE_INSENSITIVE);
				query.addCriteria(Criteria.where("phone").is(pattern));
			}
			if (StringUtils.isNotEmpty(customer.getCustomerGroup().getName())) {
				Pattern groupPattern = Pattern.compile("^.*" + customer.getCustomerGroup().getName() + ".*$", Pattern.CASE_INSENSITIVE);
				List<CustomerGroup> customerGroups = mongoTemplate.find(new Query(Criteria.where("name").is(groupPattern).and("merchantId").is(customer.getMerchantId())), CustomerGroup.class);
				if (customerGroups != null) {
					query.addCriteria(Criteria.where("customerGroup").in(customerGroups));
				}
			}
			break;
		case allCustomer:
			if (StringUtils.isNotEmpty(customer.getName())) {
				Pattern pattern = Pattern.compile("^.*" + customer.getName() + ".*$", Pattern.CASE_INSENSITIVE);
				query.addCriteria(Criteria.where("name").is(pattern));
			}
			if (StringUtils.isNotEmpty(customer.getPhone())) {
				Pattern pattern = Pattern.compile("^.*" + customer.getPhone() + ".*$", Pattern.CASE_INSENSITIVE);
				query.addCriteria(Criteria.where("phone").is(pattern));
			}
			if (StringUtils.isNotEmpty(customer.getCustomerGroup().getName())) {
				Pattern groupPattern = Pattern.compile("^.*" + customer.getCustomerGroup().getName() + ".*$", Pattern.CASE_INSENSITIVE);
				List<CustomerGroup> customerGroups = mongoTemplate.find(new Query(Criteria.where("name").is(groupPattern).and("merchantId").is(customer.getMerchantId())), CustomerGroup.class);
				if (customerGroups != null) {
					query.addCriteria(Criteria.where("customerGroup").in(customerGroups));
				}
			}
			break;
		}
		List<Customer> list = mongoTemplate.find(query, Customer.class);
		return list;
	}

	public String export(Customer customer, CustomerType customerType) throws IOException, NoSuchMethodException, SecurityException, NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<Customer> list = list(customer, customerType);
		Date createTime = new Date();
		// 文件名
		DateFormat dateFormatExport = new SimpleDateFormat("yyyy-MM-dd");
		String fileName = String.valueOf(dateFormatExport.format(createTime) + "顾客导出列表") + ".xls";
		// 产生文件路径
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		// 所在商户的相对路径
		String merchantPath = dateFormat.format(createTime) + "/" + customer.getMerchantId().toString() + "/";
		String absoluteDir = exportDir + merchantPath;
		String absoluteFile = absoluteDir + fileName;
		String relativePath = merchantPath + fileName;
		File absoluteFolder = new File(absoluteDir);
		absoluteFolder.mkdirs();
		String headers[] = { "姓名", "性别", "生日", "电话", "地址", "qq", "email", "备注", "顾客组名" };
		Map<String, String> headerFieldMap = new HashMap<String, String>();
		headerFieldMap.put("姓名", "name");
		headerFieldMap.put("性别", "sex");
		headerFieldMap.put("生日", "birthday");
		headerFieldMap.put("电话", "phone");
		headerFieldMap.put("地址", "address");
		headerFieldMap.put("qq", "qq");
		headerFieldMap.put("email", "email");
		headerFieldMap.put("备注", "note");
		headerFieldMap.put("顾客组名", "customerGroupName");

		File file = new File(absoluteFile);
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet("work1");
		// 产生表格标题行

		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
			if ("电话".equals(headers[i]) || "qq".equals(headers[i])) {
				sheet.setColumnWidth(i, 256 * 15);
			}
		}

		int index = 0;
		for (Customer c : list) {
			index++;
			row = sheet.createRow(index);
			for (int i = 0; i < headers.length; i++) {
				try {
					String fieldName = headerFieldMap.get(headers[i]);
					Object value = null;
					if ("customerGroupName".equals(fieldName)) {
						HSSFCell cell = row.createCell(i);
						if (c.getCustomerGroup() != null) {
							HSSFRichTextString richString = new HSSFRichTextString(c.getCustomerGroup().getName());
							cell.setCellValue(richString);
						} else {
							cell.setCellValue("");
						}
					} else {
						String firstLetter = fieldName.substring(0, 1).toUpperCase();
						Field field = c.getClass().getDeclaredField(fieldName);
						// 获得和属性对应的getXXX()方法的名字
						String getMethodName;
						if (field.getType() == boolean.class) {
							getMethodName = "is" + firstLetter + fieldName.substring(1);
						} else {
							getMethodName = "get" + firstLetter + fieldName.substring(1);
						}
						Method getMethod = c.getClass().getMethod(getMethodName, new Class[] {});
						if ("sex".equals(fieldName)) {
							if (Sex.FEMALE.equals(value)) {
								value = "女";
							} else {
								value = "男";
							}
							HSSFCell cell = row.createCell(i);
							if (value != null) {
								HSSFRichTextString richString = new HSSFRichTextString(value.toString());
								cell.setCellValue(richString);
							}
						} else if ("phone".equals(fieldName) || "qq".equals(fieldName)) {
							value = getMethod.invoke(c, new Object[] {});
							HSSFCell cell = row.createCell(i);
							if (value != null) {
								double val = Double.parseDouble(value.toString());
								cell.setCellValue(val);
							}
						} else {
							value = getMethod.invoke(c, new Object[] {});
							HSSFCell cell = row.createCell(i);
							if (value != null) {
								HSSFRichTextString richString = new HSSFRichTextString(value.toString());
								cell.setCellValue(richString);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		FileOutputStream fos = new FileOutputStream(file);
		workbook.write(fos);
		fos.close();
		return relativePath;
	}

	public Customer get(String id) {
		return mongoTemplate.findById(new ObjectId(id), Customer.class);
	}

	public void callIn(String deviceSn, String phone, String time, Integer duration, Integer callStatus) throws Exception {// ,CallStatus
		Device device = mongoTemplate.findOne(new Query(Criteria.where("sn").is(deviceSn)), Device.class);
		if (device == null) {
			throw new Exception("盒子编号不存在");
		}
		Query findMerchantByDeviceSn = new Query();
		findMerchantByDeviceSn.addCriteria(Criteria.where("deviceIds").is(device.getId()));
		Merchant merchant = mongoTemplate.findOne(findMerchantByDeviceSn, Merchant.class);
		if (merchant == null) {
			throw new Exception("盒子所属商户不存在");
		}
		CallRecord record = new CallRecord();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date callInTime = null;
		try {
			callInTime = dateFormat.parse(time);
		} catch (Exception e) {
			throw new Exception("接听时间格式不正确");
		}
		record.setCallTime(callInTime);
		record.setCallDuration(duration);
		record.setCallStatus(callStatus);
		Update update = new Update();

		Customer c = mongoTemplate.findOne(new Query(Criteria.where("phone").is(phone).and("merchantId").is(merchant.getId())), Customer.class);
		if (c == null || StringUtils.isEmpty(c.getName())) {
			record.setNewCustomer(true);
		}
		update.set("phone", phone).set("merchantId", merchant.getId()).set("lastCallInTime", callInTime).set("operateTime", new Date()).addToSet("callRecords", record);
		mongoTemplate.upsert(new Query(Criteria.where("merchantId").is(merchant.getId()).and("phone").is(phone)), update, Customer.class);
		if (PhoneUtils.isMobile(phone)) {
			if (merchant.getSetting().isAutoSend() == true && merchant.getMessageBalance().getSurplusMsgCount() > 0 && record.getCallDuration() >= 6 && CallStatus.CALL_IN.equals(record.getCallStatus())) {
				CallInMessage callInMessage = new CallInMessage();
				callInMessage.setDeviceSn(deviceSn);
				callInMessage.setDuration(duration);
				callInMessage.setMerchantId(merchant.getId());
				callInMessage.setPhone(phone);
				callInMessage.setTime(time);
				jmsService.sendObject(customerDestination, callInMessage, JmsConstants.TYPE, JmsConstants.CALL_IN);
			}
		}
	}

	public String getTempDir() {
		return tempDir;
	}

	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}

	public void addImportLog(CustomerImportLog importLog) throws Exception {
		final CustomerImportLog importLogFinal = importLog;
		try {
			jmsService.sendObject(customerDestination, importLogFinal, JmsConstants.TYPE, JmsConstants.IMPORT_CUSTOMER);
		} catch (Exception e) {
			importLog.setStatus(ImportStatus.导入失败.toString());
			throw new Exception("日志接收失败");
		}
		mongoTemplate.insert(importLog);
	}

	public void updateInfo(Customer customer, String merchantId) {
		customer.setOperateTime(new Date());
		if (customer.getId() == null) {
			Customer c = mongoTemplate.findOne(new Query(Criteria.where("phone").is(customer.getPhone()).and("merchantId").is(new ObjectId(merchantId))), Customer.class);
			if (c != null) {
				customer.setId(c.getId());
			}
		}
		super.updateIncludeProperties(customer, "name", "sex", "birthday", "phone", "blackList", "address", "otherPhones", "qq", "email", "note", "reminds", "customerGroup", "favorite", "operateTime");
	}

	public CustomerDto findBySnAndPhone(String sn, String phone) {
		Device device = mongoTemplate.findOne(new Query(Criteria.where("sn").is(sn)), Device.class);
		if (device == null)
			return null;
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("deviceIds").is(device.getId())), Merchant.class);
		if (merchant == null)
			return null;
		Customer customer = mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchant.getId()).and("phone").is(phone)), Customer.class);
		CustomerDto customerDto = new CustomerDto();
		EntityUtils.copyCustomerEntityToDto(customer, customerDto);
		return customerDto;
	}

	public void deleteById(String id) {
		super.deleteById(id, Customer.class);
	}

	/**
	 * 以天为单位统计来电次数
	 * 
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @param type
	 *            0为全部,1为新顾客,2为老顾客
	 * @return
	 */
	public List<ObjectValue> findCallInCountByDay(String merchantId, Date startTime, Date endTime, int type) {
		Query query = new Query();
		Criteria criteria = Criteria.where("callRecords.callTime").gt(startTime).lt(endTime).and("merchantId").is(new ObjectId(merchantId));
		String mapResource = "classpath:mapreduce/allCallRecordsByDayMap.js";
		if (type == 1) {
			criteria.and("callRecords.newCustomer").is(true);
			mapResource = "classpath:mapreduce/newCallRecordsByDayMap.js";
		} else if (type == 2) {
			criteria.and("callRecords.newCustomer").is(false);
			mapResource = "classpath:mapreduce/oldCallRecordsByDayMap.js";
		}
		query.addCriteria(criteria);
		MapReduceResults<ObjectValue> results = mongoTemplate.mapReduce(query, "customer", mapResource, "classpath:mapreduce/callRecordsByDayReduce.js", ObjectValue.class);
		List<ObjectValue> values = new ArrayList<ObjectValue>();
		List<String> daysHasValue = new ArrayList<String>();
		List<String> allDays = FormateUtils.getDays(startTime, endTime);
		for (String day : allDays) {
			boolean hasDay = false;
			for (ObjectValue value : results) {
				if (day.equals(value.getId())) {
					daysHasValue.add(value.getId());
					values.add(value);
					hasDay = true;
				}
			}
			if (!hasDay) {
				ObjectValue v = new ObjectValue();
				v.setId(day);
				LinkedHashMap<Object, Object> valueMap = new LinkedHashMap<Object, Object>();
				valueMap.put("count", 0.0);
				v.setValue(valueMap);
				values.add(v);
			}
		}

		return values;
	}

	public String getExportDir() {
		return exportDir;
	}

	public void setExportDir(String exportDir) {
		this.exportDir = exportDir;
	}

	public String getLocalUrl() {
		return localUrl;
	}

	public void setLocalUrl(String localUrl) {
		this.localUrl = localUrl;
	}

	public List<Customer> findByCallRecords(String deviceSn, Date startTime, Date endTime) {
		Device device = mongoTemplate.findOne(new Query(Criteria.where("sn").is(deviceSn)), Device.class);
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("deviceIds").is(device.getId())), Merchant.class);
		return findByCallRecords(merchant.getId(), startTime, endTime);
	}

	public List<Customer> findByCallRecords(ObjectId merchantId, Date startTime, Date endTime) {
		Query query = new Query();
		query.addCriteria(Criteria.where("merchantId").is(merchantId).and("callRecords.callTime").gt(startTime).lt(endTime));
		return mongoTemplate.find(query, Customer.class);
	}

	public long findNewCustomerCount(String sn, Date startTime) {
		Device device = mongoTemplate.findOne(new Query(Criteria.where("sn").is(sn)), Device.class);
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("deviceIds").is(device.getId())), Merchant.class);
		long count = mongoTemplate.count(new Query(Criteria.where("name").is(null).and("merchantId").is(merchant.getId()).and("lastCallInTime").gte(startTime)), Customer.class);
		return count;
	}

	public List<Customer> findNewCustomers(String sn, Date startTime) {
		Device device = mongoTemplate.findOne(new Query(Criteria.where("sn").is(sn)), Device.class);
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("deviceIds").is(device.getId())), Merchant.class);
		return mongoTemplate.find(new Query(Criteria.where("name").is(null).and("merchantId").is(merchant.getId()).and("lastCallInTime").gte(startTime)), Customer.class);
	}

	public CallInStatisticsCount findCallInStatisticsByCount(ObjectId merchantId, Date startTime, Date endTime) {
		Query query = new Query();
		Date date = new Date();
		if (startTime == null) {
			startTime = FormateUtils.getStartDateOfDay(date);
		}
		if (endTime == null) {
			endTime = date;
		}
		query.addCriteria(Criteria.where("merchantId").is(merchantId).and("callRecords.callTime").gt(startTime).lt(endTime));
		List<Customer> customers = mongoTemplate.find(query, Customer.class);
		int newCount = 0;
		int oldCount = 0;
		for (Customer c : customers) {
			if (c.getCallRecords() == null) {
				continue;
			}
			for (CallRecord r : c.getCallRecords()) {
				if (r.getCallTime().getTime() > startTime.getTime() && r.getCallTime().getTime() < endTime.getTime()) {
					if (r.isNewCustomer()) {
						newCount++;
					} else {
						oldCount++;
					}
				}
			}
		}
		CallInStatisticsCount callInStatisticsToday = new CallInStatisticsCount(newCount, oldCount);
		return callInStatisticsToday;
	}

	public List<Customer> findCustomersByBirthday(ObjectId merchantId, String birthday) {
		Query query = new Query();
		query.addCriteria(Criteria.where("birthday").is(birthday).and("merchantId").is(merchantId));
		return mongoTemplate.find(query, Customer.class);
	}

	public List<Map<String, Object>> findCustomerStatistics(ObjectId merchantId) {
		CustomerStatisticsDto customerStatisticsDto = new CustomerStatisticsDto();
		customerStatisticsDto.setEditedCustomerCount(new StatisticObjectDto("已编辑顾客数量：", findAllCustomerCount(merchantId, CustomerType.oldCustomer)));
		customerStatisticsDto.setUneditedCustomerCount(new StatisticObjectDto("未编辑顾客数量：", findAllCustomerCount(merchantId, CustomerType.newCustomer)));
		CallInStatisticsCount callInStatisticsCountThreeDay = findCallInCountByDayFromToday(merchantId, 3);
		customerStatisticsDto.setUneditedCallInCountThreeDay(new StatisticObjectDto("三天内未编辑顾客打进电话数：", callInStatisticsCountThreeDay.getNewCount()));
		customerStatisticsDto.setEditedCallInCountThreeDay(new StatisticObjectDto("三天内已编辑顾客打进电话数：", callInStatisticsCountThreeDay.getOldCount()));
		CallInStatisticsCount callInStatisticsCountAWeek = findCallInCountByDayFromToday(merchantId, 7);
		customerStatisticsDto.setUneditedCallInCountAWeek(new StatisticObjectDto("一周内未编辑顾客打进电话数：", callInStatisticsCountAWeek.getNewCount()));
		customerStatisticsDto.setEditedCallInCountAWeek(new StatisticObjectDto("一周内已编辑顾客打进电话数：", callInStatisticsCountAWeek.getOldCount()));
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> editedCustomerCount = new HashMap<String, Object>();
		Map<String, Object> uneditedCustomerCount = new HashMap<String, Object>();
		Map<String, Object> editedCallInCountThreeDay = new HashMap<String, Object>();
		Map<String, Object> uneditedCallInCountThreeDay = new HashMap<String, Object>();
		Map<String, Object> uneditedCallInCountAWeek = new HashMap<String, Object>();
		Map<String, Object> editedCallInCountAWeek = new HashMap<String, Object>();

		editedCustomerCount.put("editedCustomerCount", customerStatisticsDto.getEditedCustomerCount());
		uneditedCustomerCount.put("uneditedCustomerCount", customerStatisticsDto.getUneditedCustomerCount());
		editedCallInCountThreeDay.put("editedCallInCountThreeDay", customerStatisticsDto.getEditedCallInCountThreeDay());
		uneditedCallInCountThreeDay.put("uneditedCallInCountThreeDay", customerStatisticsDto.getUneditedCallInCountThreeDay());
		editedCallInCountAWeek.put("editedCallInCountAWeek", customerStatisticsDto.getEditedCallInCountAWeek());
		uneditedCallInCountAWeek.put("uneditedCallInCountAWeek", customerStatisticsDto.getUneditedCallInCountAWeek());

		list.add(editedCustomerCount);
		list.add(uneditedCustomerCount);
		list.add(editedCallInCountThreeDay);
		list.add(uneditedCallInCountThreeDay);
		list.add(editedCallInCountAWeek);
		list.add(uneditedCallInCountAWeek);
		return list;
	}

	/**
	 * 查询最近几天的来电次数
	 * 
	 * @param merchantId
	 *            商户ID
	 * @param dayCount
	 *            天数
	 * @param customerType
	 *            顾客类型
	 * @return
	 */
	public CallInStatisticsCount findCallInCountByDayFromToday(ObjectId merchantId, int dayCount) {
		Date endTime = new Date();
		Date startTime = DateUtils.addDays(endTime, 0 - dayCount);
		// 获取开始时间的0点0分0秒
		startTime = FormateUtils.getStartDateOfDay(startTime);
		return findCallInStatisticsByCount(merchantId, startTime, endTime);
	}

	public long findAllCustomerCount(ObjectId merchantId, CustomerType customerType) {
		Query query = new Query();
		Criteria criteria = Criteria.where("merchantId").is(merchantId);
		switch (customerType) {
		case newCustomer:
			criteria.and("name").is(null);
			break;
		case oldCustomer:
			criteria.and("name").ne(null);
			break;
		case allCustomer:
			break;
		}
		query.addCriteria(criteria);
		return mongoTemplate.count(query, Customer.class);
	}

	public List<CustomerRemindDto> findTodayReminds(ObjectId merchantId) {
		List<CustomerRemindDto> result = new ArrayList<CustomerRemindDto>();
		List<MerchantRemindTemplate> merchantRemindTemplates = mongoTemplate.find(new Query(Criteria.where("merchantId").is(merchantId)), MerchantRemindTemplate.class);
		Date startTime = new Date();
		for (MerchantRemindTemplate merchantRemindTemplate : merchantRemindTemplates) {
			Query query = new Query();
			Criteria criteria = Criteria.where("merchantId").is(merchantId);
			Date endTime = FormateUtils.calEndTime(startTime, merchantRemindTemplate.getAdvancedTime());
			criteria.and("reminds").elemMatch(Criteria.where("remindTime").gte(startTime).lte(endTime).and("merchantRemindTemplate.$id").is(merchantRemindTemplate.getId()).and("alreadyRemind").is(false));
			query.addCriteria(criteria);
			List<Customer> customers = mongoTemplate.find(query, Customer.class);
			if (CollectionUtils.isEmpty(customers)) {
				continue;
			}
			CustomerRemindDto customerRemindDto = new CustomerRemindDto();
			customerRemindDto.setAdvancedTime(merchantRemindTemplate.getAdvancedTime());
			customerRemindDto.setContent(merchantRemindTemplate.getMessageContent());
			customerRemindDto.setTitle(merchantRemindTemplate.getRemindTemplate().getTitle());
			for (Customer customer : customers) {
				for (Remind remind : customer.getReminds()) {
					if (remind.getMerchantRemindTemplate().getId().equals(merchantRemindTemplate.getId()) && remind.getRemindTime().getTime() > startTime.getTime() && remind.getRemindTime().getTime() < endTime.getTime() && remind.isAlreadyRemind() == false) {
						CustomerInfo customerInfo = new CustomerInfo(customer.getName(), customer.getPhone(), remind.getRemindTime());
						customerRemindDto.addRemind(customerInfo);
					}
				}
			}
			result.add(customerRemindDto);
		}
		return result;
	}

	public void saveBySn(CustomerDto customerDto, ObjectId merchantId) {
		Customer c = mongoTemplate.findOne(new Query(Criteria.where("phone").is(customerDto.getPhone()).and("merchantId").is(merchantId)), Customer.class);
		if (c == null) {
			c = new Customer();
			c.setOperateTime(new Date());
			c.setMerchantId(merchantId);
			EntityUtils.copyCustomerDtoToEntity(customerDto, c, merchantId);
			mongoTemplate.save(c);
		} else {
			c.setOperateTime(new Date());
			c.setMerchantId(merchantId);
			EntityUtils.copyCustomerDtoToEntity(customerDto, c, merchantId);
			super.updateIncludeProperties(c, "name", "sex", "birthday", "phone", "blackList", "address", "otherPhones", "qq", "email", "note", "customerGroup", "favorite", "operateTime");
		}
	}

}
