package com.source3g.hermes.monitor.api;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.source3g.hermes.constants.Constants;
import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.monitor.service.ErrorLogService;
import com.source3g.hermes.monitor.service.MailService;

@Controller
@RequestMapping(value = "/errorLog")
public class ErrorLogApi {
	@Autowired
	private ErrorLogService errorLogService;
	@Autowired
	private MailService mailService;

	@RequestMapping(value = "/report/{sn}", method = RequestMethod.POST)
	@ResponseBody
	public String report(@RequestParam("file") MultipartFile file, @PathVariable String sn, HttpServletRequest req) throws IOException {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat(Constants.FILE_PATH_DATE_FORMAT);
		String path = errorLogService.getErrorReportDir() + dateFormat.format(date) + "/" + sn + "/";
		File filePath = new File(path);
		filePath.mkdirs();
		File localFile = new File(path + file.getOriginalFilename());
		FileUtils.copyInputStreamToFile(file.getInputStream(), localFile);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "sendMail")
	@ResponseBody
	public String sendMail() throws EmailException {
		mailService.sendEmail();
		return ReturnConstants.SUCCESS;
	}
}
