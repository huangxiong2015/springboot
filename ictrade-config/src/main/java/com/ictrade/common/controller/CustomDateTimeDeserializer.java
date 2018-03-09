package com.ictrade.common.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Jackson时间反序列化，格式：yyyy-MM-dd HH:mm:ss
 * 
 * @author zr.xuheng@yikuyi.com
 * @version 1.0.0
 */
public class CustomDateTimeDeserializer extends JsonDeserializer<Date> {

	String[] parterns = new String[] {
			"yyyy-MM-dd HH:mm:ss", 
			"yyyy/MM/dd HH:mm:ss",
			"yyyy-MM-dd",
			"yyyy/MM/dd",
			DateFormatUtils.ISO_DATETIME_FORMAT.getPattern(),
			DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern(),
			DateFormatUtils.SMTP_DATETIME_FORMAT.getPattern() };

	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
		Date date = null;
		try {
			date = DateUtils.parseDate(jp.getText(), parterns);
		} catch (ParseException e) {
			throw new IOException(e);
		}
		return date;
	}

}