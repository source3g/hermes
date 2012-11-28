package com.source3g.hermes.sync.api;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.source3g.hermes.service.TaskService;

@Controller
@RequestMapping(value = "/package")
public class PackageController {

	@Autowired
	private TaskService taskService;

	@RequestMapping(value = "/{year}/{month}/{day}/{merchantId}/{fileName}", method = RequestMethod.GET)
	public void download(@PathVariable String year, @PathVariable String month, @PathVariable String day, @PathVariable String merchantId, @PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
		// response.setContentType("text/html;charset=UTF-8");
		// request.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		String downLoadPath = taskService.getTaskPackagePath() + year + "/" + month + "/" + day + "/" + merchantId + "/" + fileName;

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

}
