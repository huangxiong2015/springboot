package com.ictrade.tools.leadin;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.ictrade.tools.excel.IRowReader;

public class LeadInFactoryTest{  
	
	@Test
	public void testExportWithoutStyle() {
		File importFile = new File(LeadInFactoryTest.class.getClassLoader().getResource("com/ictrade/tools/leadin/with0.xlsx").getFile());
		LeadInProcesser leadin =LeadInFactory.createProcess(importFile);
		String[] datas;
		while((datas=leadin.getNext())!=null){
			System.out.println(datas);
		}
		leadin.close();
	}

	@Test
	public void testExportWithoutStyle2() {
		File importFile = new File(LeadInFactoryTest.class.getClassLoader().getResource("com/ictrade/tools/leadin/with0.xlsx").getFile());
		LeadInProcesserSax leadin =LeadInFactorySax.createProcess(new IRowReader(){

			@Override
			public void getRows(int sheetIndex, String sheetName, int curRow, List<String> rowlist) {
				System.out.println(rowlist);
			}
			
		}, importFile, null);
	}
}  