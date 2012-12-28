package com.source3g.hermes.customer.api;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.customer.dto.CallRecordDto;
import com.source3g.hermes.customer.dto.CustomerDto;
import com.source3g.hermes.customer.dto.NewCustomerDto;
import com.source3g.hermes.customer.service.CustomerImportService;
import com.source3g.hermes.customer.service.CustomerService;
import com.source3g.hermes.entity.Device;
import com.source3g.hermes.entity.customer.CallRecord;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.CustomerImportItem;
import com.source3g.hermes.entity.customer.CustomerImportLog;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.enums.ImportStatus;
import com.source3g.hermes.enums.TypeEnum.CustomerType;
import com.source3g.hermes.utils.DateFormateUtils;
import com.source3g.hermes.utils.Page;
import com.source3g.hermes.vo.CallInStatistics;
import com.source3g.hermes.vo.CallInStatisticsToday;

@Controller
@RequestMapping("/customer")
public class CustomerApi {
	private Logger logger = LoggerFactory.getLogger(CustomerApi.class);

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerImportService customerImportService;

	@Autowired
	private MongoTemplate mongoTemplate;

	@RequestMapping(value = "/export/download/{year}/{month}/{day}/{merchantId}/{fileName}", method = RequestMethod.GET)
	public void downloadExport(@PathVariable String year, @PathVariable String month, @PathVariable String day, @PathVariable String merchantId, @PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		String downLoadPath = customerService.getExportDir() + year + "/" + month + "/" + day + "/" + merchantId + "/" + fileName;

		long fileLength = new File(downLoadPath).length();

		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		response.setHeader("Content-Length", String.valueOf(fileLength));

		bis = new BufferedInputStream(new FileInputStream(downLoadPath));
		bos = new BufferedOutputStream(response.getOutputStream());
		byte[] buff = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		bis.close();
		bos.close();
	}

	@RequestMapping(value = "/phoneValidate/{phone}", method = RequestMethod.GET)
	@ResponseBody
	public Boolean phoneValidate(@PathVariable String phone) {
		return customerService.phoneValidate(phone);
	}

	@RequestMapping(value = "/add/", method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody Customer customer) {
		customerService.add(customer);
		return "success";
	}

	@RequestMapping(value = "/get/{sn}/{phone}", method = RequestMethod.GET)
	@ResponseBody
	public CustomerDto getBySn(@PathVariable String sn, @PathVariable String phone) {
		return customerService.findBySnAndPhone(sn, phone);
	}

	@RequestMapping(value = "customerListBycustomerGroupId/{customerGroupId}", method = RequestMethod.GET)
	@ResponseBody
	public  List<Customer> customerListBycustomerGroupId(@PathVariable ObjectId customerGroupId) {
		return customerService.customerListBycustomerGroupId(customerGroupId);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Customer get(@PathVariable String id) {
		return customerService.get(id);
	}

	
	@RequestMapping(value = "/list/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public Page list(String pageNo, String name, String phone, String property, String sortType, String phoneSortType, CustomerType type, @PathVariable String merchantId) {
		logger.debug("list customer....");
		int pageNoInt = Integer.valueOf(pageNo);
		Customer customer = new Customer();
		customer.setName(name);
		customer.setMerchantId(new ObjectId(merchantId));
		customer.setPhone(phone);
		Direction direction = Direction.DESC;
		if ("asc".equalsIgnoreCase(sortType) || "asc".equalsIgnoreCase(phoneSortType)) {
			direction = Direction.ASC;
		}
		if (StringUtils.isEmpty(property)) {
			property = "_id";
		}
		return customerService.list(pageNoInt, customer, type, direction, property);
	}

	@RequestMapping(value = "/export/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public String export(String name, String phone, @PathVariable String merchantId) {
		Customer customer = new Customer();
		customer.setName(name);
		customer.setMerchantId(new ObjectId(merchantId));
		customer.setPhone(phone);
		String result = "";
		try {
			result = customerService.export(customer);
		} catch (NoSuchMethodException | SecurityException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException e) {
			e.printStackTrace();
		}
		return customerService.getLocalUrl() + "customer/export/download/" + result + "/";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@RequestBody Customer customer) {
		logger.debug("update customer....");
		customerService.updateInfo(customer);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable String id) {
		customerService.deleteById(id);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/callIn/{deviceSn}/{phone}/{time}/{duration}", method = RequestMethod.GET)
	@ResponseBody
	public String callIn(@PathVariable String deviceSn, @PathVariable String phone, @PathVariable String time, @PathVariable String duration) {
		try {
			customerService.callIn(deviceSn, phone, time, duration);
		} catch (Exception e) {
			return e.getMessage();
		}
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/newCustomerList/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public Page newCustomerList(String pageNo, String name, String phone, @PathVariable String merchantId) {
		int pageNoInt = Integer.valueOf(pageNo);
		Customer customer = new Customer();
		customer.setName(name);
		customer.setMerchantId(new ObjectId(merchantId));
		customer.setPhone(phone);
		return customerService.list(pageNoInt, customer, CustomerType.newCustomer);
	}

	@RequestMapping(value = "/callInList/all/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public Page callInList(String pageNo, String name, String phone, CustomerType customerType, @PathVariable String merchantId) {
		int pageNoInt = Integer.valueOf(pageNo);
		Customer customer = new Customer();
		customer.setName(name);
		customer.setMerchantId(new ObjectId(merchantId));
		customer.setPhone(phone);
		return customerService.callInList(pageNoInt, customer, customerType);
	}

	@RequestMapping(value = "/importLog/merchant/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public Page importLog(@PathVariable String merchantId, String pageNo, Date startTime, Date endTime) {
		int pageNoInt = Integer.parseInt(pageNo);
		return customerImportService.findImportLog(merchantId, pageNoInt, startTime, endTime);
	}

	@RequestMapping(value = "/import/{merchantId}", method = RequestMethod.POST)
	@ResponseBody
	public String importCustomer(@RequestParam("file") MultipartFile file, @RequestParam("oldName") String oldName, @PathVariable String merchantId) {// MultipartFile
		String dir = customerService.getTempDir() + file.getOriginalFilename();
		File fileToCopy = new File(dir);
		try {
			FileUtils.copyInputStreamToFile(file.getInputStream(), fileToCopy);
		} catch (IOException e) {
			return "拷贝失败";
		}
		CustomerImportLog importLog = new CustomerImportLog();
		importLog.setId(ObjectId.get());
		Merchant merchant = new Merchant();
		merchant.setId(new ObjectId(merchantId));
		Date importTime = new Date();
		importLog.setImportTime(importTime);
		importLog.setMerchant(merchant);
		importLog.setName(oldName);
		importLog.setNewName(file.getOriginalFilename());
		importLog.setStatus(ImportStatus.已接收准备导入.toString());
		importLog.setFilePath(fileToCopy.getAbsolutePath());
		try {
			customerService.addImportLog(importLog);
		} catch (Exception e) {
			return e.getMessage();
		}
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/importLog/items/{logId}", method = RequestMethod.GET)
	@ResponseBody
	public List<CustomerImportItem> findImportItems(@PathVariable String logId) {
		return customerImportService.findImportItems(logId);
	}

	@RequestMapping(value = "/callInStatistics/{id}/", method = RequestMethod.GET)
	@ResponseBody
	public CallInStatistics callInStatistics(@PathVariable String id, Date startTime, Date endTime) {
		return findCallInStatistics(id, startTime, endTime);
	}

	@RequestMapping(value = "/callInStatistics/today/{id}/", method = RequestMethod.GET)
	@ResponseBody
	public CallInStatisticsToday callInStatisticsToday(@PathVariable String id, Date startTime, Date endTime) {
		return customerService.findCallInStatisticsToday(id, startTime, endTime);
	}

	@RequestMapping(value = "/callInStatistics/sn/{sn}/", method = RequestMethod.GET)
	@ResponseBody
	public CallInStatistics callInStatisticsBySn(@PathVariable String sn, Date startTime, Date endTime) {
		Device device = mongoTemplate.findOne(new Query(Criteria.where("sn").is(sn)), Device.class);
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("deviceIds").is(device.getId())), Merchant.class);
		return findCallInStatistics(merchant.getId().toString(), startTime, endTime);
	}

	private CallInStatistics findCallInStatistics(String merchantId, Date startTime, Date endTime) {
		if (endTime == null) {
			endTime = new Date();
		}
		if (startTime == null) {
			startTime = DateUtils.addDays(endTime, -30);
		}
		startTime = DateFormateUtils.getStartDateOfDay(startTime);
		endTime = DateFormateUtils.getEndDateOfDay(endTime);
		CallInStatistics callInStatistics = new CallInStatistics();
		callInStatistics.setAllList(customerService.findCallInCountByDay(merchantId, startTime, endTime, 0));
		callInStatistics.setNewList(customerService.findCallInCountByDay(merchantId, startTime, endTime, 1));
		callInStatistics.setOldList(customerService.findCallInCountByDay(merchantId, startTime, endTime, 2));
		return callInStatistics;
	}

	@RequestMapping(value = "/listCallRecord/{sn}", method = RequestMethod.GET)
	@ResponseBody
	public List<CallRecordDto> listCallRecord(@PathVariable String sn, Date startTime, Date endTime) {
		if (startTime == null) {
			startTime = DateFormateUtils.getStartDateOfDay(new Date());
		}
		if (endTime == null) {
			endTime = new Date();
		}
		List<Customer> customers = customerService.findByCallRecords(sn, startTime, endTime);
		List<CallRecordDto> result = new ArrayList<CallRecordDto>();
		filterCallRecord(startTime, endTime, customers, result);
		Collections.sort(result);
		return result;
	}

	@RequestMapping(value = "/newCustomerCount/sn/{sn}", method = RequestMethod.GET)
	@ResponseBody
	public long newCustomerCount(@PathVariable String sn, Date startTime) {
		if (startTime == null) {
			startTime = DateFormateUtils.getStartDateOfDay(new Date());
		}
		return customerService.findNewCustomerCount(sn, startTime);
	}

	@RequestMapping(value = "/newCustomerList/sn/{sn}", method = RequestMethod.GET)
	@ResponseBody
	public List<NewCustomerDto> newCustomerList(@PathVariable String sn, Date startTime) {
		if (startTime == null) {
			startTime = DateFormateUtils.getStartDateOfDay(new Date());
		}
		List<Customer> customers = customerService.findNewCustomers(sn, startTime);
		List<NewCustomerDto> customerDtos = new ArrayList<NewCustomerDto>();
		for (Customer c : customers) {
			NewCustomerDto newCustomerDto = new NewCustomerDto();
			newCustomerDto.setLastCallTime(c.getLastCallInTime());
			newCustomerDto.setPhone(c.getPhone());
			customerDtos.add(newCustomerDto);
		}
		return customerDtos;
	}

	private void filterCallRecord(Date startTime, Date endTime, List<Customer> customers, List<CallRecordDto> result) {
		for (Customer customer : customers) {
			for (CallRecord r : customer.getCallRecords()) {
				if (r.getCallTime().getTime() >= startTime.getTime() && r.getCallTime().getTime() <= endTime.getTime()) {
					CallRecordDto callRecordDto = new CallRecordDto();
					callRecordDto.setCallDuration(r.getCallDuration());
					callRecordDto.setCallTime(r.getCallTime());
					callRecordDto.setCustomerName(customer.getName());
					callRecordDto.setPhone(customer.getPhone());
					result.add(callRecordDto);
				}
			}
		}
	}
	
	//public List<Map<String,Object>> findTodayReminds(){
		
	//}
	
	
}
