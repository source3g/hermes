package com.source3g.hermes.utils.excel;

import java.io.File;
import java.text.DecimalFormat;
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
