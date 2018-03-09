package com.ictrade.tools.leadin;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ictrade.tools.excel.ExcelCellData;
import com.ykyframework.exception.SystemException;

/**
 * 导入执行器（csv）
 * @author zr.tongkun@yikuyi.com
 * @version 1.0.0
 */
public class LeadInProcesserCsv implements LeadInProcesser{
	private static final Logger logger = LoggerFactory.getLogger(LeadInProcesserCsv.class);
	/**
	 * 转换的文件类型
	 */
	public static final String FILE_TYPE = "csv";
	
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
	private CSVParser parser;
	private Iterator<CSVRecord> it;
	
	/**
	 * 当前所读取的比特数
	 */
	private long nowReadBits = 0L;
	
	/**
	 * 文件最大的比特数
	 */
	private long maxReadBits = 0L;
	
	/**
	 * 当前正在读取的行的比特数
	 */
	private long nowReadRowBits = 0L;
	
	/**
	 * 构造方法
	 * @param leadInFile
	 * @throws SystemException 
	 */
	public LeadInProcesserCsv(File leadInFile) throws SystemException{
		this(leadInFile,CHARSET);
	}

	/**
	 * 构造方法
	 * @param leadInFile
	 * @param charset
	 * @throws SystemException
	 */
	public LeadInProcesserCsv(File leadInFile,Charset charset) throws SystemException{
		this.leadInFile = leadInFile;
		Charset useCharset = charset;
		if(charset==null){
			useCharset = CHARSET;
		}
		maxReadBits = this.leadInFile.length();
		try {
			parser = CSVParser.parse(this.leadInFile, useCharset, CSVFormat.DEFAULT);
			it = parser.iterator();
		} catch (IOException e) {
			throw new SystemException("读取失败",e);
		}
	}

	@Override
	public String[] getNext() {
		if(!it.hasNext())return null;//如果没有了则返回空
		CSVRecord record = it.next();
		long nowReadBitsTemp = record.getCharacterPosition();//当前读取的字符位置
		nowReadRowBits = nowReadBitsTemp - nowReadBits;//先算出本行的长度
		nowReadBits = nowReadBitsTemp;//将当前读取的行换位置到全局变量
		int recordNumber = record.size();
		String[] params = new String[recordNumber];
		for(int i = 0;i < recordNumber;i++){
			params[i] = record.get(i);
			//去掉首尾的"
			if(params[i]!=null&&params[i].length()>1&&params[i].charAt(1)=='"'&&params[i].charAt(params[i].length()-1)=='"'){
				params[i] = params[i].substring(2, params[i].length()-1);
			}
		}
		return params;
	}

	@Override
	public void close() {
		try {
			parser.close();
		} catch (IOException e) {
			logger.error("无法关闭流"+this.leadInFile.getAbsolutePath(),e);
		}
	}

	@Override
	public long getReadLength() {
		return nowReadBits;
	}

	@Override
	public long getMaxLength() {
		return maxReadBits;
	}

	@Override
	public long getNowReadRowLength() {
		return nowReadRowBits;
	}

	@Override
	public ExcelCellData[] getNextWithStyle() {
		return null;
	}

	@Override
	public String getSheetName() {
		return null;
	}

}