package com.ictrade.tools.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ictrade.tools.excel.ExcelCellData;
import com.ictrade.tools.excel.ExcelCopy;

/**
 * 导入执行器（csv）
 * @author zr.tongkun@yikuyi.com
 * @version 1.0.0
 */
public class ExportProcesserXls implements ExportProcesser{
	private static final Logger logger = LoggerFactory.getLogger(ExportProcesserXls.class);
	/**
	 * 转换的文件类型
	 */
	public static final String FILE_TYPE = "xls";
	
	/**
	 * 模板读取
	 */
	public InputStream templateInput = null;
	
	/**
	 * 模板
	 */
	public HSSFWorkbook template = null;
	
	/**
	 * 文档对象
	 */
	private HSSFWorkbook wb = null;
	
	/**
	 * 输出流
	 */
	private OutputStream os = null;
	
	/**
	 * 根据sheet存储当前输出的最大行数
	 */
	private Map<String,Integer> rowNumbers = null;
	
	/**
	 * 根据sheet存储模板当前读取的最大行数
	 */
	private Map<String,Integer> templateRowNumbers = null;
	
	private Map<String,Map<Integer,CellRangeAddress>> regions = null;
	
	/**
	 * sheet集合
	 */
	private Map<String,HSSFSheet> sheets = null; 
	
	/**
	 * 构造方法，不需要模板
	 * @param os
	 */
	public ExportProcesserXls(OutputStream os){
		init(os, null);
	}
	
	/**
	 * 构造方法，需要模板
	 * @param os
	 * @param templatePath
	 */
	public ExportProcesserXls(OutputStream os,File tempateFile){
		InputStream tempInput = null;
		try {			
			if(tempateFile!=null){
				tempInput = new FileInputStream(tempateFile);
			}
			init(os, tempInput);
		}
		catch (FileNotFoundException exp) {
			logger.error("FileNotFoundException",exp);
		}
	}
	
	/**
	 * 构造方法，需要模板
	 * @param os
	 * @param templatePath
	 */
	public ExportProcesserXls(OutputStream os,InputStream tempateStream){
		init(os, tempateStream);
	}
	
	private void init(OutputStream os,InputStream tempateStream) {
		this.wb = new HSSFWorkbook();
		this.os = os;
		sheets = new HashMap<>();
		rowNumbers = new HashMap<>();
		try {
			if(tempateStream != null){
				templateInput = tempateStream;
				this.template = new HSSFWorkbook(templateInput);
			}else{
				this.template = new HSSFWorkbook();
				//创建一个sheet
				this.template.createSheet();
			}
			templateRowNumbers = new HashMap<>();
			this.regions = new HashMap<>();
			//加载所有的合并单元格
			int sheetCount = this.template.getNumberOfSheets();
			//合并单元格组成map,格式为：sheet名:行号:合并单元格
			for (int i = 0; i < sheetCount; i++) {  
				HSSFSheet sheet = this.template.getSheetAt(i);
				int sheetMergerCount = sheet.getNumMergedRegions(); 
				Map<Integer,CellRangeAddress> regions = this.regions.get(sheet.getSheetName());
				if(regions==null){
					regions = new HashMap<>();
					this.regions.put(sheet.getSheetName(), regions);
				}
				for (int j = 0; j < sheetMergerCount; j++) {  
					CellRangeAddress cra = sheet.getMergedRegion(j);  
					regions.put(cra.getFirstRow(), cra);
				}  
			}  
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException",e);
		} catch (IOException e) {
			logger.error("IOException",e);
		}
	}
	
	@Override
	public void writeLine(String sheetName, List<String> datas) {
		writeLine(sheetName,datas.toArray(new String[0]));
	}

	@Override
	public void writeLine(String sheetName, String[] datas) {
		writeLine(sheetName,datas,false);
	}

	@Override
	public void writeLine(String sheetName, String[] datas, boolean copyStyle) {
		ExcelCellData[] cellDatas = new ExcelCellData[datas.length];
		for(int i = 0;i < datas.length;i++){
			cellDatas[i] = new ExcelCellData(datas[i],null);
		}
		writeLine(sheetName,cellDatas,copyStyle);
	}

	@Override
	public void writeLine(String sheetName, ExcelCellData[] datas) {
		writeLine(sheetName,datas,false);
	}

	@Override
	public void writeLine(String sheetName, ExcelCellData[] datas, boolean copyStyle) {
		HSSFSheet sheet = sheets.get(sheetName);
		Integer rowNumber = rowNumbers.get(sheetName);
		if(sheet==null){
			sheet = wb.createSheet(sheetName);
			sheets.put(sheetName, sheet);
			//设置宽度
			HSSFSheet templdateSheet = null == template ? null : template.getSheet(sheetName);
			if(templdateSheet!=null){
				int maxColumn = templdateSheet.getRow(0).getPhysicalNumberOfCells();
				for(int i = 0;i < maxColumn;i++){
					sheet.setColumnWidth(i, templdateSheet.getColumnWidth(i));
				}
			}
			rowNumber = 0;
		}else{
			rowNumber = rowNumber+1;
		}
		HSSFRow row = sheet.createRow(rowNumber);
		HSSFRow templateRow = getTemplateRow(sheetName,copyStyle);
		//如果有模板行，则要复制合并区域
		if(templateRow!=null){
			Map<Integer,CellRangeAddress> regionMap = regions.get(sheetName);
			CellRangeAddress region = regionMap.get(this.templateRowNumbers.get(sheetName)-1);
			if(region!=null){
				CellRangeAddress newRegion = new CellRangeAddress(rowNumber,rowNumber+(region.getLastRow()-region.getFirstRow()),region.getFirstColumn(),region.getLastColumn());
				sheet.addMergedRegion(newRegion);
			}
			row.setHeight(templateRow.getHeight());
		}
		//合并模板和数据
		for(int i = 0;i < datas.length;i++){
			ExcelCellData data = datas[i];
			int colNum = 1;
			try{
				colNum = row.getLastCellNum();
			}catch(Exception e){
				logger.error(e.getMessage(),e);
			}
			HSSFCell cell = row.createCell(colNum==-1?0:colNum,HSSFCell.CELL_TYPE_STRING);
			HSSFCell templateCell = null;
			try{
				if(templateRow!=null)
					templateCell = templateRow.getCell(i);
			}catch(Exception e){
				logger.error(e.getMessage(),e);
			}
			String str = data==null?null:data.getData();
			CellStyle style = data==null?null:data.getStyle();
			//有模板的时候要应用模板的内容
			if(templateCell!=null){
				
				if(style==null)
					style = ExcelCopy.cloneCellStyle(template, wb, templateCell.getCellStyle());
				if(StringUtils.isBlank(str)){
					int type = templateCell.getCellType();
					switch(type){
						case HSSFCell.CELL_TYPE_NUMERIC:{
							str = Double.toString(templateCell.getNumericCellValue());
							break;
						}
						case HSSFCell.CELL_TYPE_BLANK:{
							str = "";
							break;
						}
						case HSSFCell.CELL_TYPE_BOOLEAN:{
							str = Boolean.toString(templateCell.getBooleanCellValue());
							break;
						}
						case HSSFCell.CELL_TYPE_FORMULA:{
							str = templateCell.getCellFormula();
							break;
						}
						default:{
							str = templateCell.getStringCellValue();
						}
					}
				}
			}
			cell.setCellValue(str);
			if(style!=null){
				cell.setCellStyle(style);
			}
		}
		rowNumbers.put(sheetName, rowNumber);
	}
	
	/**
	 * 获取当前读取到的模板行
	 * @param sheetName 要读取样式的sheet名
	 * @param copyStyle true表示读取上一行模板样式，false表示读取当前行模板样式
	 * @return
	 * @since 2017年1月19日
	 * @author tongkun@yikuyi.com
	 */
	public HSSFRow getTemplateRow(String sheetName,boolean copyStyle){
		if(this.template==null||this.templateRowNumbers==null)
			return null;
		Integer rownumber = this.templateRowNumbers.get(sheetName);
		if(rownumber==null)
			rownumber = 0;
		//如果要复制上一行模板样式，则返回上一行
		if(copyStyle)
			rownumber = rownumber-1;
		HSSFSheet sheet = this.template.getSheet(sheetName);
		if(sheet==null)
			return null;
		HSSFRow row = null;
		try{
			row = sheet.getRow(rownumber);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		this.templateRowNumbers.put(sheetName, ++rownumber);
		return row;
	}

	@Override
	public void close() {
		try{
			wb.close();
		}
		catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		try{
			os.close();
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		try{
			if(templateInput!=null){
				templateInput.close();
			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
	}

	@Override
	public void output() {
		try {
			wb.write(os);
		} catch (IOException e) {
			logger.error("IOException",e);
		}
	}

	@Override
	public CellStyle createStyle() {
		return wb.createCellStyle();
	}

	@Override
	public Font createFont() {
		return wb.createFont();
	}
}