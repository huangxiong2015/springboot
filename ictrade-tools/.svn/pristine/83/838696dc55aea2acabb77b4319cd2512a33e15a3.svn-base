package com.ictrade;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.junit.Before;
import org.junit.Test;

import com.ictrade.tools.FileUtils;
import com.ictrade.tools.excel.ExcelCellData;
import com.ictrade.tools.export.ExportFactory;
import com.ictrade.tools.export.ExportProcesser;
import com.ictrade.tools.leadin.LeadInFactoryTest;

/**
 * 文件测试
 * @author tongkun
 */
public class FileUtilsTest{  
	
	@Test
	public void testExtractFirstFile() {
		File zipFile = new File(FileUtilsTest.class.getClassLoader().getResource("com/ictrade/tools/template.zip").getFile());
		FileUtils.extractFirstFile(zipFile.getParentFile().getAbsolutePath(), zipFile.getName(), false);
	}
}  