package com.source3g.hermes.customer.api;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.customer.service.CustomerImportService;
import com.source3g.hermes.customer.service.CustomerService;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.CustomerImportItem;
import com.source3g.hermes.entity.customer.CustomerImportLog;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.enums.ImportStatus;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping("/customer")
public class CustomerApi {
	private Logger logger = LoggerFactory.getLogger(CustomerApi.class);

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerImportService customerImportService;

	@RequestMapping(value = "/export/download/{year}/{month}/{day}/{merchantId}/{fileName}", method = RequestMethod.GET)
	public void downloadExport(@PathVariable String year, @PathVariable String month, @PathVariable String day, @PathVariable String merchantId, @PathVariable String fileName,HttpServletRequest request, HttpServletResponse response) throws IOException {
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

	@RequestMapping(value = "/add/", method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody Customer customer) {
		customerService.add(customer);
		return "success";
	}

	@RequestMapping(value = "/get/{sn}/{phone}", method = RequestMethod.GET)
	@ResponseBody
	public Customer getBySn(@PathVariable String sn, @PathVariable String phone) {
		return customerService.findBySnAndPhone(sn, phone);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Customer get(@PathVariable String id) {
		return customerService.get(id);
	}

	@RequestMapping(value = "/list/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public Page list(String pageNo, String name, String phone, @PathVariable String merchantId) {
		logger.debug("list customer....");
		int pageNoInt = Integer.valueOf(pageNo);
		Customer customer = new Customer();
		customer.setName(name);
		customer.setMerchantId(new ObjectId(merchantId));
		customer.setPhone(phone);
		return customerService.listByPage(pageNoInt, customer);
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
		return customerService.getLocalUrl()+"customer/export/download/"+result+"/";
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
		return customerService.list(pageNoInt, customer, true);
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

	@RequestMapping(value = "/importLog/merchantInfo/{id}", method = RequestMethod.GET)
	@ResponseBody
	public List<CustomerImportItem> importLogMerchantInfo(@PathVariable String id) {
		return customerImportService.importLogMerchantInfo(id);
	}
}
