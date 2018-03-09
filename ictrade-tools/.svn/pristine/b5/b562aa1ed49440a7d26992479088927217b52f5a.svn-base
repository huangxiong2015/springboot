package com.ictrade.tools.export;

import java.io.Closeable;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import com.ictrade.tools.excel.ExcelCellData;

/**
 * 文件导出执行器
 * @author zr.tongkun@yikuyi.com
 * @version 1.0.0
 */
public interface ExportProcesser extends Closeable{
	
	/**
	 * 关闭输出流
	 * @param os
	 * @since 2016年6月29日
	 * @author zr.tongkun@yikuyi.com
	 */
	@Override
	public void close();
	
	/**
	 * 写一行数据
	 * @param sheet
	 * @param datas
	 * @since 2016年6月29日
	 * @author zr.tongkun@yikuyi.com
	 */
	public void writeLine(String sheet,List<String> datas);
	
	/**
	 * 写一行数据
	 * @param sheet
	 * @param datas
	 * @since 2016年11月16日
	 * @author zr.tongkun@yikuyi.com
	 */
	public void writeLine(String sheet,String[] datas);
	
	/**
	 * 写一行数据（复制上一行样式）
	 * @param sheet
	 * @param datas
	 * @since 2017年1月18日
	 * @author zr.tongkun@yikuyi.com
	 */
	public void writeLine(String sheet,String[] datas,boolean copyStyle);
	
	/**
	 * 写一行数据（带样式）
	 * @param sheet
	 * @param datas
	 * @since 2017年1月18日
	 * @author tongkun@yikuyi.com
	 */
	public void writeLine(String sheet,ExcelCellData[] datas);
	
	/**
	 * 写一行数据（复制上一行样式）
	 * @param sheet
	 * @param datas
	 * @since 2017年1月18日
	 * @author zr.tongkun@yikuyi.com
	 */
	public void writeLine(String sheet,ExcelCellData[] datas,boolean copyStyle);
	
	/**
	 * 输出到输出流
	 * @since 2016年6月29日
	 * @author zr.tongkun@yikuyi.com
	 */
	public void output();
	
	/**
	 * 单元格样式
	 * @return
	 * @since 2017年1月19日
	 * @author tongkun@yikuyi.com
	 */
	public CellStyle createStyle();
	
	/**
	 * 单元格字体
	 * @return
	 * @since 2017年1月19日
	 * @author tongkun@yikuyi.com
	 */
	public Font createFont();
}
