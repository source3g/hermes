package com.source3g.hermes.api.config;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

public class CustomDateDeserializer extends JsonDeserializer<Date> {
	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String unformatedDate = jp.getText();
		SimpleDateFormat formatterLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formatterShort = new SimpleDateFormat("yyyy-MM-dd");
		Date retVal = null;
		try {
			if (unformatedDate.length() == 19) {
				retVal = formatterLong.parse(unformatedDate);
			} else if (unformatedDate.length() == 10) {
				retVal = formatterShort.parse(unformatedDate);
			}
		} catch (Exception e) {
			return null;
		}
		return retVal;
	}
}