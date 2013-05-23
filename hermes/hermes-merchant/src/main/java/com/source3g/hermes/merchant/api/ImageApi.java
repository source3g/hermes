package com.source3g.hermes.merchant.api;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.source3g.hermes.service.BaseService;

@Controller
@RequestMapping("/menu/images")
public class ImageApi {

	@Autowired
	private BaseService baseService;

	@RequestMapping(value = "/{year}/{month}/{day}/{fileName}")
	public void writeElectricMenuImage(@PathVariable String year, @PathVariable String month,@PathVariable String day, @PathVariable String fileName, HttpServletResponse response) throws IOException {
		File file = new File(baseService.getPicPath() + year + "/" + month + "/"+day+"/" + fileName);
		FileInputStream inputStream = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		inputStream.read(data);
		inputStream.close();
		response.setContentType("image/jpg");
		OutputStream stream = response.getOutputStream();
		stream.write(data);
		stream.flush();
		stream.close();
	}

	@RequestMapping(value = "/haha/menu")
	@ResponseBody
	public String aa() {
		return "fff";
	}
}