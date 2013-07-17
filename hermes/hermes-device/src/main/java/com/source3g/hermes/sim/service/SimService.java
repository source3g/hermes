package com.source3g.hermes.sim.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.sim.SimImportRecord;
import com.source3g.hermes.entity.sim.SimInfo;
import com.source3g.hermes.service.BaseService;
import com.source3g.hermes.utils.Page;
import com.source3g.hermes.utils.excel.ExcelHelper;
import com.source3g.hermes.utils.excel.ExcelObjectMapperDO;
import com.source3g.hermes.utils.excel.ReadExcelResult;

@Service
public class SimService extends BaseService {

	@Value(value = "${temp.import.sim.dir}")
	private String importDir;

	public boolean simValidate(String no) {
		List<SimInfo> list = mongoTemplate.find(new Query(Criteria.where("no").is(no)), SimInfo.class);
		if (list.size() == 0) {
			return true;
		}
		return false;
	}

	public Page list(int pageNo, String serviceNumber,String imsiNo) {
		Query query = new Query();
		Criteria criteria=new Criteria();
		query.with(new Sort(Direction.DESC, "_id"));
		Page page = new Page();
	 if (StringUtils.isNotEmpty(serviceNumber)) {
			Pattern pattern = Pattern.compile("^.*" + serviceNumber + ".*$", Pattern.CASE_INSENSITIVE);
			criteria.and("serviceNo").is(pattern);
		}
		if(StringUtils.isNotEmpty(imsiNo)){
			Pattern pattern = Pattern.compile("^.*" + imsiNo + ".*$", Pattern.CASE_INSENSITIVE);
			criteria.and("imsiNo").is(pattern);
		}
		query.addCriteria(criteria);
		Long totalCount = mongoTemplate.count(query, SimInfo.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNo);
		List<SimInfo> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), SimInfo.class);
		page.setData(list);
		return page;
	}

	public void deleteById(String id) {
		ObjectId objId = new ObjectId(id);
		mongoTemplate.remove(new Query(Criteria.where("_id").is(objId)));
	}

	public SimInfo findByNo(String no) {
		return mongoTemplate.findOne(new Query(Criteria.where("no").is(no)), SimInfo.class);
	}

	public SimInfo findById(ObjectId id) {
		return mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), SimInfo.class);
	}

	public String getImportDir() {
		return importDir;
	}

	public void setImportDir(String importDir) {
		this.importDir = importDir;
	}

	public void importFromExcel(File excelFile) throws Exception {
		ExcelHelper<SimInfo> excelHelper = new ExcelHelper<>(initObjectMapper(), SimInfo.class);
		ReadExcelResult<SimInfo> result = excelHelper.readFromExcel(excelFile);
		for (SimInfo s : result.getResult()) {
			mongoTemplate.upsert(new Query(Criteria.where("serviceNo").is(s.getServiceNo())), genUpdate(s), SimInfo.class);
		}
		SimImportRecord importRecord = new SimImportRecord();
		importRecord.setImportCount(result.getResult().size());
		importRecord.setReportErrors(result.getReports());
		mongoTemplate.insert(importRecord);
		return;
	}

	private List<ExcelObjectMapperDO> initObjectMapper() {
		List<ExcelObjectMapperDO> list = new ArrayList<ExcelObjectMapperDO>();
		ExcelObjectMapperDO serviceNumberMapperDO = new ExcelObjectMapperDO();
		serviceNumberMapperDO.setExcelColumnName("业务号码");
		serviceNumberMapperDO.setObjectFieldName("serviceNo");
		serviceNumberMapperDO.setObjectFieldType(String.class);
		list.add(serviceNumberMapperDO);

		ExcelObjectMapperDO usernameMapperDO = new ExcelObjectMapperDO();
		usernameMapperDO.setExcelColumnName("用户姓名");
		usernameMapperDO.setObjectFieldName("username");
		usernameMapperDO.setObjectFieldType(String.class);
		list.add(usernameMapperDO);

		ExcelObjectMapperDO simUimNoMapperDO = new ExcelObjectMapperDO();
		simUimNoMapperDO.setExcelColumnName("SIM/UIM卡号");
		simUimNoMapperDO.setObjectFieldName("simUimCardNo");
		simUimNoMapperDO.setObjectFieldType(String.class);
		list.add(simUimNoMapperDO);

		ExcelObjectMapperDO imsiNoMapperDO = new ExcelObjectMapperDO();
		imsiNoMapperDO.setExcelColumnName("IMSI号");
		imsiNoMapperDO.setObjectFieldName("imsiNo");
		imsiNoMapperDO.setObjectFieldType(String.class);
		list.add(imsiNoMapperDO);
		return list;
	}

	public Update genUpdate(SimInfo simInfo) {
		Update update = new Update().set("username", simInfo.getUsername()).set("simUimCardNo", simInfo.getSimUimCardNo()).set("imsiNo", simInfo.getImsiNo());
		return update;
	}
}
