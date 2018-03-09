package com.ictrade.tools.leadin;

import com.ictrade.tools.excel.ExcelCellData;

/**
 * 库存导入执行器
 * @author zr.tongkun@yikuyi.com
 * @version 1.0.0
 */
public interface LeadInProcesser {
	
	/**
	 * 找到下一行数据
	 * 如果已经到达文件末尾，则返回null
	 * @since 2016年3月22日
	 * @author zr.tongkun@yikuyi.com
	 */
	public String[] getNext();
	
	/**
	 * 找到下一行数据，带样式
	 * 
	 * @return
	 * @since 2017年1月20日
	 * @author tongkun@yikuyi.com
	 */
	public ExcelCellData[] getNextWithStyle();
	
	/**
	 * 关闭连接
	 * 一定要执行，否则连接会一直保持
	 * @since 2016年3月22日
	 * @author zr.tongkun@yikuyi.com
	 */
	public void close();
	
	/**
	 * 获取当前已经读取的文件长度
	 * @return
	 * @since 2016年3月24日
	 * @author zr.tongkun@yikuyi.com
	 */
	public long getReadLength();
	
	/**
	 * 获取当前正在读取的行长度
	 * @return
	 * @since 2016年3月29日
	 * @author zr.tongkun@yikuyi.com
	 */
	public long getNowReadRowLength();
	
	/**
	 * 获取文件总长度
	 * @return
	 * @since 2016年3月24日
	 * @author zr.tongkun@yikuyi.com
	 */
	public long getMaxLength();
	
	public String getSheetName();
}