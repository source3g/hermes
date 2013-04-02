package com.source3g.hermes.service;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.source3g.hermes.entity.log.OperatorLog;

@Component
@Aspect
public class LogService {
	private Logger logger = LoggerFactory.getLogger(LogService.class);
	@Autowired
	protected MongoTemplate mongoTemplate;

	public void log() {
		System.out.println("*************Log*******************");
	}

	public void doAfter(JoinPoint jp) {
		logger.info("log Ending method: " + jp.getTarget().getClass().getName() + "." + jp.getSignature().getName());
		System.out.println("log Ending method: " + jp.getTarget().getClass().getName() + "." + jp.getSignature().getName());
	}

	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		long time = System.currentTimeMillis();
		Object retVal = pjp.proceed();
		time = System.currentTimeMillis() - time;
		System.out.println("process time: " + time + " ms");
		return retVal;
	}

	@Before("execution(* com.source3g.hermes..api.*Api.*(..))")
	public void doBefore(JoinPoint jp) {
		Object[] args = jp.getArgs();
		OperatorLog operatorLog = new OperatorLog(jp.getTarget().getClass().getName(), jp.getSignature().getName(), args);
		System.out.println("log: " + operatorLog);
		// mongoTemplate.insert(operatorLog);
		// System.out.println("log: " + operatorLog);
		 List<Object> list=new ArrayList<Object>();
		 for(int i=0;i<args.length;i++){
		 if(args[i] instanceof CommonsMultipartFile){
		 CommonsMultipartFile c=(CommonsMultipartFile)args[i];
		 list.add(c.getFileItem().getFieldName());
		 list.add(c.getFileItem().getName());
		 }else{
		 list.add(args[i]);
		 }
		
		 }
		 operatorLog.setArgs(list);
		 mongoTemplate.insert(operatorLog);
	}

	// 有参无返回值的方法
	public void logArg(JoinPoint point) {
		// 此方法返回的是一个数组，数组中包括request以及ActionCofig等类对象
		Object[] args = point.getArgs();
		System.out.println("目标参数列表：");
		if (args != null) {
			for (Object obj : args) {
				System.out.println(obj + ",");
			}
			System.out.println();
		}
	}

	// 有参并有返回值的方法
	// @AfterReturning("execution(* com.source3g.hermes..api.*Api.*(..))")
	public void logArgAndReturn(JoinPoint point, Object returnObj) {
		// 此方法返回的是一个数组，数组中包括request以及ActionCofig等类对象
		Object[] args = point.getArgs();
		System.out.println("目标参数列表：");
		if (args != null) {
			for (Object obj : args) {
				System.out.println(obj + ",");
			}
			System.out.println();
			System.out.println("执行结果是：" + returnObj);
		}
	}
}
