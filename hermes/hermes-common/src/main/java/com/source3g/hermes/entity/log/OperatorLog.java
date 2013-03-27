package com.source3g.hermes.entity.log;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class OperatorLog extends AbstractEntity {
	private static final long serialVersionUID = -128476956237584195L;
	private String className;
	private String methodName;
	private List<Object> args;

	public OperatorLog(String className, String methodName, Object[] args) {
		this.className = className;
		this.methodName = methodName;
		this.args = Arrays.asList(args);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<Object> getArgs() {
		return args;
	}

	public void setArgs(List<Object> args) {
		this.args = args;
	}

	@Override
	public String toString() {
		String result = "class=" + className + ",method=" + methodName + ",params=";
		if (args != null && args.size() > 0) {
			for (int i = 0; i < args.size(); i++) {
				if (args.get(i) != null) {
					result += args.get(i).toString();
				} else {
					result += "null";
				}
				if (i != args.size() - 1) {
					result += ",";
				}
			}
		} else {
			result += "none";
		}
		return result;
	}

}
