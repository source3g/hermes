package com.source3g.hermes.utils.excel;

import java.io.File;

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
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				value = String.valueOf(cell.getDateCellValue().getTime());
			} else {
				value = String.valueOf(cell.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_STRING:
			value = cell.getRichStringCellValue().getString();
			break;
		}
		return value;
	}

}
