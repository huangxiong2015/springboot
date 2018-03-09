package com.ictrade.tools.leadin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.util.IOUtils;

import com.ictrade.tools.excel.ExcelCellData;
import com.ykyframework.exception.SystemException;

/**
 * 导入执行器（excel拷贝）
 * @author zr.tongkun@yikuyi.com
 * @version 1.0.0
 */
public class LeadInProcesserCopy implements LeadInProcesser{
	public static final String splitSymbol = "\t"; 
	/**
	 * 导入内容
	 */
	private String[] content;
	
	private int nowReadLine = 1;
	
	
	/**
	 * 构造方法
	 * @param leadInFile
	 */
	public LeadInProcesserCopy(String copyContent){
		this.content = copyContent.split(System.lineSeparator());
	}
	
	
	/**
	 * 构造方法
	 * @param leadInFile
	 * @throws SystemException 
	 */
	public LeadInProcesserCopy(File copyFile) throws SystemException{
		FileInputStream fis = null;
		StringBuffer result = new StringBuffer();
		byte[] bytes = new byte[1024];
		int length = 0;
		try {
			fis = new FileInputStream(copyFile);
			while((length = fis.read(bytes))>0){
				result.append(new String(bytes,0,length));
			}
		} catch (FileNotFoundException e) {
			throw new SystemException("excel粘贴的文件找不到",e);
		} catch (IOException e) {
			throw new SystemException("读取excel粘贴文件发生错误",e);
		} finally{
			IOUtils.closeQuietly(fis);
		}
		this.content = result.toString().split(System.lineSeparator());
	}


	@Override
	public String[] getNext() {
		String[] params = null;
		if(nowReadLine<=content.length){
			params = content[nowReadLine-1].split(splitSymbol);
		}
		nowReadLine++;
		return params;
	}

	@Override
	public void close() {}


	@Override
	public long getReadLength() {
		return nowReadLine;
	}


	@Override
	public long getMaxLength() {
		return content.length;
	}


	@Override
	public long getNowReadRowLength() {
		return 1;
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
