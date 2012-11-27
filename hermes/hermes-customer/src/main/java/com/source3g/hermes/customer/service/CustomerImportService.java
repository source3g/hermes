package com.source3g.hermes.customer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.customer.utils.ExcelUtils;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.CustomerGroup;
import com.source3g.hermes.entity.customer.CustomerImportItem;
import com.source3g.hermes.entity.customer.CustomerImportLog;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.enums.ImportStatus;
import com.source3g.hermes.enums.Sex;
import com.source3g.hermes.service.BaseService;

@Service
public class CustomerImportService extends BaseService {

	public void updateStatus(CustomerImportLog customerImportLog, ImportStatus status) {
		customerImportLog.setStatus(status.toString());
		updateIncludeProperties(customerImportLog, "status");
	}

	public List<CustomerImportLog> findImportLog(String merchantId) {
		Query query = new Query();
		Merchant merchant = new Merchant();
		merchant.setId(merchantId);
		query.addCriteria(Criteria.where("merchant").is(merchant));
		return mongoTemplate.find(query, CustomerImportLog.class);
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
			Customer customer = new Customer();
			customer.setId(ObjectId.get());
			customer.setAddress(customerImportItem.getAddress());
			customer.setBirthday(customerImportItem.getBirthday());
			customer.setCustomerGroupId(findCustomerGroupIdByName(customerGroups, customerImportItem.getCustomerGroupName()));
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
		} else {
			Customer customerInDb = mongoTemplate.findOne(new Query(Criteria.where("phone").is(customerImportItem.getPhone()).and("merchantId").is(customerImportItem.getMerchantId())), Customer.class);
			if (customerInDb != null) {
				customerImportItem.setImportStatus(ImportStatus.导入失败.toString());
				customerImportItem.setFailedReason("电话号码已存在");
				mongoTemplate.save(customerImportItem);
				return false;
			}
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
		List<List<Cell>> rows = ExcelUtils.readExcel(resource);
		if (rows.size() <= 1) {
			return result;
		}
		List<Cell> header = rows.get(0);
		int nameIndex = 0;
		int sexIndex = 0;
		int birthIndex = 0;
		int phoneIndex = 0;
		int addrIndex = 0;
		int qqIndex = 0;
		int emailIndex = 0;
		int noteIndex = 0;
		int customerGroupIndex = 0;
		for (int headerIndex = 0; headerIndex < header.size(); headerIndex++) {
			if ("姓名".equals(header.get(headerIndex).getStringCellValue())) {
				nameIndex = headerIndex;
				continue;
			}
			if ("性别".equals(header.get(headerIndex).getStringCellValue())) {
				sexIndex = headerIndex;
				continue;
			}
			if ("生日".equals(header.get(headerIndex).getStringCellValue())) {
				birthIndex = headerIndex;
				continue;
			}
			if ("电话".equals(header.get(headerIndex).getStringCellValue())) {
				phoneIndex = headerIndex;
				continue;
			}
			if ("地址".equals(header.get(headerIndex).getStringCellValue())) {
				addrIndex = headerIndex;
				continue;
			}
			if ("qq".equals(header.get(headerIndex).getStringCellValue())) {
				qqIndex = headerIndex;
				continue;
			}
			if ("email".equals(header.get(headerIndex).getStringCellValue())) {
				emailIndex = headerIndex;
				continue;
			}
			if ("备注".equals(header.get(headerIndex).getStringCellValue())) {
				noteIndex = headerIndex;
				continue;
			}
			if ("顾客组名".equals(header.get(headerIndex).getStringCellValue())) {
				customerGroupIndex = headerIndex;
				continue;
			}
		}

		for (int i = 1; i < rows.size(); i++) {
			List<Cell> row = rows.get(i);
			CustomerImportItem customerImportItem = new CustomerImportItem();
			customerImportItem.setName(row.get(nameIndex).getStringCellValue());
			String sexStr = row.get(sexIndex).getStringCellValue();
			if ("男".equals(sexStr)) {
				customerImportItem.setSex(Sex.MALE);
			} else {
				customerImportItem.setSex(Sex.FEMALE);
			}
			String birthStr = row.get(birthIndex).getStringCellValue();
			birthStr = birthStr.replace("月", "-");
			birthStr = birthStr.replace("日", "-");
			customerImportItem.setId(ObjectId.get());
			customerImportItem.setBirthday(birthStr);
			customerImportItem.setPhone(String.valueOf((long)row.get(phoneIndex).getNumericCellValue()));
			customerImportItem.setAddress(row.get(addrIndex).getStringCellValue());
			customerImportItem.setQq(String.valueOf((long)row.get(qqIndex).getNumericCellValue()));
			customerImportItem.setEmail(row.get(emailIndex).getStringCellValue());
			customerImportItem.setNote(row.get(noteIndex).getStringCellValue());
			customerImportItem.setMerchantId(new ObjectId(merchantId));
			customerImportItem.setCustomerGroupName(row.get(customerGroupIndex).getStringCellValue());
			customerImportItem.setImportStatus(ImportStatus.未导入.toString());
			customerImportItem.setCustomerImportLogId(new ObjectId(customerImportLogId));
			mongoTemplate.insert(customerImportItem);
			result.add(customerImportItem);
		}
		return result;
	}

}
