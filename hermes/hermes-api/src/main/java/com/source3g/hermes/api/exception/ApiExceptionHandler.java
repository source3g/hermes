package com.source3g.hermes.api.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class ApiExceptionHandler implements HandlerExceptionResolver {

	private static Logger logger=LoggerFactory.getLogger(ApiExceptionHandler.class);
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object obj, Exception e) {
		//e.printStackTrace();
		System.out.println(e.getMessage());
		logger.debug(e.getMessage());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("error", e.getMessage());
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/json;charset=UTF-8");
		PrintWriter writer = null;
		try {
			writer = resp.getWriter();
			if (e instanceof UnauthorizedException) {
				// model.put("errorMsg", "\"sorry,您没有权限访问此页面\"");
				writer.write(objectMapper.writeValueAsString("sorry,您没有权限访问此页面"));// e.getMessage()
			} else {
				writer.write(objectMapper.writeValueAsString(e.getMessage()));// e.getMessage()
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			writer.close();
		}

		return null;
	}

	// @Override
	// public ModelAndView resolveException(HttpServletRequest req,
	// HttpServletResponse resp, Object obj, Exception e) {
	// Map<String, Object> model = new HashMap<String, Object>();
	// if (e instanceof UnauthorizedException) {
	// model.put("errorMsg", "\"sorry,您没有权限访问此页面\"");
	// } else {
	//
	// try {
	// model.put("errorMsg", objectMapper.writeValueAsString(e.getMessage()));
	// } catch (IOException e1) {
	// model.put("errorMsg", "\"未知错误\"");
	// }
	// }
	// return new ModelAndView("/exception", model);
	// }
}
