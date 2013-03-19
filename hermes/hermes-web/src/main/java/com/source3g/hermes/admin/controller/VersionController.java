package com.source3g.hermes.admin.controller;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.constants.ReturnConstants;

@Controller
@RequestMapping("/admin/version")
public class VersionController {
	
	@RequestMapping(value = "toVersion", method = RequestMethod.GET)
	public ModelAndView toVersion(){
		return new ModelAndView("admin/system/version");
	}
	
	@RequestMapping(value = "import", method = RequestMethod.POST)
	@ResponseBody
	public String importNewVersion(@RequestParam("file") MultipartFile file){
		File fileToCopy = new File("D:/aaa/file/" + new Date().getTime()+".rar");
		try {
			FileUtils.copyInputStreamToFile(file.getInputStream(), fileToCopy);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ReturnConstants.SUCCESS;
	}
	
}
