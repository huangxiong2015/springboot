package com.ictrade.common.utils.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.junit.Before;
import org.junit.Test;

import com.ictrade.tools.excel.ExcelCellData;
import com.ictrade.tools.export.ExportFactory;
import com.ictrade.tools.export.ExportProcesser;

public class ExportProcesserXlsTest{  
	private String path = "/temp/";
	@Before
	public void init(){
		File file =	new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	
	/**
	 * 不带样式输出
	 * @since 2017年1月19日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testExportWithoutStyle() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path+"test1.xls");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ExportProcesser processer = ExportFactory.getProcesser(ExportFactory.TYPE_XLS, fos);
		//如果要输出空白单元格，对应的地方写null即可
		processer.writeLine("测试的sheet", new String[]{"第一行第一列","第一行第二列",null,"第一行第四列"});
		processer.writeLine("测试的sheet", new String[]{"第二行第一列",null,"第二行第三列","第二行第四列"});
		processer.output();
		processer.close();
	}

	
	/**
	 * 带样式输出
	 * @since 2017年1月19日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testExportWithStyle() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path+"test2.xls");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ExportProcesser processer = ExportFactory.getProcesser(ExportFactory.TYPE_XLS, fos);
		CellStyle cs = processer.createStyle();//获取样式
		cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//指定填充方式为单色
		cs.setFillForegroundColor(HSSFColor.RED.index);//设置背景颜色。HSSFColor是2003版本的类。如果是2007以上版本，要用XSSFColor。其它类同理。
		ExcelCellData cellA1 = new ExcelCellData("第一行第一列",cs);
		cs = processer.createStyle();
		cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//指定填充方式为单色
		cs.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);//设置背景颜色。
		Font font = processer.createFont();//创建字体
	    font.setFontName("黑体");//字体类型
	    font.setFontHeightInPoints((short)15);//字号 
	    font.setColor(HSSFColor.RED.index); //字体颜色
	    cs.setFont(font);//塞进去
	    //其它更多样式的设置方式请上网查找
		ExcelCellData cellA3 = new ExcelCellData("第一行第三列",cs);
		//如果要输出空白单元格，对应的地方写null即可
		processer.writeLine("测试的sheet", new ExcelCellData[]{cellA1,null,cellA3});
		processer.output();
		processer.close();
	}
	
	/**
	 * 带模板输出
	 * @since 2017年1月19日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testExportWithTemplate() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path+"test3.xls");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		File tempFile = new File(ExportProcesserXlsTest.class.getClassLoader().getResource("com/ictrade/common/utils/export/template.xls").getFile());
		ExportProcesser processer = ExportFactory.getProcesser(ExportFactory.TYPE_XLS, fos,tempFile);
		//如果要输出空白单元格，对应的地方写null即可
		//sheet名一定要和模板的sheet名对应好，这样才能读取到，效果请执行下看看就知道了
		processer.writeLine("样例的sheet", new ExcelCellData[]{null});//标题行
		processer.writeLine("样例的sheet", new ExcelCellData[]{null,new ExcelCellData("不想说",null),null,new ExcelCellData("那就别说了",null)});//填入行
		processer.writeLine("样例的sheet", new ExcelCellData[]{null,null,null,null});//标题，不需要输出
		processer.writeLine("样例的sheet", new ExcelCellData[]{new ExcelCellData("1",null),new ExcelCellData("MD1372DAS",null),new ExcelCellData("华为",null),new ExcelCellData("100",null)});//数据
		processer.writeLine("样例的sheet", new ExcelCellData[]{new ExcelCellData("2",null),new ExcelCellData("MD3271DAS",null),new ExcelCellData("小米",null),new ExcelCellData("300",null)},true);//最后一个参数为true表示样式需要和上一个相同，这样保证模板中一行的样式可以供多行使用
		processer.writeLine("样例的sheet", new ExcelCellData[]{null,new ExcelCellData("400",null)});//结果
		processer.output();
		processer.close();
	}
}  