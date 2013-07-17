package com.source3g.hermes.utils.excel;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtils {

	public static Sheet getSheet(File file, int sheetNum) throws Exception {
		// 创建 POI文件系统对象
		Workbook wb = WorkbookFactory.create(file);
		int max = wb.getNumberOfSheets();
		if (sheetNum >= max) {
			sheetNum = max - 1;
		}
		if (sheetNum < 0) {
			sheetNum = 0;
		}
		// 获取sheet
		return wb.getSheetAt(sheetNum);
	}

	public static int getSheetNum(File file, String sheetName) throws Exception {
		if (sheetName == null) {
			return 0;
		}
		Workbook wb = WorkbookFactory.create(file);
		return wb.getSheetIndex(sheetName);
	}

	public static Row getRow(File file, int sheetNum, int rowNum) throws Exception {
		Sheet sheet = getSheet(file, sheetNum);
		return getRow(sheet, rowNum);
	}

	public static Row getRow(Sheet sheet, int rowNum) throws Exception {
		// 验证有误数据
		if (sheet == null) {
			throw new IllegalArgumentException("file content is null.");
		}
		if (rowNum < 0) {
			rowNum = 0;
		}
		// 找到第一行
		return sheet.getRow(rowNum);
	}

	/**
	 * 判断Excel单元格中的内容，统一返回String类型
	 * 
	 * @param cell
	 * @return
	 */
	public static String getCellStringValue(Cell cell) {
		if (cell == null) {
			return null;
		}
		String value = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
		case Cell.CELL_TYPE_FORMULA:
			short dateFormat = cell.getCellStyle().getDataFormat();
			// yyyy-MM-dd----- 14
			// yyyy年m月d日--- 31
			// yyyy年m月------- 57
			// m月d日---------- 58
			// HH:mm----------- 20
			// h时mm分 ------- 32
			if (HSSFDateUtil.isCellDateFormatted(cell) || dateFormat == 14 || dateFormat == 31 || dateFormat == 57 || dateFormat == 58) {
				value = String.valueOf(cell.getDateCellValue().getTime());
			} else {
				DecimalFormat dd = new DecimalFormat("0");
				String lv = dd.format(cell.getNumericCellValue());
				value = String.valueOf(lv);
			}
			break;
		case Cell.CELL_TYPE_STRING:
			value = cell.getRichStringCellValue().getString();
			break;
		}
		return value;
	}

	public static String getPhoneValue(Cell cell) {
		if (cell == null) {
			return null;
		}
		String result = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
		case Cell.CELL_TYPE_FORMULA:
			DecimalFormat dd = new DecimalFormat("0");
			String lv = dd.format(cell.getNumericCellValue());
			result = String.valueOf(lv);
			break;
		case Cell.CELL_TYPE_STRING:
			result = processPhone(cell.getRichStringCellValue().getString());
			break;
		}
		return result;
	}

	private static String processPhone(String phone) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isEmpty(phone)) {
			return null;
		}
		for (int i = 0; i < phone.length(); i++) {
			char letter = phone.charAt(i);
			if (letter >= '0' && letter <= '9') {
				sb.append(letter);
			}
		}
		return sb.toString();
	}

	/**
	 * 获取单元格中的日期类型
	 * 
	 * @param cell
	 * @return
	 */
	public static Date getCellDateValue(Cell cell) {
		if (cell == null) {
			return null;
		}
		Date result = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
		case Cell.CELL_TYPE_FORMULA:
			short dateFormat = cell.getCellStyle().getDataFormat();
			// yyyy-MM-dd----- 14
			// yyyy年m月d日--- 31
			// yyyy年m月------- 57
			// m月d日---------- 58
			// HH:mm----------- 20
			// h时mm分 ------- 32
			if (HSSFDateUtil.isCellDateFormatted(cell) || dateFormat == 14 || dateFormat == 31 || dateFormat == 57 || dateFormat == 58) {
				result = new Date(cell.getDateCellValue().getTime());
			} else {
				DecimalFormat dd = new DecimalFormat("0");
				String lv = dd.format(cell.getNumericCellValue());
				try {
					result = new Date(Long.parseLong(lv));
				} catch (Exception e) {

				}
			}
			break;
		case Cell.CELL_TYPE_STRING:
			String dateStr = cell.getRichStringCellValue().getString();
			result = getDateFromString(dateStr);
			break;
		}
		return result;
	}

	public static Date getDateFromString(String str) {
		String patterns[] = { "yyyy-MM-dd", "MM-dd", "M-d", "yyyy/MM/dd", "MM/dd", "M/d", "yyyy年MM月dd日", "MM月dd日", "M月d日" };
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
		for (String pattern : patterns) {
			simpleDateFormat.applyPattern(pattern);
			try {
				Date date = simpleDateFormat.parse(str);
				if (date != null) {
					return date;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String[] args) {

	}

	public static boolean isNull(Row row) {
		if (row == null) {
			return true;
		}
		Iterator<?> it = row.cellIterator();
		while (it.hasNext()) {
			Cell cell = (Cell) it.next();
			String value = getCellStringValue(cell);
			if (StringUtils.isNotBlank(value)) {
				return false;
			}
		}
		return true;
	}
}
