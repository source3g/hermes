package com.source3g.hermes.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class GeneralExceptionController implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object obj, Exception e) {
		e.printStackTrace();
		Map<String, Object> model = new HashMap<String, Object>();
		if (e instanceof UnauthorizedException) {
			model.put("errorMsg", "sorry,您没有权限访问此页面");
		} else if (e instanceof NotLoginException) {
			model.put("errorMsg", "sorry,请重新登录");
		} else {
			model.put("errorMsg", e.getMessage());
		}
		return new ModelAndView("/exception", model);
	}
}
