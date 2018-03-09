/*
 * Created: 2017年3月28日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.common.utils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;  

/**
 * 禁止json转换时，时间转化为对象 
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
public class JsonDateValueProcessor implements JsonValueProcessor{
	
	private String format ="yyyy-MM-dd HH:mm:ss";  
	   
	public JsonDateValueProcessor() {  
        super();  
    }  
    public JsonDateValueProcessor(String format) {  
        super();  
        this.format = format;  
    }  
    
    @Override
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {  
        return process(value);  
    }
    
    @Override
    public Object processObjectValue(String key, Object value,  
            JsonConfig jsonConfig) {  
        return process(value);  
    }  
    private Object process(Object value) {  
        try {  
            if (value instanceof Date) {  
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.UK);  
                return sdf.format((Date) value);  
            }  
            return value == null ? "" : value.toString();  
        } catch (Exception e) {  
            return "";  
        }  
    }  
    public String getDatePattern() {  
        return format;  
    }  
    public void setDatePattern(String pDatePattern) {  
    	format = pDatePattern;  
    } 	     
}
