/*
 * Created: 2017年9月7日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ictrade.tools.export;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * CSV操作(导出和导入)
 * @author injor.huang@yikuyi.com
 * @version 1.0.0
 */
public class CSVUtils {
	
    /**
     * 导出
     * 
     * @param file csv文件(路径+文件名)，csv文件不存在会自动创建
     * @param dataList 数据
     * @return
     */
    public static boolean exportCsv(File file, List<List<String>> dataList){
        boolean isSucess=false;
        String LINE_SEPARATOR = "\n";
        CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator(LINE_SEPARATOR);
        FileWriter out = null;
        CSVPrinter printer = null;
        try {
        	byte[] uft8bom={(byte)0xef,(byte)0xbb,(byte)0xbf};
        	out = new FileWriter(file);
       		out.write(new String(uft8bom));
            printer = new CSVPrinter(out, csvFormat);
            if(dataList!=null && !dataList.isEmpty()){
            	printer.printRecords(dataList);
            }
            isSucess=true;
        } catch (Exception e) {
            isSucess=false;
        }finally{
        	IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(printer);
        }
        return isSucess;
    }
    
    /**
     * 导入
     * 
     * @param file csv文件(路径+文件)
     * @return
     */
    public static List<String> importCsv(File file){
        List<String> dataList=new ArrayList<String>();
        BufferedReader br=null;
        try { 
            br = new BufferedReader(new FileReader(file));
            String line = ""; 
            while ((line = br.readLine()) != null) { 
                dataList.add(line);
            }
        }catch (Exception e) {
        }finally{
        	IOUtils.closeQuietly(br);
        }
 
        return dataList;
    }
}