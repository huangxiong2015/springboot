package com.ictrade.tools.leadin;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.ictrade.tools.excel.Excel2003Reader;
import com.ictrade.tools.excel.IRowReader;
import com.ykyframework.exception.SystemException;

/**
 * 导入执行器（csv）
 * @author zr.tongkun@yikuyi.com
 * @version 1.0.0
 */
public class LeadInProcesserXlsSax implements LeadInProcesserSax,IRowReader{
	/**
	 * 转换的文件类型
	 */
	public static final String FILE_TYPE = "xls";
	
	/**
	 * 导入文件
	 */
	private File leadInFile;
	
	/**
	 * 业务处理对象
	 */
	private IRowReader reader;
	
	/**
	 * 当前所读取的行（sheet内）
	 */
	private int nowReadRows = 0;
	
	/**
	 * 文件最大的行数
	 */
	private long maxRows = 0L;
	
	/**
	 * 构造方法
	 * @param leadInFile
	 * @throws SystemException 
	 */
	public LeadInProcesserXlsSax(IRowReader reader,File leadInFile) throws SystemException{
		this.leadInFile = leadInFile;
		this.reader = reader;
        try {
            Excel2003Reader excel03 = new Excel2003Reader();  
            excel03.setRowReader(this);  
            excel03.process(this.leadInFile.getAbsolutePath());  
		} catch (IOException e) {
			throw new SystemException("excel文件无法读取:"+leadInFile.getName(),e);
		} catch (Exception e) {
			throw new SystemException("创建读取流发生异常:"+leadInFile.getName(),e);
		}
	}

	@Override
	public long getReadLength() {
		return nowReadRows;
	}

	@Override
	public long getMaxLength() {
		return maxRows;
	}

	@Override
	public void getRows(int sheetIndex , String sheetName, int curRow, List<String> rowlist) {
		nowReadRows = curRow;
		maxRows = curRow;
		reader.getRows(sheetIndex, sheetName ,curRow, rowlist);
	}
	
}
