package com.source3g.hermes.utils.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelHelper<T extends Object> {
	private static final Logger logger = LoggerFactory.getLogger(ExcelHelper.class);

	private List<ExcelObjectMapperDO> objectMappers;
	private Class<T> clazz;

	static {
		BeanUtilsBean.setInstance(new BeanUtilsBean2());
		ConvertUtils.register(new Converter() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(Class type, Object value) {
				// 当value参数等于空时返回空
				if (value == null) {
					return null;
				}
				// 自定义时间的格式为yyyy-MM-dd28 29
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// 创建日期类对象
				String str = null;
				// 使用自定义日期的格式转化value参数为yyyy-MM-dd格式
				str = sdf.format((Date) value);
				// 返回dt日期对象
				return str;
			}
		}, Date.class);
	}

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
			if (ExcelUtils.isNull(row)) {
				continue;
			}
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

	public void writeToExcel(List<T> data, File destFile) throws IOException {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet("sheet1");
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < objectMappers.size(); i++) {
			HSSFCell cell = row.createCell(i);
			HSSFRichTextString text = new HSSFRichTextString(objectMappers.get(i).getExcelColumnName());
			cell.setCellValue(text);
			objectMappers.get(i).excelColumnNum = i;
			sheet.setColumnWidth(i, 255 * 25);
		}
		for (int i = 1; i <= data.size(); i++) {
			row = sheet.createRow(i);
			for (ExcelObjectMapperDO excelObjectMapperDO : objectMappers) {
				HSSFCell cell = row.createCell(excelObjectMapperDO.getExcelColumnNum());
				try {
					String value = BeanUtils.getProperty(data.get(i - 1), excelObjectMapperDO.getObjectFieldName());
					if (value == null) {
						continue;
					}
					Class<?> fieldType = excelObjectMapperDO.getObjectFieldType();
					if (fieldType.equals(Date.class) || fieldType.getSimpleName().equalsIgnoreCase("date")) {
						Pattern dateByDay = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
						Pattern shortDate = Pattern.compile("\\d{2}-\\d{2}");
						Pattern dateByTime = Pattern.compile("\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}");
						if (shortDate.matcher(value).matches()) {
							String year = "2013";
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							Date time = sdf.parse(year + "-" + value);
							HSSFDataFormat format = workbook.createDataFormat();
							HSSFCellStyle style = workbook.createCellStyle();
							style.setDataFormat(format.getFormat("MM-dd"));
							cell.setCellValue(time);
							cell.setCellStyle(style);
						} else if (dateByDay.matcher(value).matches()) {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							Date time = sdf.parse(value);
							HSSFDataFormat format = workbook.createDataFormat();
							HSSFCellStyle style = workbook.createCellStyle();
							style.setDataFormat(format.getFormat("yyyy-MM-dd"));
							cell.setCellValue(time);
							cell.setCellStyle(style);
						} else if (dateByTime.matcher(value).matches()) {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date time = sdf.parse(value);
							HSSFDataFormat format = workbook.createDataFormat();
							HSSFCellStyle style = workbook.createCellStyle();
							style.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm:ss"));
							cell.setCellValue(time);
							cell.setCellStyle(style);
						}
					} else if (fieldType.equals(Long.class) || fieldType.getSimpleName().equalsIgnoreCase("long")) {
						Long longValue = Long.parseLong(value);
						cell.setCellValue(longValue);
					} else {
						if (excelObjectMapperDO.getValueMap() != null && excelObjectMapperDO.getValueMap().size() > 0) {
							Object mappedValue = excelObjectMapperDO.getValueMap().get(value);
							value = mappedValue != null ? (String) mappedValue : value;
						}
						cell.setCellValue(value);
					}
				} catch (Exception e) {
					logger.debug(e.getMessage());
				}
			}
		}
		FileOutputStream fos = new FileOutputStream(destFile);
		workbook.write(fos);
		fos.close();
	}

	public static void main(String[] args) {
		String shortTime = "05-12";
		String dayTime = "2013-05-03";
		String time = "2013-05-06 15:23:16";
		Pattern dateByDay = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
		Pattern shortDate = Pattern.compile("\\d{2}-\\d{2}");
		Pattern dateByTime = Pattern.compile("\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}");
		System.out.println(shortDate.matcher(shortTime).matches());
		System.out.println(dateByDay.matcher(dayTime).matches());
		System.out.println(dateByTime.matcher(time).matches());

	}
}
