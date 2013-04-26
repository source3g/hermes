package com.source3g.hermes.service;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.source3g.hermes.device.service.DeviceService;

@Component
@Aspect
public class ApiSecurityService {
	// private Logger logger =
	// LoggerFactory.getLogger(ApiSecurityService.class);
	@Autowired
	private DeviceService deviceService;

	public void log() {
		System.out.println("*************Log*******************");
	}

	@Before("execution(* com.source3g.hermes..api.*Api.*(..))")
	public void doBefore(JoinPoint jp) throws Exception {
		Object[] args = jp.getArgs();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		for (Object arg : args) {
			if ("sn".equals(arg)) {
				String secret = (String) request.getAttribute("secret");
				if (deviceService.validateSecret((String) arg, secret)) {
					return;
				}
				throw new Exception("没有权限");
			}
		}
	}
}
