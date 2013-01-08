package com.source3g.hermes.api.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class ApiExceptionHandler implements HandlerExceptionResolver {

	// @Override
	// public ModelAndView resolveException(HttpServletRequest req,
	// HttpServletResponse resp, Object obj, Exception e) {
	// e.printStackTrace();
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("error", e.getMessage());
	// resp.setCharacterEncoding("utf-8");
	// resp.setContentType("application/json;charset=UTF-8");
	// PrintWriter writer = null;
	// try {
	// writer = resp.getWriter();
	// writer.write("\"出错了:\""+e.getMessage());// e.getMessage()
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// } finally {
	// writer.close();
	// }
	//
	// return null;
	// }

	@Override
	public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object obj, Exception e) {
		Map<String, Object> model = new HashMap<String, Object>();
		if (e instanceof UnauthorizedException) {
			model.put("errorMsg", "sorry,您没有权限访问此页面");
		} else {
			model.put("errorMsg", e.getMessage());
		}
		return new ModelAndView("/exception", model);
	}
}
