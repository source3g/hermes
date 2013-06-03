package com.source3g.hermes.admin.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.Page;
import com.sourse3g.hermes.apkVersion.OnlineVersion;

@Controller
@RequestMapping("/admin/version")
@RequiresRoles("admin")
public class VersionController {

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "toVersion", method = RequestMethod.GET)
	public ModelAndView toVersion() {
		return new ModelAndView("admin/system/version");
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public String importNewVersion(@RequestParam("describe") String describe, @RequestParam("version") String version, @RequestParam("code") String code, @RequestParam("Filedata") MultipartFile Filedata, HttpServletRequest req) throws IOException {
		if (version == null) {
			return "版本号不能为空";
		}
		if (describe == null) {
			return "描述不能为空";
		}
		if (code == null) {
			return "对比编码不能为空";
		}
		File fileToCopy = new File("/temp/file/" + new Date().getTime());
		FileUtils.copyInputStreamToFile(Filedata.getInputStream(), fileToCopy);
		Resource resource = new FileSystemResource(fileToCopy);
		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
		formData.add("file", resource);
		formData.add("oldName", new String(Filedata.getOriginalFilename()));
		formData.add("version", version);
		formData.add("code", code);
		formData.add("describe", describe);
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, requestHeaders);
		String result = restTemplate.postForObject(ConfigParams.getBaseUrl() + "version/upload/", requestEntity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return "上传成功";
		}
		return "上传失败";
	}

	// 验证版本号是否存在
	@RequestMapping(value = "versionValidate", method = RequestMethod.GET)
	@ResponseBody
	public Boolean versionValidate(String version) {
		String uri = ConfigParams.getBaseUrl() + "version/versionValidate/" + version + "/";
		Boolean result = restTemplate.getForObject(uri, Boolean.class);
		return result;
	}

	// 验证对比编码是否存在
	@RequestMapping(value = "codeValidate", method = RequestMethod.GET)
	@ResponseBody
	public Boolean codeValidate(String code) {
		String uri = ConfigParams.getBaseUrl() + "version/codeValidate/" + code + "/";
		Boolean result = restTemplate.getForObject(uri, Boolean.class);
		return result;
	}

	@RequestMapping(value = "/changeOnline", method = RequestMethod.POST)
	public String changeOnline(String version) {
		String uri = ConfigParams.getBaseUrl() + "version/changeOnline";
		HttpEntity<String> entity = new HttpEntity<String>(version);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return "redirect:/admin/version/versionList";
		} else {
			return "redirect:/admin/version/versionList";
		}
	}
	
	@RequestMapping(value = "/changeBetaVersion", method = RequestMethod.POST)
	public String changeBetaVersion(String version) {
		String uri = ConfigParams.getBaseUrl() + "version/changeBetaVersion";
		HttpEntity<String> entity = new HttpEntity<String>(version);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return "redirect:/admin/version/versionList";
		} else {
			return "redirect:/admin/version/versionList";
		}
	}
	
	@RequestMapping(value = "versionList", method = RequestMethod.GET)
	public ModelAndView versionList(String pageNo) {
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		String uri = ConfigParams.getBaseUrl() + "version/versionList/?pageNo=" + pageNo;
		String onlineVersionUri = ConfigParams.getBaseUrl() + "version/releaseVersion";
		String betaVersionUri= ConfigParams.getBaseUrl() + "version/betaVersion";
		Page page = restTemplate.getForObject(uri, Page.class);
		OnlineVersion releaseVersion = restTemplate.getForObject(onlineVersionUri, OnlineVersion.class);
		OnlineVersion betaVersion =restTemplate.getForObject(betaVersionUri, OnlineVersion.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("page", page);
		model.put("onlineVersion", releaseVersion);
		if(betaVersion!=null){
			model.put("betaVersion", betaVersion);
		}
		return new ModelAndView("admin/system/versionList", model);
	}
	
	@RequestMapping(value = "/toGrayUpdateDevicesList", method = RequestMethod.GET)
	public String toGrayUpdateDevicesList(String pageNo,Model model) {
		if(StringUtils.isEmpty(pageNo)){
			pageNo="1";
		}
		String uri=ConfigParams.getBaseUrl()+"version/GrayUpdateDevicesList/?pageNo="+pageNo;
		String onlineVersionUri = ConfigParams.getBaseUrl() + "version/releaseVersion";
		String betaVersionUri= ConfigParams.getBaseUrl() + "version/betaVersion";
		Page page=restTemplate.getForObject(uri, Page.class);
		OnlineVersion releaseVersion = restTemplate.getForObject(onlineVersionUri, OnlineVersion.class);
		OnlineVersion betaVersion =restTemplate.getForObject(betaVersionUri, OnlineVersion.class);
		if(betaVersion!=null){
			model.addAttribute("betaVersion", betaVersion);
		}
		model.addAttribute("page", page);
		model.addAttribute("onlineVersion", releaseVersion);
		return "/admin/system/GrayUpdateDevicesList";
	}
	@RequestMapping(value = "/snValidate")
	@ResponseBody
	public Boolean DeviceSnValidate(String sn) {
		String uri = ConfigParams.getBaseUrl() + "version/snValidate/"+sn+"/";
		Boolean result=restTemplate.getForObject(uri, Boolean.class);
		return result;
	}
	
	@RequestMapping(value = "/addToGrayUpdateDevicesList", method = RequestMethod.POST)
	public String addToGrayUpdateDevicesList(String sn) {
		String uri = ConfigParams.getBaseUrl() + "version/addToGrayUpdateDevicesList/";
		HttpEntity<String> entity = new HttpEntity<String>(sn);
		restTemplate.postForObject(uri, entity, String.class);
		return "redirect:/admin/version/toGrayUpdateDevicesList";
	}
	
	@RequestMapping(value = "/deleteGrayUpdateDevice/{id}", method = RequestMethod.GET)
	public String deleteGrayUpdateDevice(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "version/deleteGrayUpdateDevice/"+id+"/";
		restTemplate.getForObject(uri, String.class);
		return "redirect:/admin/version/toGrayUpdateDevicesList";
	}
}
