package com.ictrade.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件相关的公共方法
 * 
 * @author zr.tongkun@yikuyi.com
 * @version 1.0.0
 */
public class FileUtils {
	private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * 工具类不需要显示的public构造函数
	 * 
	 * @author tongkun@yikuyi.com
	 */
	private FileUtils() {
	}

	/**
	 * 获取文件后缀名
	 * 
	 * @param fileName
	 * @return
	 * @since 2016年3月22日
	 * @author zr.tongkun@yikuyi.com
	 */
	public static String getPrefix(String fileName) {
		if (fileName == null)
			return "";
		int index = fileName.lastIndexOf('.');
		if (index <= 0) {
			return "";
		} else {
			return fileName.substring(index + 1).trim().toLowerCase();// 后缀名
		}
	}

	/**
	 * 单个文件上传
	 * @param is
	 * @param fileName
	 * @param filePath
	 */
	public static File upFile(InputStream is, String fileName, String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		File f = new File(filePath + "/" + fileName);
		try(BufferedInputStream bis = new BufferedInputStream(is);
				FileOutputStream fos = new FileOutputStream(f);
				BufferedOutputStream bos = new BufferedOutputStream(fos);) {
			byte[] bt = new byte[4096];
			int len;
			while ((len = bis.read(bt)) > 0) {
				bos.write(bt, 0, len);
			}
			bos.flush();
		} catch (Exception e) {
			logger.error("write upload file to server fail,fileName is:{},message:"+e.getMessage() ,fileName, e);
		}
		return f;
	}
	

	
	/**
	 * 解压缩文件(只解压第一个文件)只支持文件名为gbk的压缩包
	 * @param fileName 文件名
	 * @param deleteFlag 解压后是否删除文件
	 * @return 
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public static String extractFirstFile(String path,String fileName,boolean deleteFlag){
		String filePath = path+File.separator+fileName;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		String outputFileName = null;
		try (ZipFile zipFile = new ZipFile (filePath,Charset.forName("gbk"))){
			ZipEntry entry = null;
			Enumeration<ZipEntry> enties = (Enumeration<ZipEntry>)zipFile.entries();
			while(!(entry = enties.nextElement()).isDirectory()){
				bis = new BufferedInputStream (zipFile.getInputStream ( entry ) ) ;
				outputFileName = Calendar.getInstance().getTimeInMillis()+("."+getPrefix(entry.getName()));
				fos = new FileOutputStream(path+File.separator+outputFileName);
				pipelineStream(bis,fos);
				break;
			}
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		} finally{
			if(bis!=null){
				try{
					bis.close();
				}catch(Exception e){
					logger.error(e.getMessage(),e);
				}
			}
			if(fos!=null){
				try{
					fos.close();
				}catch(Exception e){
					logger.error(e.getMessage(),e);
				}
			}
		}
		//如果解压后要删除文件
		if(deleteFlag&&!new File(filePath).delete()){
			logger.info(filePath+" 临时文件删除失败");
		}
		return outputFileName;
	}
	
	/**
	 * 管道传输，将输入流的内容全部输出到输出流中
	 * @param is 输入流
	 * @param os 输出流
	 * @throws IOException 
	 */
	public static void pipelineStream(InputStream is,OutputStream os) throws IOException{
		byte [ ] b = new byte [ 100 ] ;
		while ( true ) {
			int len = is.read ( b ) ;
			if ( len == - 1 )
				break ;
			os.write ( b , 0 , len ) ;
		}
	}
}
