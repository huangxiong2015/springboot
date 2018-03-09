package com.ictrade.tools.export;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 导出工厂类
 * @author zr.tongkun@yikuyi.com
 * @version 1.0.0
 */
public class ExportFactory {

	public static final String TYPE_XLS = "xls";
	
	/**
	 * 
	 * @author tongkun@yikuyi.com
	 */
	private ExportFactory(){}
	
	/**
	 * 获取导出执行器
	 * @param type 类型
	 * @param os 输出流
	 * @return
	 * @since 2016年6月29日
	 * @author zr.tongkun@yikuyi.com
	 */
	public static ExportProcesser getProcesser(String type,OutputStream os){
		return getProcesser(type,os,(File)null);
	}

	/**
	 * 获取导出执行器
	 * @param type 类型
	 * @param os 输出流
	 * @param templateFilePath 模板的url
	 * @return
	 * @since 2017年1月18日
	 * @author zr.tongkun@yikuyi.com
	 */
	public static ExportProcesser getProcesser(String type,OutputStream os,File templateFile){
		if(ExportProcesserXls.FILE_TYPE.equals(type))
			return new ExportProcesserXls(os,templateFile);
		else if(ExportProcesserCsv.FILE_TYPE.equals(type))
			return new ExportProcesserCsv(os);
		return null;
	}

	/**
	 * 获取导出执行器
	 * @param type 类型
	 * @param os 输出流
	 * @param templateStream 模板的输入流
	 * @return
	 * @since 2017年2月21日
	 * @author liudian@yikuyi.com
	 */
	public static ExportProcesser getProcesser(String type,OutputStream os,InputStream templateStream){
		if(ExportProcesserXls.FILE_TYPE.equals(type))
			return new ExportProcesserXls(os, templateStream);
		return null;
	}
	
	/**
	 * 获取导出执行器
	 * @param type 类型
	 * @param os
	 * @return
	 * @since 2017年8月21日
	 * @author injor.huang@yikuyi.com
	 */
	public static ExportProcesser getProcesserByOutputStream(String type,OutputStream os){
		if(ExportProcesserXls.FILE_TYPE.equals(type))
			return new ExportProcesserXls(os);
		return null;
	}
}
