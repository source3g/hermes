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

import org.apache.commons.lang.StringUtils;
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

import com.source3g.hermes.constants.JmsConstants;
import com.source3g.hermes.customer.dto.CustomerDto;
import com.source3g.hermes.entity.Device;
import com.source3g.hermes.entity.ObjectValue;
import com.source3g.hermes.entity.customer.CallRecord;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.CustomerGroup;
import com.source3g.hermes.entity.customer.CustomerImportLog;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.enums.ImportStatus;
import com.source3g.hermes.enums.Sex;
import com.source3g.hermes.enums.TypeEnum.CustomerType;
import com.source3g.hermes.message.CallInMessage;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.service.JmsService;
import com.source3g.hermes.utils.DateFormateUtils;
import com.source3g.hermes.utils.EntityUtils;
import com.source3g.hermes.utils.Page;
import com.source3g.hermes.vo.CallInStatisticsToday;

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

	public Customer add(Customer customer) {
		customer.setId(ObjectId.get());
		customer.setOperateTime(new Date());
		mongoTemplate.insert(customer);
		return customer;
	}


	public Boolean phoneValidate(String phone) {
		List<Customer> customer=mongoTemplate.find(new Query(Criteria.where("phone").is(phone)), Customer.class);
		if(customer.size()==0){
			return true;
		}else{
		return false;
		}
	}
	
	public void updateExcludeProperties(Customer customer, String... properties) {
		super.updateExcludeProperties(customer, properties);
	}

	// public List<Customer> listAll() {
	// return mongoTemplate.findAll(Customer.class);
	// }

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
	private Page list(int pageNo, Customer customer, CustomerType customerType, Direction direction, String... properties) {
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
		case allCustomer:
			if (StringUtils.isNotEmpty(customer.getName())) {
				Pattern pattern = Pattern.compile("^.*" + customer.getName() + ".*$", Pattern.CASE_INSENSITIVE);
				criteria.and("name").is(pattern);
			}
			if (StringUtils.isNotEmpty(customer.getPhone())) {
				Pattern pattern = Pattern.compile("^.*" + customer.getPhone() + ".*$", Pattern.CASE_INSENSITIVE);
				criteria.and("phone").is(pattern);
			}
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

	public List<Customer> list(Customer customer) {
		Query query = new Query();
		if (customer.getMerchantId() == null) {
			return null;
		} else {
			query.addCriteria(Criteria.where("merchantId").is(customer.getMerchantId()));
		}
		if (StringUtils.isNotEmpty(customer.getName())) {
			Pattern pattern = Pattern.compile("^.*" + customer.getName() + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("name").is(pattern));
		}
		if (StringUtils.isNotEmpty(customer.getPhone())) {
			Pattern pattern = Pattern.compile("^.*" + customer.getPhone() + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("phone").is(pattern));
		}
		List<Customer> list = mongoTemplate.find(query, Customer.class);
		return list;
	}

	public String export(Customer customer) throws IOException, NoSuchMethodException, SecurityException, NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<Customer> list = list(customer);
		Date createTime = new Date();
		// 文件名
		String fileName = String.valueOf(createTime.getTime()) + ".xls";
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
				String fieldName = headerFieldMap.get(headers[i]);
				Object value = null;
				if ("customerGroupName".equals(fieldName)) {
					CustomerGroup group = mongoTemplate.findOne(new Query(Criteria.where("_id").is(c.getCustomerGroupId())), CustomerGroup.class);
					if (group != null) {
						value = group.getName();
					}
					HSSFCell cell = row.createCell(i);
					if (value != null) {
						HSSFRichTextString richString = new HSSFRichTextString(value.toString());
						cell.setCellValue(richString);
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

	public void callIn(String deviceSn, String phone, String time, String duration) throws Exception {
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
		record.setCallDuration(Integer.parseInt(duration));
		Update update = new Update();

		Customer c = mongoTemplate.findOne(new Query(Criteria.where("phone").is(phone).and("merchantId").is(merchant.getId())), Customer.class);
		if (c == null || StringUtils.isEmpty(c.getName())) {
			record.setNewCustomer(true);
		}
		update.set("phone", phone).set("merchantId", merchant.getId()).set("lastCallInTime", callInTime).addToSet("callRecords", record);
		mongoTemplate.upsert(new Query(Criteria.where("merchantId").is(merchant.getId()).and("phone").is(phone)), update, Customer.class);

		if (merchant.getSetting().isAutoSend() == false) {
			return;
		}
		if (merchant.getShortMessage().getSurplusMsgCount() > 0) {
			CallInMessage callInMessage = new CallInMessage();
			callInMessage.setDeviceSn(deviceSn);
			callInMessage.setDuration(duration);
			callInMessage.setMerchantId(merchant.getId());
			callInMessage.setPhone(phone);
			callInMessage.setTime(time);
			jmsService.sendObject(customerDestination, callInMessage, JmsConstants.TYPE, JmsConstants.CALL_IN);
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

	public void updateInfo(Customer customer) {
		customer.setOperateTime(new Date());
		if (customer.getId() == null) {
			Customer c = mongoTemplate.findOne(new Query(Criteria.where("phone").is(customer.getPhone())), Customer.class);
			if (c != null) {
				customer.setId(c.getId());
			}
		}
		super.updateIncludeProperties(customer, "name", "sex", "birthday", "phone", "blackList", "address", "otherPhones", "qq", "email", "note", "reminds", "customerGroupId", "operateTime");
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
		List<String> allDays = DateFormateUtils.getDays(startTime, endTime);
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

	public CallInStatisticsToday findCallInStatisticsToday(String id, Date startTime, Date endTime) {
		Query query = new Query();
		Date date = new Date();
		Date startDate = DateFormateUtils.getStartDateOfDay(date);
		query.addCriteria(Criteria.where("merchantId").is(new ObjectId(id)).and("callRecords.callTime").gt(startDate).lt(date));
		List<Customer> customers = mongoTemplate.find(query, Customer.class);

		int newCount = 0;
		int oldCount = 0;
		for (Customer c : customers) {
			if (c.getCallRecords() == null) {
				continue;
			}
			for (CallRecord r : c.getCallRecords()) {
				if (r.getCallTime().getTime() > startDate.getTime() && r.getCallTime().getTime() < date.getTime()) {
					if (r.isNewCustomer()) {
						newCount++;
					} else {
						oldCount++;
					}
				}
			}
		}
		CallInStatisticsToday callInStatisticsToday = new CallInStatisticsToday(newCount, oldCount);
		return callInStatisticsToday;
	}
}
