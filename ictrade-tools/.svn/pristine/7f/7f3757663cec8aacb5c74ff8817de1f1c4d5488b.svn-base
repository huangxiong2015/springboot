/*
 * Created: 2018年2月26日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2018 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ictrade.tools.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Ostermiller.util.CSVPrinter;
import com.ictrade.tools.excel.ExcelCellData;

public class ExportProcesserCsv implements ExportProcesser{
	private static final Logger logger = LoggerFactory.getLogger(ExportProcesserCsv.class);
	/**
	 * 转换的文件类型
	 */
	public static final String FILE_TYPE = "csv";
	
	/**
	 * 文件输出流
	 */
	private OutputStream os;
	/**
	 * csv文件输出对象
	 */
	private CSVPrinter csvPrint;

	public ExportProcesserCsv(OutputStream os){
        try {
        	this.os = os;
			os.write(new byte[]{(byte)0xef,(byte)0xbb,(byte)0xbf});// 写入bom头
			csvPrint = new CSVPrinter(os);//初始化输出对象  
		} catch (IOException e) {
			logger.error("获取csv输出流失败",e);
		}
	}
	
	@Override
	public void close() {
		try {
			os.close();
		} catch (IOException e) {
			logger.error("关闭csv文件输出流失败",e);
		}
		try {
			csvPrint.close();
		} catch (IOException e) {
			logger.error("关闭csv文件输出对象失败",e);
		}
	}

	@Override
	public void writeLine(String sheet, List<String> datas) {
		csvPrint.println(datas.toArray(new String[datas.size()]));
	}

	@Override
	public void writeLine(String sheet, String[] datas) {
		csvPrint.println(datas);
	}

	@Override
	@Deprecated
	public void writeLine(String sheet, String[] datas, boolean copyStyle) {
	}

	@Override
	@Deprecated
	public void writeLine(String sheet, ExcelCellData[] datas) {
	}

	@Override
	@Deprecated
	public void writeLine(String sheet, ExcelCellData[] datas, boolean copyStyle) {}

	@Override
	public void output() {
		try {
			csvPrint.flush();
		} catch (IOException e) {
			logger.error("csv内容清空失败",e);
		}
	}

	@Override
	@Deprecated
	public CellStyle createStyle() {
		return null;
	}

	@Override
	@Deprecated
	public Font createFont() {
		return null;
	}

}
