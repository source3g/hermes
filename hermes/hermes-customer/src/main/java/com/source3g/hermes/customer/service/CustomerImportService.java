package com.source3g.hermes.customer.service;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.customer.CustomerImportLog;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.enums.ImportStatus;
import com.source3g.hermes.service.BaseService;

@Service
public class CustomerImportService extends BaseService {

	public void updateStatus(CustomerImportLog customerImportLog, ImportStatus status) {
		customerImportLog.setStatus(status.toString());
		updateIncludeProperties(customerImportLog, "status");
	}

	@Override
	public String getCollectionName() {
		return "CustomerImportLog";
	}

	public List<CustomerImportLog> findImportLog(String merchantId) {
		Query query=new Query();
		Merchant merchant=new Merchant();
		merchant.setId(merchantId);
		query.addCriteria(Criteria.where("merchant").is(merchant));
		return mongoTemplate.find(query, CustomerImportLog.class);
	}
	

}
