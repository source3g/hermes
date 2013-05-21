package com.source3g.hermes.utils.excel;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelHelper<T extends Object> {

	private List<ExcelObjectMapperDO> objectMappers;
	private Class<T> clazz;

	public ExcelHelper(List<ExcelObjectMapperDO> objectMappers, Class<T> clazz) {
		this.objectMappers = objectMappers;
		this.clazz = clazz;
	}

	public ReadExcelResult readFromExcel(File file) throws Exception {
		ReadExcelResult readExcelResult = new ReadExcelResult();
		List<T> result = new ArrayList<T>();
		// 找到第一行
		Sheet sheet = ExcelUtils.getSheet(file, 0);
		Row row = ExcelUtils.getRow(sheet, 0);
		initMapper(row);
		int maxRowNum = sheet.getLastRowNum();
		List<ErrorResult> reports = new ArrayList<ErrorResult>();
		for (int i = 1; i <= maxRowNum; i++) {
			// 开始行
			row = sheet.getRow(i);
			try {
				T t = fill(row);
				result.add(t);
			} catch (Exception e) {
				ErrorResult errorResult = new ErrorResult();
				errorResult.setRowNumber(i);
				errorResult.setMessage(e.getMessage());
				reports.add(errorResult);
			}

		}
		readExcelResult.setReports(reports);
		readExcelResult.setResult(result);
		return readExcelResult;
	}

	/**
	 * 将Excel中的一行转换成DO
	 * 
	 * @param row
	 * @return
	 * @throws Exception
	 */
	private T fill(Row row) throws Exception {
		T t = clazz.newInstance();
		for (ExcelObjectMapperDO eom : objectMappers) {
			if (!eom.isRequired() && eom.getExcelColumnNum() == null) {
				continue;
			}
			Cell cell = row.getCell(eom.getExcelColumnNum());
			String cellVaule = ExcelUtils.getCellStringValue(cell);
			if (eom.isRequired() && StringUtils.isEmpty(cellVaule)) {
				throw new Exception(eom.getExcelColumnName() + "列名不能为空");
			}

			// 其他的String类型
			if (cellVaule != null) {
				try {
					Class<?> fieldType = eom.getObjectFieldType();
					if (fieldType.equals(Integer.class) || fieldType.getSimpleName().equalsIgnoreCase("int")) {
						BeanUtils.setProperty(t, eom.getObjectFieldName(), new BigDecimal(cellVaule).intValue());
						continue;
					}
					if (fieldType.equals(Long.class) || fieldType.getSimpleName().equalsIgnoreCase("long")) {
						BeanUtils.setProperty(t, eom.getObjectFieldName(), new BigDecimal(cellVaule).longValue());
						continue;
					}
					if (fieldType.equals(Boolean.class) || fieldType.getSimpleName().equalsIgnoreCase("boolean")) {
						if (!eom.getValueMap().isEmpty()) {
							Map<String, Object> valueMap = eom.getValueMap();
							boolean value = (Boolean) valueMap.get(cellVaule);
							BeanUtils.setProperty(t, eom.getObjectFieldName(), value);
						} else {
							BeanUtils.setProperty(t, eom.getObjectFieldName(), Boolean.valueOf(cellVaule));
						}
						continue;
					}
					if (fieldType.getSimpleName().equalsIgnoreCase("Date")) {
						Date value = new Date(Long.parseLong(cellVaule));
						BeanUtils.setProperty(t, eom.getObjectFieldName(), value);
						continue;
					}
					if (fieldType.getSimpleName().equalsIgnoreCase("byte")) {
						BeanUtils.setProperty(t, eom.getObjectFieldName(), new BigDecimal(cellVaule).byteValue());
						continue;
					}
					BeanUtils.setProperty(t, eom.getObjectFieldName(), cellVaule);
				} catch (Exception e) {
					throw new Exception(eom.getExcelColumnName() + "设置属性值出错.", e);
				}
			}
		}
		return t;
	}

	private void initMapper(Row row) {
		// 根据字段名称找列号
		Iterator<?> it = row.cellIterator();
		for (int i = 0; it.hasNext(); i++) {
			Cell cell = (Cell) it.next();
			for (ExcelObjectMapperDO eom : objectMappers) {
				if (eom.getExcelColumnName().equals(ExcelUtils.getCellStringValue(cell))) {
					eom.excelColumnNum = i;
					eom.exist = true;
					break;
				}
			}
		}
	}

	public void writeToExcel(List<T> data, File destFile) {

	}

}
