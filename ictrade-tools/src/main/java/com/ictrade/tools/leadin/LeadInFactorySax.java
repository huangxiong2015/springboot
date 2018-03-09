package com.ictrade.tools.leadin;

import java.io.File;
import java.io.InputStream;

import org.omg.CORBA.SystemException;

import com.ictrade.tools.FileUtils;
import com.ictrade.tools.excel.IRowReader;

public class LeadInFactorySax {
	
	/**
	 * 创建执行器
	 * @param leadInFile
	 * @since 2016年3月22日
	 * @author zr.tongkun@yikuyi.com
	 * @throws SystemException 
	 */
	public static LeadInProcesserSax createProcess(IRowReader reader,File leadInFile,String extra) throws SystemException{
		//如果不是文件而是粘贴方式
		if(leadInFile==null && extra!=null){
			return new LeadInProcesserCopySax(reader,extra);
		}
		//如果是文件
		else{
			if(leadInFile == null){
				return null;
			}
			String fileName = leadInFile.getName();//文件名
			String prefix = FileUtils.getPrefix(fileName);//后缀名
			prefix = prefix.toLowerCase();
			//csv类型
			if(LeadInProcesserCsvSax.FILE_TYPE.equals(prefix)){
				return new LeadInProcesserCsvSax(reader,leadInFile);
			}
			//xls
			else if(LeadInProcesserXlsSax.FILE_TYPE.equals(prefix)){
				return new LeadInProcesserXlsSax(reader,leadInFile);
			}
			//xlsx
			else if(LeadInProcesserXlsxSax.FILE_TYPE.equals(prefix)){
				return new LeadInProcesserXlsxSax(reader,leadInFile);
			}
			//excel粘贴
			else if(LeadInProcesserCopySax.FILE_TYPE.equals(prefix)){
				return new LeadInProcesserCopySax(reader,leadInFile);
			}
		}
		//没有合适的则返回空
		return null;
	}
	
	/**
	 * 增加csv文件流解析
	 * @param reader
	 * @param csvInputStream
	 * @return
	 * @throws com.ykyframework.exception.SystemException
	 * @since 2017年1月25日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public static LeadInProcesserSax createCsvStreamProcess(IRowReader reader,InputStream csvInputStream) throws com.ykyframework.exception.SystemException{
		if(csvInputStream == null){
			throw new com.ykyframework.exception.SystemException("csvInputStream is null");
		}
		try {
			return new LeadInProcesserCsvSax(reader, csvInputStream);
		} catch (Exception e) {
			throw new com.ykyframework.exception.SystemException(e.getMessage(), e);
		}
	}
}
