package com.source3g.hermes.customer.utils;

import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class ExcelTest {

	@Test
	public void testExcel2007() throws IOException, InvalidFormatException {
		PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		Resource resource = resourceResolver.getResource("classpath:data/template.xlsx");
		List<List<Cell>> result=ExcelUtils.readExcel(resource);
		for (List<Cell> row:result){
			for (Cell cell:row){
				System.out.print(cell.toString());
			}
			System.out.println();
		}
		Assert.assertEquals(result.size(), 2);
	}
	@Test
	public void testExcel2003() throws IOException, InvalidFormatException {
		PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		Resource resource = resourceResolver.getResource("classpath:data/template.xls");
		List<List<Cell>> result=ExcelUtils.readExcel(resource);
		for (List<Cell> row:result){
			for (Cell cell:row){
				System.out.print(cell);
				System.out.print("|");
			}
			System.out.println();
		}
		Assert.assertEquals(result.size(),3);
	}
}
