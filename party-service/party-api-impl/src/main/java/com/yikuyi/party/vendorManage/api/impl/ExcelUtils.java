/*
 * Created: 2017年2月21日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.vendorManage.api.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件上传
 * 
 * @author tb.huangqingfeng@yikuyi.com
 * @version 1.0.0
 */
public class ExcelUtils {

	private static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

	private static final int ROWNUM_END = 101;

	private static final int LINE_END = 3;

	public ExcelUtils() {
		super();
	}

	@SuppressWarnings("resource")
	public List<List<String>> readXlsIn(InputStream in) {
		try (InputStream is = in) {
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
			List<List<String>> result = new ArrayList<>();
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
			// 处理每行
			for (int rowNum = 0; rowNum <= ROWNUM_END; rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				List<String> rowList = new ArrayList<>();

				// 处理每行的每列
				for (int line = 0; line <= LINE_END; line++) {
					HSSFCell cell = hssfRow.getCell(line);
					rowList.add(ExcelUtils.getStringVal(cell));
				}
				result.add(rowList);
			}
			return result;
		} catch (IOException e) {
			logger.error("ExcelUtils*****readXlsIn*****异常", e);
		}
		return Collections.emptyList();
	}

	@SuppressWarnings("resource")
	public List<List<String>> readXlsxIn(InputStream in) {
		try (InputStream is = in) {
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
			List<List<String>> result = new ArrayList<>();
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
			// 处理每行
			for (int rowNum = 0; rowNum <= ROWNUM_END; rowNum++) {
				XSSFRow xssfrow = xssfSheet.getRow(rowNum);
				if(null == xssfrow){
					continue;
				}
				List<String> rowList = new ArrayList<>();
				// 处理每行的每列
				for (int line = 0; line <= LINE_END; line++) {
					rowList.add(getStringVal(xssfrow.getCell(line)));
				}
				result.add(rowList);
			}
			return result;
		} catch (IOException e) {
			logger.error("ExcelUtils*****readXlsxIn*****异常", e);
		}
		return Collections.emptyList();
	}

	public static String getStringVal(XSSFCell cell) {
		if (null == cell) {
			return StringUtils.EMPTY;
		}
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case Cell.CELL_TYPE_NUMERIC:
			cell.setCellType(Cell.CELL_TYPE_STRING);
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		default:
			return null;
		}
	}

	public static String getStringVal(HSSFCell cell) {
		if (null == cell) {
			return StringUtils.EMPTY;
		}
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case Cell.CELL_TYPE_NUMERIC:
			cell.setCellType(Cell.CELL_TYPE_STRING);
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		default:
			return null;
		}
	}

}