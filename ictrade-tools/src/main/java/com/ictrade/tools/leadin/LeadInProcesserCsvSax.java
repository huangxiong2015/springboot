package com.ictrade.tools.leadin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ictrade.tools.excel.IRowReader;
import com.ykyframework.exception.SystemException;

/**
 * 导入执行器（csv）
 * @author zr.tongkun@yikuyi.com
 * @version 1.0.0
 */
public class LeadInProcesserCsvSax implements LeadInProcesserSax{
	private static final Logger logger = LoggerFactory.getLogger(LeadInProcesserCsvSax.class);
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
	 * 业务读取
	 */
	private IRowReader reader;
	
	/**
	 * 输入流
	 */
	private CSVParser parser;
	private Iterator<CSVRecord> it;
	
	/**
	 * 当前所读取的比特数
	 */
	private long nowReadRows = 0L;
	
	/**
	 * 文件最大的比特数
	 */
	private long maxReadRows = 0L;
	
	/**
	 * 构造方法
	 * @param leadInFile
	 * @throws SystemException 
	 */
	public LeadInProcesserCsvSax(IRowReader reader,File leadInFile) throws SystemException{
		this.leadInFile = leadInFile;
		this.reader = reader;
		try {
			parser = CSVParser.parse(this.leadInFile, CHARSET, CSVFormat.EXCEL);
			it = parser.iterator();
			process();
		} catch (IOException e) {
			throw new SystemException("读取失败",e);
		}finally {
			close();
		}
	}
	
	/**
	 * 构造方法
	 * @param reader
	 * @param csvInputStream
	 * @throws SystemException
	 * @throws IOException 
	 */
	public LeadInProcesserCsvSax(IRowReader reader, InputStream csvInputStream) throws SystemException, IOException{
		this.reader = reader;
		try (InputStreamReader r = new InputStreamReader(csvInputStream)) {
			parser = new CSVParser(r, CSVFormat.EXCEL);
			it = parser.iterator();
			process();
		} catch (Exception e) {
			throw new SystemException("读取失败", e);
		} finally {
			close();
		}
	}

	/**
	 * 关闭流
	 * @since 2016年4月22日
	 * @author zr.tongkun@yikuyi.com
	 */
	public void close() {
		try {
			parser.close();
		} catch (IOException e) {
			logger.error("无法关闭流"+this.leadInFile.getAbsolutePath(),e);
		}
	}

	@Override
	public long getReadLength() {
		return nowReadRows;
	}

	@Override
	public long getMaxLength() {
		return maxReadRows;
	}

	/**
	 * 执行
	 * @since 2016年4月22日
	 * @author zr.tongkun@yikuyi.com
	 */
	private void process(){
		List<String> row = null;
		while(it.hasNext()){
			CSVRecord record = it.next();
			row = new ArrayList<String>();
			//读取
			int recordNumber = record.size();
			for(int i = 0;i < recordNumber;i++){
				row.add(record.get(i));
			}
			reader.getRows(0, "0", (int)nowReadRows, row);
			nowReadRows++;
		}
		reader.getRows(0, "0", (int)nowReadRows, null);
		maxReadRows = nowReadRows;
		this.close();
	}
}
