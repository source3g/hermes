package com.source3g.hermes.common.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.source3g.hermes.service.BaseService;

@Controller
@RequestMapping(value = "/images/")
public class ImageApi {

	@Autowired
	private BaseService baseService;

	@RequestMapping(value = "/menu/{year}/{month}/{fileName}")
	public void writeElectricMenuImage(@PathVariable String year, @PathVariable String month, @PathVariable String fileName, HttpServletResponse response) throws IOException {
		File file = new File(baseService.getPicPath() + year + "/" + month + "/" + fileName);
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
}
