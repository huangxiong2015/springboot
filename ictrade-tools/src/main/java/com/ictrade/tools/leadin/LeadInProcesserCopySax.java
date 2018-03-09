package com.ictrade.tools.leadin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ictrade.tools.excel.IRowReader;
import com.ykyframework.exception.SystemException;

/**
 * 导入执行器（excel拷贝）
 * @author zr.tongkun@yikuyi.com
 * @version 1.0.0
 */
public class LeadInProcesserCopySax implements LeadInProcesserSax{
	/**
	 * 转换的文件类型
	 */
	public static final String FILE_TYPE = "copy";
	
	/**
	 * 字段分割符
	 */
	public static final String SPLIT_SYMBOL = "\t"; 
	/**
	 * 导入内容
	 */
	private String[] content;
	
	/**
	 * 业务读取
	 */
	private IRowReader reader;
	
	private int nowReadLine = 0;
	
	
	/**
	 * 构造方法（通过内容）
	 * @param leadInFile
	 */
	public LeadInProcesserCopySax(IRowReader reader,String copyContent){
		this.content = copyContent.split(System.lineSeparator());
		this.reader = reader;
		process();
	}
	
	
	/**
	 * 构造方法（通过文件）
	 * @param leadInFile
	 * @throws SystemException 
	 */
	public LeadInProcesserCopySax(IRowReader reader,File copyFile){
		StringBuilder result = new StringBuilder();
		byte[] bytes = new byte[1024];
		int length = 0;
		try(FileInputStream fis = new FileInputStream(copyFile)) {
			while((length = fis.read(bytes))>0){
				result.append(new String(bytes,0,length));
			}
		} catch (FileNotFoundException e) {
			throw new SystemException("excel粘贴的文件找不到",e);
		} catch (IOException e) {
			throw new SystemException("读取excel粘贴文件发生错误",e);
		}
		this.content = result.toString().split(System.lineSeparator());
		this.reader = reader;
		process();
	}

	@Override
	public long getReadLength() {
		return nowReadLine;
	}


	@Override
	public long getMaxLength() {
		return content.length;
	}

	/**
	 * 执行
	 * @since 2016年4月22日
	 * @author zr.tongkun@yikuyi.com
	 */
	private void process(){
		for(nowReadLine = 0;nowReadLine < this.content.length;nowReadLine++){
			String[] params = content[nowReadLine].split(SPLIT_SYMBOL);//取得一行的所有数据
			List<String> list = new ArrayList<>();
			//转换成数组
			for(int i = 0;i < params.length;i++){
				list.add(params[i]);
			}
			//调用业务方法
			reader.getRows(0, "0", nowReadLine, list);
		}
	}
}
