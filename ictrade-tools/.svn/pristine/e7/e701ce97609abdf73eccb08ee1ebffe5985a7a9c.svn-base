package com.ictrade.tools.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.Ostermiller.util.CSVPrint;
import com.Ostermiller.util.CSVPrinter;  
  
/**
 * csv文件写入  
 * @author injor.huang@yikuyi.com
 * @version 1.0.0
 */
public class CsvFilePrinter{     
    
    private CSVPrint csvPrint;  
  
    /**  
     *   
     * @param fileName 文件路径  
     * @param append 是否支持追加  
     * @throws IOException  
     */  
    public CsvFilePrinter(String fileName,boolean append) throws IOException {   
       this.execute(fileName, null,append);
    }
    
    /**
     * 默认追加写
     * @param fileName文件路径 
     * @param template 头部信息
     * @throws IOException
     */
    public CsvFilePrinter(String fileName,String[] template) throws IOException { 
    	this.execute(fileName,template, true);
    }
    
    /**
     *  默认不追加写
     * @param file
     * @param fileName文件路径
     * @param template头部信息
     * @throws IOException
     */
    public CsvFilePrinter(File file,String fileName,String[] template) throws IOException { 
    	this.execute(file,fileName,template, false);
    }
    
    public void execute(String fileName,String[] template,boolean append)throws IOException{
    	 File file = new File(fileName);  
    	// 写入bom头
         byte[] uft8bom={(byte)0xef,(byte)0xbb,(byte)0xbf};
    	 FileWriter fileWriter = new FileWriter(fileName,append);
    	 fileWriter.write(new String(uft8bom));
//         fileWriter.write("UTF-8");
         if(!file.exists()){ 
        	 // 写入bom头
             csvPrint = new CSVPrinter(fileWriter);  
             init(template);  
         }else{  
             csvPrint = new CSVPrinter(fileWriter);  
             if(!append){  
                 init(template);  
             }  
         }  
    }
    
    public void execute(File file,String fileName,String[] template,boolean append)throws IOException{
    	// 写入bom头
        byte[] uft8bom={(byte)0xef,(byte)0xbb,(byte)0xbf};
	   	 FileWriter fileWriter = new FileWriter(fileName,append);
	   	 fileWriter.write(new String(uft8bom));
        if(!file.exists()){
            csvPrint = new CSVPrinter(fileWriter);  
            init(template);  
        }else{  
            csvPrint = new CSVPrinter(fileWriter);  
            if(!append){  
                init(template);  
            }  
        }  
   }
    
      
    public void init(String[] template) throws IOException{
    	 write(template);
    }  
    
    public void write(List<String> values) throws IOException {    
        csvPrint.writeln((String[])values.toArray(new String[values.size()]));
    }  
     
    public void write(String[] values) throws IOException {
        csvPrint.writeln(values);  
    }     
}  