package com.source3g.hermes.customer.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.CustomerGroup;
import com.source3g.hermes.entity.customer.CustomerImportItem;
import com.source3g.hermes.entity.customer.CustomerImportLog;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.enums.ImportStatus;
import com.source3g.hermes.enums.Sex;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.utils.Page;

@Service
public class CustomerImportService extends BaseService {

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
		Sort sort=new Sort(Direction.DESC, "_id");
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
		CustomerImportLog customerImportLog = mongoTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(customerImportLogId))), CustomerImportLog.class);
		List<CustomerGroup> customerGroups = mongoTemplate.find(new Query(Criteria.where("merchantId").is(new ObjectId(merchantId))), CustomerGroup.class);
		customerImportLog.setTotalCount(customerImportItems.size());
		customerImportLog.setStatus(ImportStatus.导入中.toString());
		mongoTemplate.save(customerImportLog);
		for (CustomerImportItem customerImportItem : customerImportItems) {
			if (!checkItem(customerImportItem)) {
				customerImportLog.setFailedCount(customerImportLog.getFailedCount() + 1);
				continue;
			}
			Customer customer = mongoTemplate.findOne(new Query(Criteria.where("phone").is(customerImportItem.getPhone()).and("merchantId").is(customerImportItem.getMerchantId())), Customer.class);
			if (customer == null) {
				customer=new Customer();
				customer.setId(ObjectId.get());
			}
			customer.setAddress(customerImportItem.getAddress());
			customer.setBirthday(customerImportItem.getBirthday());
			ObjectId customerGroupId= findCustomerGroupIdByName(customerGroups, customerImportItem.getCustomerGroupName());
			customer.setCustomerGroup(new CustomerGroup(customerGroupId));
			customer.setEmail(customerImportItem.getEmail());
			customer.setMerchantId(customerImportItem.getMerchantId());
			customer.setName(customerImportItem.getName());
			customer.setNote(customerImportItem.getNote());
			customer.setPhone(customerImportItem.getPhone());
			customer.setQq(customerImportItem.getQq());
			customer.setSex(customerImportItem.getSex());
			customer.setOperateTime(new Date());
			mongoTemplate.save(customer);
			customerImportItem.setImportStatus(ImportStatus.导入成功.toString());
			mongoTemplate.save(customerImportItem);
		}
		customerImportLog.setStatus(ImportStatus.导入成功.toString());
		mongoTemplate.save(customerImportLog);
	}

	private boolean checkItem(CustomerImportItem customerImportItem) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<CustomerImportItem>> constraintViolations = validator.validate(customerImportItem);
		if (constraintViolations.size() > 0) {
			String failedReason = "";
			for (ConstraintViolation<CustomerImportItem> v : constraintViolations) {
				failedReason += v.getMessage() + ",";
			}
			customerImportItem.setImportStatus(ImportStatus.导入失败.toString());
			customerImportItem.setFailedReason(failedReason);
			mongoTemplate.save(customerImportItem);
			return false;
		} 
		return true;
	}

	private ObjectId findCustomerGroupIdByName(List<CustomerGroup> customerGroups, String name) {
		for (CustomerGroup group : customerGroups) {
			if (group.getName().equals(name)) {
				return group.getId();
			}
		}
		return null;
	}

	public List<CustomerImportItem> fromExcelToDb(Resource resource, String merchantId, String customerImportLogId) throws InvalidFormatException, IOException {
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
			CustomerImportItem customerImportItem = new CustomerImportItem();
			customerImportItem.setId(ObjectId.get());
			customerImportItem.setMerchantId(new ObjectId(merchantId));
			if (row.getCell(nameIndex) != null) {
				customerImportItem.setName(row.getCell(nameIndex).getStringCellValue());
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
				String birthStr = row.getCell(birthIndex).getStringCellValue();
				birthStr = birthStr.replace("月", "-");
				birthStr = birthStr.replace("日", "-");
				customerImportItem.setBirthday(birthStr);
			}
			if (row.getCell(phoneIndex) != null) {
				customerImportItem.setPhone(String.valueOf((long) row.getCell(phoneIndex).getNumericCellValue()));
			}
			if (row.getCell(addrIndex) != null) {
				customerImportItem.setAddress(row.getCell(addrIndex).getStringCellValue());
			}
			if (row.getCell(qqIndex) != null) {
				customerImportItem.setQq(String.valueOf((long) row.getCell(qqIndex).getNumericCellValue()));
			}
			if (row.getCell(emailIndex) != null) {
				customerImportItem.setEmail(row.getCell(emailIndex).getStringCellValue());
			}
			if (row.getCell(noteIndex) != null) {
				customerImportItem.setNote(row.getCell(noteIndex).getStringCellValue());
			}
			if (row.getCell(customerGroupIndex) != null) {
				customerImportItem.setCustomerGroupName(row.getCell(customerGroupIndex).getStringCellValue());
			}

			customerImportItem.setImportStatus(ImportStatus.未导入.toString());
			customerImportItem.setCustomerImportLogId(new ObjectId(customerImportLogId));
			mongoTemplate.insert(customerImportItem);
			result.add(customerImportItem);
		}
		return result;
	}

	public List<CustomerImportItem> findImportItems(String importLogId) {
		ObjectId importLogObjId = new ObjectId(importLogId);
		List<CustomerImportItem> customerImportItem = mongoTemplate.find(new Query(Criteria.where("customerImportLogId").is(importLogObjId)), CustomerImportItem.class);
		return customerImportItem;
	}

}
