package com.source3g.hermes.customer.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.source3g.hermes.constants.JmsConstants;
import com.source3g.hermes.entity.Device;
import com.source3g.hermes.entity.customer.CallRecord;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.CustomerGroup;
import com.source3g.hermes.entity.customer.CustomerImportLog;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.enums.ImportStatus;
import com.source3g.hermes.enums.Sex;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.service.JmsService;
import com.source3g.hermes.utils.Page;

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

	public void updateExcludeProperties(Customer customer, String... properties) {
		super.updateExcludeProperties(customer, properties);
	}

	public List<Customer> listAll() {
		return mongoTemplate.findAll(Customer.class);
	}

	public Page list(int pageNo, Customer customer, boolean isNew) {
		Query query = new Query();
		if (customer.getMerchantId() == null) {
			return null;
		} else {
			query.addCriteria(Criteria.where("merchantId").is(customer.getMerchantId()));
		}

		if (isNew == true) {
			query.addCriteria(Criteria.where("name").is(null));
		} else if (StringUtils.isNotEmpty(customer.getName())) {
			Pattern pattern = Pattern.compile("^.*" + customer.getName() + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("name").is(pattern));
		}
		if (StringUtils.isNotEmpty(customer.getPhone())) {
			Pattern pattern = Pattern.compile("^.*" + customer.getPhone() + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("phone").is(pattern));
		}

		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, Customer.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNo);
		List<Customer> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), Customer.class);
		page.setData(list);
		return page;
	}

	public Page listByPage(int pageNo, Customer customer) {
		return list(pageNo, customer, false);
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
		update.set("phone", phone).set("merchantId", merchant.getId()).set("lastCallInTime", callInTime).addToSet("callRecords", record);
		mongoTemplate.upsert(new Query(Criteria.where("merchantId").is(merchant.getId()).and("phone").is(phone)), update, Customer.class);
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
		super.updateIncludeProperties(customer, "name", "sex", "birthday", "phone", "blackList", "address", "otherPhones", "qq", "email", "note", "reminds", "customerGroupId", "operateTime");
	}

	public Customer findBySnAndPhone(String sn, String phone) {
		Device device = mongoTemplate.findOne(new Query(Criteria.where("sn").is(sn)), Device.class);
		if (device == null)
			return null;
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("deviceIds").is(device.getId())), Merchant.class);
		if (merchant == null)
			return null;
		return mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchant.getId()).and("phone").is(phone)), Customer.class);
	}

	public void deleteById(String id) {
		super.deleteById(id, Customer.class);
	}

	public Long findNewCustomerCountByDay(String merchantId,Date startTime, Date endTime) {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("callRecords.callTime").gt(startTime).lt(endTime).and("merchantId").is(new ObjectId(merchantId)));
		
		MapReduceResults<ObjectValue> results= mongoTemplate.mapReduce(query,"customer",  "classpath:mapreduce/callRecordsByDayMap.js","classpath:mapreduce/callRecordsByDayReduce.js",ObjectValue.class);
		
		for (ObjectValue result:results){
			System.out.println("==============="+result.getValue());
		}
		return 0L;
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

}
