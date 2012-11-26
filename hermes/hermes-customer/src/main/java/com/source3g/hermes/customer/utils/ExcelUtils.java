package com.source3g.hermes.customer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
//				System.out.println(row.getCell(j).getCellType()+row.getCell(j).toString());
				cellList.add(row.getCell(j));
			}
			result.add(cellList);
		}
		return result;
	}
	
}
