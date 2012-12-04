package com.source3g.hermes.customer.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.core.io.Resource;

public class ExcelUtils {
	public static List<List<Cell>> readExcel(Resource resource) throws IOException, InvalidFormatException {
		List<List<Cell>> result = new ArrayList<List<Cell>>();
		// 创建文件输入流对象
		InputStream is = resource.getInputStream();
		// 创建 POI文件系统对象
		Workbook wb = WorkbookFactory.create(is);
		// 获取工作薄
		Sheet sheet = wb.getSheetAt(0);
		// 声明行对象
		Row row = null;
		// 通过循环获取每一行
		for (int i = 0; sheet.getRow(i) != null; i++) {
			row = sheet.getRow(i);
			// 循环获取一行的中列
			List<Cell> cellList = new ArrayList<Cell>();
			for (int j = 0; row.getCell(j) != null; j++) {
				// System.out.println(row.getCell(j).getCellType()+row.getCell(j).toString());
				cellList.add(row.getCell(j));
			}
			result.add(cellList);
		}
		return result;
	}

	public static void createExcelHeader(File file, String[] headers) throws IOException {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet("work1");
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		OutputStream o = new FileOutputStream(file);
		workbook.write(o);
		o.close();
	}

	/**
	 * 生成表格 根据反射获取数据
	 * 
	 * @param file
	 *            文件
	 * @param headers
	 *            表头列表
	 * @param headerFieldMap
	 *            表头和字段名的对应Map
	 * @param dataset
	 *            数据
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */

	public static void createExcel(File file, String[] headers, Map<String, String> headerFieldMap, Collection<T> dataset) throws NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, FileNotFoundException, IOException {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet("work1");
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行
		Iterator<T> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = (T) it.next();
			for (int i = 0; i < headers.length; i++) {
				String fieldName = headerFieldMap.get(headers[i]);
				String firstLetter = fieldName.substring(0, 1).toUpperCase();
				Field field = t.getClass().getDeclaredField(fieldName);
				// 获得和属性对应的getXXX()方法的名字
				String getMethodName;
				if (field.getType() == boolean.class) {
					getMethodName = "is" + firstLetter + fieldName.substring(1);
				} else {
					getMethodName = "get" + firstLetter + fieldName.substring(1);
				}
				Method getMethod = t.getClass().getMethod(getMethodName, new Class[] {});
				Object value = getMethod.invoke(t, new Object[] {});
				HSSFCell cell = row.createCell(i);
				HSSFRichTextString richString = new HSSFRichTextString(value.toString());
				cell.setCellValue(richString);
			}
		}
		workbook.write(new FileOutputStream(file));
	}

	/**
	 * 根据HSSFCell类型设置数据
	 * 
	 * @param cell
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getCellFormatValue(HSSFCell cell) {
		String cellvalue = "";
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
			// 如果当前Cell的Type为NUMERIC
			case HSSFCell.CELL_TYPE_NUMERIC:
			case HSSFCell.CELL_TYPE_FORMULA: {
				// 判断当前的cell是否为Date
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// 如果是Date类型则，转化为Data格式

					// 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
					// cellvalue = cell.getDateCellValue().toLocaleString();

					// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					cellvalue = sdf.format(date);

				}
				// 如果是纯数字
				else {
					// 取得当前Cell的数值
					cellvalue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			}
			// 如果当前Cell的Type为STRIN
			case HSSFCell.CELL_TYPE_STRING:
				// 取得当前的Cell字符串
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			// 默认的Cell值
			default:
				cellvalue = " ";
			}
		} else {
			cellvalue = "";
		}
		return cellvalue;

	}

}
