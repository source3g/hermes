package com.source3g.hermes.utils.excel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ExcelObjectMapperDO implements Serializable {

	private static final long serialVersionUID = 7745296502632710593L;

	/**
	 * 对应类的属性
	 */
	private String objectFieldName;
	/**
	 * 对应类属性的类型
	 */
	private Class<?> objectFieldType;
	/**
	 * 对应Excel的列名
	 */
	private String excelColumnName;
	/**
	 * 对应Excel的列号
	 */
	protected Integer excelColumnNum;
	/**
	 * Excel列是否必须
	 */
	private boolean required;
	/**
	 * excel列是否存在
	 */
	protected boolean exist = false;

	/**
	 * 特殊值对应关系
	 */
	private Map<String, Object> valueMap = new HashMap<String, Object>();

	public String getObjectFieldName() {
		return objectFieldName;
	}

	public void setObjectFieldName(String objectFieldName) {
		this.objectFieldName = objectFieldName;
	}

	public String getExcelColumnName() {
		return excelColumnName;
	}

	public void setExcelColumnName(String excelColumnName) {
		this.excelColumnName = excelColumnName;
	}

	public Integer getExcelColumnNum() {
		return excelColumnNum;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public Class<?> getObjectFieldType() {
		return objectFieldType;
	}

	public void setObjectFieldType(Class<?> objectFieldType) {
		this.objectFieldType = objectFieldType;
	}

	@Override
	public String toString() {
		return this.excelColumnName.toString();
	}

	public boolean isExist() {
		return exist;
	}

	public Map<String, Object> getValueMap() {
		return valueMap;
	}

	public void setValueMap(Map<String, Object> valueMap) {
		this.valueMap = valueMap;
	}
}