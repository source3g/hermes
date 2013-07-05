package com.source3g.hermes.utils.excel;

import org.apache.poi.ss.usermodel.Row;

public interface ExcelRowMapper<T> {
	public T fill(Row row);
}
