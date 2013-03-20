package com.source3g.hermes.admin.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.utils.ConfigParams;

@Controller
@RequestMapping("/admin/version")
public class VersionController {

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "toVersion", method = RequestMethod.GET)
	public ModelAndView toVersion() {
		return new ModelAndView("admin/system/version");
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public String importNewVersion(@RequestParam String version, @RequestParam("file") MultipartFile file) throws IOException {
		File fileToCopy = new File("/temp/file/" + new Date().getTime());
		FileUtils.copyInputStreamToFile(file.getInputStream(), fileToCopy);
		Resource resource = new FileSystemResource(fileToCopy);
		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
		formData.add("file", resource);
		formData.add("oldName", new String(file.getOriginalFilename()));
		formData.add("version", version);
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, requestHeaders);
		String result = restTemplate.postForObject(ConfigParams.getBaseUrl() + "version/upload/", requestEntity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return ReturnConstants.SUCCESS;
		}
		return result;
	}

}
