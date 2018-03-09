package com.ictrade.tools.leadin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;

import com.ictrade.tools.FileUtils;
import com.ykyframework.exception.SystemException;

/**
 * 文件导入工具类，现在支持xls,xlsx,csv
 * @author tongkun@yikuyi.com
 * @version 1.0.0
 */
public class LeadInFactory {
	
	/**
	 * 工具类不需要构造方法
	 * @author tongkun@yikuyi.com
	 */
	private LeadInFactory(){}

	/**
	 * 读取
	 * @param leadInFile
	 * @return
	 */
	public static LeadInProcesser createProcess(File leadInFile){
		return createProcess(leadInFile,null,null);
	}

	/**
	 * 读取
	 * @param leadInFile
	 * @return
	 */
	public static LeadInProcesser createProcess(File leadInFile,String extra){
		return createProcess(leadInFile,extra,null);
	}
	
	/**
	 * 创建执行器（输入流方式，只支持xls和xlsx）
	 * @param is 输入流
	 * @param prefix 后缀名
	 * @param charset 字符编码
	 * @return
	 * @since 2017年11月3日
	 * @author tongkun@yikuyi.com
	 */
	public static LeadInProcesser createProcess(InputStream is,String prefix,Charset charset){
		LeadInProcesser result = null;
		prefix = prefix.toLowerCase();
		//xls
		if(LeadInProcesserXls.FILE_TYPE.equals(prefix)){
			result = new LeadInProcesserXls(is,String.valueOf(new Date().getTime()));
		}
		//xlsx
		else if(LeadInProcesserXlsx.FILE_TYPE.equals(prefix)){
			result = new LeadInProcesserXlsx(is,String.valueOf(new Date().getTime()));
		}
		return result;
	}
	
	/**
	 * 创建执行器
	 * @param leadInFile
	 * @since 2016年3月22日
	 * @author zr.tongkun@yikuyi.com
	 * @throws SystemException 
	 */
	public static LeadInProcesser createProcess(File leadInFile,String extra,Charset charset){
		LeadInProcesser result = null;
		//如果不是文件而是粘贴方式
		if(leadInFile==null && extra!=null){
			return new LeadInProcesserCopy(extra);
		}
		//如果是文件
		else if(leadInFile != null){
			String fileName = leadInFile.getName();//文件名
			String prefix = FileUtils.getPrefix(fileName);//后缀名
			prefix = prefix.toLowerCase();
			//csv类型
			if(LeadInProcesserCsv.FILE_TYPE.equals(prefix)){
				result = new LeadInProcesserCsv(leadInFile,charset);
			}
			//xls
			else if(LeadInProcesserXls.FILE_TYPE.equals(prefix)){
				try {
					result = new LeadInProcesserXls(leadInFile);
				} catch (FileNotFoundException e) {
					throw new SystemException("无法打开文件："+leadInFile.getAbsolutePath(),e);
				}
			}
			//xlsx
			else if(LeadInProcesserXlsx.FILE_TYPE.equals(prefix)){
				try {
					result = new LeadInProcesserXlsx(leadInFile);
				} catch (FileNotFoundException e) {
					throw new SystemException("无法打开文件："+leadInFile.getAbsolutePath(),e);
				}
			}
		}
		return result;
	}
}
