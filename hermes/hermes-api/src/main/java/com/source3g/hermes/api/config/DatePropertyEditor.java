package com.source3g.hermes.api.config;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;

import com.source3g.hermes.utils.DateFormateUtils;

public class DatePropertyEditor extends PropertyEditorSupport {

	@Override
	public String getAsText() {
		SimpleDateFormat formatterLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = formatterLong.format(getValue());
		return formattedDate;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(DateFormateUtils.getDate(text));
	}

}
