package com.source3g.hermes.sync.api;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
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
import com.source3g.hermes.service.VersionService;
import com.source3g.hermes.utils.MD5;
import com.source3g.hermes.utils.Page;
import com.sourse3g.hermes.apkVersion.ApkVersion;

@Controller
@RequestMapping("/version")
public class VersionApi {

	@Autowired
	private VersionService versionService;

	@RequestMapping(value = "/download/{year}/{month}/{fileName}", method = RequestMethod.GET)
	public void versionDowload(@PathVariable String year, @PathVariable String month, @PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String path = versionService.getUploadDir() + year + "/" + month + "/" + fileName;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		long fileLength = new File(path).length();

		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		response.setHeader("Content-Length", String.valueOf(fileLength));

		bis = new BufferedInputStream(new FileInputStream(path));
		bos = new BufferedOutputStream(response.getOutputStream());
		byte[] buff = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		bis.close();
		bos.close();
	}

	@RequestMapping(value = "/upload",method = RequestMethod.POST)
	@ResponseBody
	public String upload(@RequestParam("describe") String describe,@RequestParam("file") MultipartFile file, @RequestParam("oldName") String oldName, @RequestParam("version") String version,@RequestParam("code") String code) throws IOException, ParseException {
		int codeInt=Integer.parseInt(code);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/");
		String suffxPath = dateFormat.format(new Date())+ oldName;
		String dir = versionService.getUploadDir() + suffxPath;
		File fileToCopy = new File(dir);
		FileUtils.copyInputStreamToFile(file.getInputStream(), fileToCopy);
		ApkVersion apkVersion = new ApkVersion(version, suffxPath, new Date(), describe,codeInt);
		apkVersion.setMd5(MD5.md5sum(fileToCopy.getAbsolutePath()));
		versionService.addVersion(apkVersion);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/versionValidate/{version}", method = RequestMethod.GET)
	@ResponseBody
	public Boolean versionValidate( @PathVariable String version) {
		return  versionService.versionValidate(version);
	}
	
	@RequestMapping(value = "/codeValidate/{code}", method = RequestMethod.GET)
	@ResponseBody
	public Boolean codeValidate( @PathVariable String code) {
		int codeInt=Integer.parseInt(code);
		return  versionService.codeValidate(codeInt);
	}
	@RequestMapping(value = "/changeOnline", method = RequestMethod.POST)
	@ResponseBody
	public String changeVersion(@RequestBody String code) {
		int codeInt=Integer.parseInt(code);
		versionService.changeVersion(codeInt);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/online", method = RequestMethod.GET)
	@ResponseBody
	public ApkVersion getLastVersion() {
		ApkVersion apkVersion = versionService.getOnlineVersion();
		if (apkVersion != null) {
			processUrl(apkVersion);
			return apkVersion;
		}
		return null;
	}

	@RequestMapping(value = "/online/sn/{sn}", method = RequestMethod.POST)
	@ResponseBody
	public ApkVersion getLastVersion(@PathVariable String sn, @RequestBody int code) {
		versionService.updateDeviceVersion(sn, code);
		return getLastVersion();
	}

	private void processUrl(ApkVersion apkVersion) {
		apkVersion.setUrl(versionService.getLocalUrl() + "version/download/" + apkVersion.getUrl() + "/");
	};

	@RequestMapping(value = "/versionList")
	@ResponseBody
	public Page versionList(String pageNo) throws IOException {
		int pageNoInt = Integer.parseInt(pageNo);
		return versionService.versionList(pageNoInt);
	}
}
