package com.source3g.hermes.sync.api;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/version")
public class VersionApi {

	@RequestMapping(value = "/download/{year}/{month}/{fileName}", method = RequestMethod.GET)
	public void versionDowload(@PathVariable String year,@PathVariable String month,@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException{
		String path="D:/aaa/file/"+year+"/"+month+"/"+fileName;
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
}
