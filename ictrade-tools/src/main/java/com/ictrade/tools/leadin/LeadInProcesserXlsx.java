package com.ictrade.tools.leadin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.DecimalFormat;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ictrade.tools.excel.ExcelCellData;
import com.ykyframework.exception.SystemException;

/**
 * 导入执行器（csv）
 * @author zr.tongkun@yikuyi.com
 * @version 1.0.0
 */
public class LeadInProcesserXlsx implements LeadInProcesser{
	private static final Logger logger = LoggerFactory.getLogger(LeadInProcesserXlsx.class);
	/**
	 * 转换的文件类型
	 */
	public static final String FILE_TYPE = "xlsx";
	
	/**
	 * 默认读取类型
	 */
	public static final Charset CHARSET = Charset.forName("gbk");
	
	/**
	 * 导入文件
	 */
	private File leadInFile;
	
	/**
	 * 输入流
	 */
	private XSSFWorkbook xssfWorkbook = null;
	private XSSFSheet xssfSheet = null;
	
	private String sheetName = null;
	
	/**
	 * 当前读取的行（全sheet）
	 */
	private long nowReadRowOfAll = 0;
	
	/**
	 * 当前所读取的行（sheet内）
	 */
	private int nowReadRows = 0;
	
	/**
	 * 现在读取的sheet数
	 */
	private int nowReadSheetNumber = 0;
	
	/**
	 * 文件最大的行数
	 */
	private long maxRows = 0L;

	/**
	 * 构造方法
	 * @param inputFile
	 * @throws FileNotFoundException
	 */
	public LeadInProcesserXlsx(File inputFile) throws FileNotFoundException{
		this(new FileInputStream(inputFile),inputFile.getName());
		this.leadInFile = inputFile;
	}
	/**
	 * 构造方法
	 * @param leadInFile
	 * @throws SystemException 
	 */
	public LeadInProcesserXlsx(InputStream is,String fileName) throws SystemException{
		this.leadInFile = new File(fileName);
		try {
			xssfWorkbook = new XSSFWorkbook(is);
			//计算总行数
			for(int  i = 0;i <xssfWorkbook.getNumberOfSheets();i++){
				XSSFSheet sheet = xssfWorkbook.getSheetAt(i);
				sheetName = sheet.getSheetName();
				maxRows += sheet.getLastRowNum();
			}
		} catch (IOException e) {
			throw new SystemException("excel文件无法读取:"+leadInFile.getName(),e);
		} catch (Exception e) {
			throw new SystemException("创建读取流发生异常:"+leadInFile.getName(),e);
		}
	}

	@Override
	public String[] getNext() {
		//如果需要加载sheet并且后面还有新的sheet，则加载新的sheet
		if(xssfSheet == null && nowReadSheetNumber<xssfWorkbook.getNumberOfSheets()){
			xssfSheet = xssfWorkbook.getSheetAt(nowReadSheetNumber);
			sheetName = xssfSheet.getSheetName();
		}
		//如果已经没有可以处理的sheet了，则结束读取，返回null
		if(xssfSheet == null){
			return null;
		}
		XSSFRow row = xssfSheet.getRow(nowReadRows);
		if(row==null)return null;
		int maxCellNum = row.getLastCellNum();
		String[] params = new String[maxCellNum];
		for(int i = 0;i < maxCellNum;i++){
			XSSFCell cell = row.getCell(i);
			params[i] = getValue2007(cell);
		}
		nowReadRows++;
		nowReadRowOfAll++;
		//如果已经过了最后一行，则到下一个sheet
		if(nowReadRows>xssfSheet.getLastRowNum()){
			xssfSheet = null;
			nowReadRows = 0;
			nowReadSheetNumber++;
		}
		return params;
	}
	
	/**
	 * 根据不同类型的值进行转换
	 * @param hssfCell
	 * @return
	 * @since 2016年3月30日
	 * @author zr.tongkun@yikuyi.com
	 */
	private String getValue2007(XSSFCell hssfCell) {
		if(hssfCell==null) return "";
		if (hssfCell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			DecimalFormat decimalFormat = new DecimalFormat("###################.#####");
			return String.valueOf(decimalFormat.format(hssfCell.getNumericCellValue()));
		} else if (hssfCell.getCellType() == XSSFCell.CELL_TYPE_FORMULA){
			String value = "";
			try {
				value = hssfCell.getStringCellValue();
			} catch (Exception e) {
				value = String.valueOf(hssfCell.getNumericCellValue());
			}
			return value;
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}

	@Override
	public void close() {
		try {
			xssfWorkbook.close();
		} catch (IOException e) {
			logger.error("无法关闭流"+this.leadInFile.getAbsolutePath(),e);
		}
	}

	@Override
	public long getReadLength() {
		return nowReadRowOfAll;
	}

	@Override
	public long getMaxLength() {
		return maxRows;
	}

	@Override
	public long getNowReadRowLength() {
		return 1;
	}

	@Override
	public ExcelCellData[] getNextWithStyle() {
		//如果需要加载sheet并且后面还有新的sheet，则加载新的sheet
		if(xssfSheet == null && nowReadSheetNumber<xssfWorkbook.getNumberOfSheets()){
			xssfSheet = xssfWorkbook.getSheetAt(nowReadSheetNumber);
		}
		//如果已经没有可以处理的sheet了，则结束读取，返回null
		if(xssfSheet == null){
			return null;
		}
		XSSFRow row = xssfSheet.getRow(nowReadRows);
		if(row==null){
			return null;
		}
		int maxCellNum = row.getLastCellNum();
		ExcelCellData[] params = new ExcelCellData[maxCellNum];
		for(int i = 0;i < maxCellNum;i++){
			XSSFCell cell = row.getCell(i);
			if(cell == null){
				params[i] = null;
			}else{
				params[i] = new ExcelCellData(getValue2007(cell),cell.getCellStyle());
			}
		}
		nowReadRows++;
		nowReadRowOfAll++;
		//如果已经过了最后一行，则到下一个sheet
		if(nowReadRows>xssfSheet.getLastRowNum()){
			xssfSheet = null;
			nowReadRows = 0;
			nowReadSheetNumber++;
		}
		return params;
	}
	@Override
	public String getSheetName() {
		return sheetName;
	}

}
