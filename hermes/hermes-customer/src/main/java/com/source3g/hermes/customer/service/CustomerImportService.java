package com.source3g.hermes.customer.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.CustomerGroup;
import com.source3g.hermes.entity.customer.CustomerImportItem;
import com.source3g.hermes.entity.customer.CustomerImportLog;
import com.source3g.hermes.entity.customer.PackageLock;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.enums.ImportStatus;
import com.source3g.hermes.enums.Sex;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.utils.Page;
import com.source3g.hermes.utils.excel.ExcelUtils;

@Service
public class CustomerImportService extends BaseService {

	private static Logger logger = LoggerFactory.getLogger(CustomerImportService.class);

	@PostConstruct
	public void initMethod() {
		logger.debug("初始化锁");
		mongoTemplate.updateMulti(new Query(), new Update().set("locking", Boolean.FALSE), PackageLock.class);
	}

	public void updateStatus(CustomerImportLog customerImportLog, ImportStatus status) {
		customerImportLog.setStatus(status.toString());
		updateIncludeProperties(customerImportLog, "status");
	}

	public Page findImportLog(String merchantId, int pageNoInt, Date startTime, Date endTime) {
		Query query = new Query();
		if (startTime != null && endTime != null) {
			query.addCriteria(Criteria.where("importTime").gte(startTime).lte(endTime));
		} else if (startTime != null) {
			query.addCriteria(Criteria.where("importTime").gte(startTime));
		} else if (endTime != null) {
			query.addCriteria(Criteria.where("importTime").lte(endTime));
		}
		Merchant merchant = new Merchant();
		merchant.setId(merchantId);
		query.addCriteria(Criteria.where("merchant").is(merchant));
		Sort sort = new Sort(Direction.DESC, "_id");
		query.with(sort);
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, CustomerImportLog.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNoInt);
		List<CustomerImportLog> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), CustomerImportLog.class);
		page.setData(list);
		return page;
	}

	public void importCustomer(List<CustomerImportItem> customerImportItems, String merchantId, String customerImportLogId) {
		CustomerImportLog customerImportLog = mongoTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(customerImportLogId))),
				CustomerImportLog.class);
		List<CustomerGroup> customerGroups = mongoTemplate.find(new Query(Criteria.where("merchantId").is(new ObjectId(merchantId))),
				CustomerGroup.class);
		Collection<CustomerImportItem> itemsNoRepeat = filterImportItems(customerImportItems);
		customerImportLog.setTotalCount(itemsNoRepeat.size());
		customerImportLog.setStatus(ImportStatus.导入中.toString());
		customerImportLog.setFailedCount(0);
		mongoTemplate.save(customerImportLog);
		Map<String, SimpleCustomer> phoneMap = findPhoneMap(new ObjectId(merchantId));
		List<Customer> customerList = new ArrayList<Customer>();
		logger.error("开始导入");
		List<CustomerImportItem> failedImortItems = new ArrayList<CustomerImportItem>();
		// 加锁
		mongoTemplate.upsert(new Query(Criteria.where("merchantId").is(new ObjectId(merchantId))), new Update().set("locking", Boolean.TRUE),
				PackageLock.class);
		try {
			for (CustomerImportItem customerImportItem : itemsNoRepeat) {
				try {
					checkItem(customerImportItem);
					Customer customer = new Customer();
					customer.setAddress(customerImportItem.getAddress());
					customer.setBirthday(customerImportItem.getBirthday());
					ObjectId customerGroupId = findCustomerGroupIdByName(customerGroups, customerImportItem.getCustomerGroupName());
					if (customerGroupId == null) {
						throw new Exception("顾客组不存在");
					}
					customer.setCustomerGroup(new CustomerGroup(customerGroupId));
					customer.setEmail(customerImportItem.getEmail());
					customer.setMerchantId(customerImportItem.getMerchantId());
					customer.setName(customerImportItem.getName());
					customer.setNote(customerImportItem.getNote());
					customer.setPhone(customerImportItem.getPhone());
					customer.setQq(customerImportItem.getQq());
					customer.setSex(customerImportItem.getSex());
					customer.setOperateTime(new Date());
					if (phoneMap.get(customer.getPhone()) != null) {
						// customer.setId(phoneMap.get(customer.getPhone()).getId());
						Update update = new Update();
						update.set("address", customerImportItem.getAddress()).set("birthday", customerImportItem.getBirthday())
								.set("email", customerImportItem.getEmail()).set("name", customerImportItem.getName())
								.set("note", customerImportItem.getNote()).set("phone", customerImportItem.getPhone())
								.set("qq", customerImportItem.getQq()).set("sex", customerImportItem.getSex()).set("operateTime", new Date())
								.set("customerGroup", new DBRef(mongoTemplate.getDb(), "customerGroup", customerGroupId));
						// mongoTemplate.save(customer);
						mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(phoneMap.get(customer.getPhone()).getId())), update,
								Customer.class);
					} else {
						customerList.add(customer);
					}
				} catch (Exception e) {
					e.printStackTrace();
					customerImportItem.setImportStatus(ImportStatus.导入失败.toString());
					customerImportItem.setFailedReason(e.getMessage());
					customerImportLog.setFailedCount(customerImportLog.getFailedCount() + 1);
					failedImortItems.add(customerImportItem);
				}
			}
			mongoTemplate.insertAll(customerList);
			mongoTemplate.insertAll(failedImortItems);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mongoTemplate.updateFirst(new Query(Criteria.where("merchantId").is(new ObjectId(merchantId))),
					new Update().set("locking", Boolean.FALSE), PackageLock.class);
		}
		logger.debug("导入完成 ");
		customerImportLog.setStatus(ImportStatus.导入完成.toString());
		mongoTemplate.save(customerImportLog);
	}

	private Collection<CustomerImportItem> filterImportItems(List<CustomerImportItem> customerImportItems) {
		Map<String, CustomerImportItem> map = new HashMap<String, CustomerImportItem>();
		for (CustomerImportItem customerImportItem : customerImportItems) {
			map.put(customerImportItem.getPhone(), customerImportItem);
		}
		return map.values();
	}

	private Map<String, SimpleCustomer> findPhoneMap(ObjectId merchantId) {
		Map<String, SimpleCustomer> map = new HashMap<String, SimpleCustomer>();
		BasicDBObject parameter = new BasicDBObject();
		parameter.put("merchantId", merchantId);
		List<SimpleCustomer> simpleCustomers = super.findByBasicDBObject(Customer.class, parameter, new ObjectMapper<SimpleCustomer>() {
			@Override
			public SimpleCustomer mapping(DBObject obj) {
				SimpleCustomer simpleCustomer = new SimpleCustomer();
				simpleCustomer.setId((ObjectId) obj.get("_id"));
				simpleCustomer.setPhone((String) obj.get("phone"));
				return simpleCustomer;
			}
		});
		for (SimpleCustomer simpleCustomer : simpleCustomers) {
			map.put(simpleCustomer.getPhone(), simpleCustomer);
		}
		return map;
	}

	private void checkItem(CustomerImportItem customerImportItem) throws Exception {
		// ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		// Validator validator = factory.getValidator();
		// try {
		// Set<ConstraintViolation<CustomerImportItem>> constraintViolations =
		// validator.validate(customerImportItem);
		// if (constraintViolations.size() > 0) {
		// String failedReason = "";
		// for (ConstraintViolation<CustomerImportItem> v :
		// constraintViolations) {
		// failedReason += v.getMessage() + ",";
		// }
		// throw new Exception(failedReason);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		if (StringUtils.isEmpty(customerImportItem.getPhone())) {
			throw new Exception(" 电话号码不能为空");
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher match = pattern.matcher(customerImportItem.getPhone());
		if (match.matches() == false) {
			throw new Exception("电话号码必须为数字");
		}

		return;
	}

	private ObjectId findCustomerGroupIdByName(List<CustomerGroup> customerGroups, String name) {
		for (CustomerGroup group : customerGroups) {
			if (group.getName().equals(name)) {
				return group.getId();
			}
		}
		return null;
	}

	public List<CustomerImportItem> readFromExcel(Resource resource, String merchantId, String customerImportLogId) throws InvalidFormatException,
			IOException {
		List<CustomerImportItem> result = new ArrayList<CustomerImportItem>();
		// 创建文件输入流对象
		InputStream is = resource.getInputStream();
		// 创建 POI文件系统对象
		Workbook wb = WorkbookFactory.create(is);
		// 获取工作薄
		Sheet sheet = wb.getSheetAt(0);

		if (sheet.getLastRowNum() <= 0) {
			return result;
		}
		// 声明行对象
		Row row = null;
		// 通过循环获取每一行
		Row header = sheet.getRow(0);
		int nameIndex = 0;
		int sexIndex = 0;
		int birthIndex = 0;
		int phoneIndex = 0;
		int addrIndex = 0;
		int qqIndex = 0;
		int emailIndex = 0;
		int noteIndex = 0;
		int customerGroupIndex = 0;
		for (int headerIndex = 0; header.getCell(headerIndex) != null; headerIndex++) {
			if ("姓名".equals(header.getCell(headerIndex).getStringCellValue())) {
				nameIndex = headerIndex;
				continue;
			}
			if ("性别".equals(header.getCell(headerIndex).getStringCellValue())) {
				sexIndex = headerIndex;
				continue;
			}
			if ("生日".equals(header.getCell(headerIndex).getStringCellValue())) {
				birthIndex = headerIndex;
				continue;
			}
			if ("电话".equals(header.getCell(headerIndex).getStringCellValue())) {
				phoneIndex = headerIndex;
				continue;
			}
			if ("地址".equals(header.getCell(headerIndex).getStringCellValue())) {
				addrIndex = headerIndex;
				continue;
			}
			if ("qq".equals(header.getCell(headerIndex).getStringCellValue())) {
				qqIndex = headerIndex;
				continue;
			}
			if ("email".equals(header.getCell(headerIndex).getStringCellValue())) {
				emailIndex = headerIndex;
				continue;
			}
			if ("备注".equals(header.getCell(headerIndex).getStringCellValue())) {
				noteIndex = headerIndex;
				continue;
			}
			if ("顾客组名".equals(header.getCell(headerIndex).getStringCellValue())) {
				customerGroupIndex = headerIndex;
				continue;
			}
		}
		for (int i = 1; sheet.getRow(i) != null; i++) {
			// }
			// for (int i = 1; i < rows.size(); i++) {
			row = sheet.getRow(i);
			if (ExcelUtils.isNull(row)) {
				continue;
			}
			CustomerImportItem customerImportItem = new CustomerImportItem();
			customerImportItem.setId(ObjectId.get());
			customerImportItem.setMerchantId(new ObjectId(merchantId));
			try {
				if (row.getCell(nameIndex) != null) {
					customerImportItem.setName(ExcelUtils.getCellStringValue(row.getCell(nameIndex)));
				}
				if (row.getCell(sexIndex) != null) {
					String sexStr = row.getCell(sexIndex).getStringCellValue();
					if ("男".equals(sexStr)) {
						customerImportItem.setSex(Sex.MALE);
					} else {
						customerImportItem.setSex(Sex.FEMALE);
					}
				}
				if (row.getCell(birthIndex) != null) {
					// String birthStr =
					// row.getCell(birthIndex).getStringCellValue();
					// birthStr = birthStr.replace("月", "-");
					// birthStr = birthStr.replace("日", "-");
					try {
						String birth = ExcelUtils.getCellStringValue(row.getCell(birthIndex));
						if (StringUtils.isNotEmpty(birth)) {
							Date date = new Date(Long.parseLong(birth));
							SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
							customerImportItem.setBirthday(sdf.format(date));
						}
					} catch (Exception e) {
						throw new Exception("生日格式不正确");
					}
				}
				if (row.getCell(phoneIndex) != null) {
					customerImportItem.setPhone(ExcelUtils.getCellStringValue(row.getCell(phoneIndex)));
				}
				if (row.getCell(addrIndex) != null) {
					customerImportItem.setAddress(ExcelUtils.getCellStringValue(row.getCell(addrIndex)));
				}
				if (row.getCell(qqIndex) != null) {
					customerImportItem.setQq(ExcelUtils.getCellStringValue(row.getCell(qqIndex)));
				}
				if (row.getCell(emailIndex) != null) {
					customerImportItem.setEmail(ExcelUtils.getCellStringValue(row.getCell(emailIndex)));
				}
				if (row.getCell(noteIndex) != null) {
					customerImportItem.setNote(ExcelUtils.getCellStringValue(row.getCell(noteIndex)));
				}
				if (row.getCell(customerGroupIndex) != null) {
					customerImportItem.setCustomerGroupName(ExcelUtils.getCellStringValue(row.getCell(customerGroupIndex)));
				}

				customerImportItem.setImportStatus(ImportStatus.未导入.toString());
				customerImportItem.setCustomerImportLogId(new ObjectId(customerImportLogId));
				// mongoTemplate.insert(customerImportItem);
				result.add(customerImportItem);
			} catch (Throwable e) {
				customerImportItem.setImportStatus(ImportStatus.导入失败.toString());
				customerImportItem.setFailedReason(e.getMessage());
				customerImportItem.setCustomerImportLogId(new ObjectId(customerImportLogId));
				mongoTemplate.insert(customerImportItem);
			}
		}
		// 不入库
		// mongoTemplate.insertAll(result);
		return result;
	}

	public Page findImportItems(int pageNo, String importLogId) {
		ObjectId importLogObjId = new ObjectId(importLogId);

		Query query = new Query(Criteria.where("customerImportLogId").is(importLogObjId));
		Page page = new Page();
		page.setTotalRecords(mongoTemplate.count(query, CustomerImportItem.class));
		page.gotoPage(pageNo);
		List<CustomerImportItem> customerImportItem = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()),
				CustomerImportItem.class);
		page.setData(customerImportItem);
		return page;
	}

	public void testImport() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("开始导入" + sdf.format(date));
		List<CustomerImportItem> list = new ArrayList<CustomerImportItem>();

		for (int i = 0; i < 100; i++) {
			CustomerImportItem customerImportItem = new CustomerImportItem();
			customerImportItem.setAddress("111");
			customerImportItem.setBirthday("02-01");
			customerImportItem.setCustomerGroupName("普通顾客组");
			customerImportItem.setEmail("aa.com");
			customerImportItem.setPhone("13057707964");
			customerImportItem.setCustomerImportLogId(new ObjectId("514faef36abaed4035070e23"));
			customerImportItem.setFailedReason("123");

			customerImportItem.setQq("303844824");
			customerImportItem.setSex(Sex.MALE);
			// mongoTemplate.insert(customerImportItem);
			list.add(customerImportItem);
		}
		mongoTemplate.insertAll(list);
		date = new Date();
		System.out.println("导入完成" + sdf.format(date));
	}

	static class SimpleCustomer {
		private String phone;
		private ObjectId Id;

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public ObjectId getId() {
			return Id;
		}

		public void setId(ObjectId id) {
			Id = id;
		}

	}

}
