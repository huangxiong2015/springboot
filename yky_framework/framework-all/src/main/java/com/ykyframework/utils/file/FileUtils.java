/*
 * Created: 2016年3月31日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ykyframework.utils.file;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import com.ykyframework.utils.SpringContextUtils;
import com.ykyframework.utils.file.impl.FileOperationForLocal;
import com.ykyframework.utils.file.inter.FileOperationInter;

public final class FileUtils {

	public final static String MODE_LOCAL = "LOCAL"; // 本地存储
	public final static String MODE_ALIYUN_OSS = "ALIYUN.OSS"; // OOS存储

	// 文件操作Spring Bean 名称的前缀,相关文件操作的bean必须以如下前缀加存储方式名称
	public final static String OPERATION_BEAN_PREFIX = "uitls.file.";

	private FileUtils() {
		FileOperationInter fileOperationInter = null;

		fileOperationInter = (FileOperationInter) SpringContextUtils.getBean(OPERATION_BEAN_PREFIX + MODE_LOCAL);
		/*
		 * 如果Spring bean中未定义本地存储的实现 ,那么默认采用FileOperationForLocal进行本地文件
		 * 操作，直接创建本地文件操作实现
		 */
		if (fileOperationInter == null) {
			fileOperationInter = new FileOperationForLocal();
			String beanName = OPERATION_BEAN_PREFIX + MODE_LOCAL;
			utils.put(beanName, fileOperationInter);
		}
	};

	private final Map<String, FileOperationInter> utils = new HashMap<String, FileOperationInter>();

	private static class FileUtilsHandler {
		protected static final FileUtils handaler = new FileUtils();
	}

	public static FileUtils create() { // 初始化本身
		return FileUtilsHandler.handaler;
	}

	public FileOperationInter instance() {
		return instance(MODE_LOCAL);
	}

	public FileOperationInter instance(final String mode) {
		String beanName = OPERATION_BEAN_PREFIX + mode;
		if (create().utils.get(beanName) == null) {
			FileOperationInter fileOperationInter = (FileOperationInter) SpringContextUtils
					.getBean(OPERATION_BEAN_PREFIX + mode);
			create().utils.put(beanName, fileOperationInter);
		}
		return utils.get(beanName);
	}

	public byte[] convertInputStreamToBytes(final InputStream in) throws IOException {
		final ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		try {
			byte[] buff = new byte[100];
			int rc = 0;
			while ((rc = in.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
			byte[] in2b = swapStream.toByteArray();
			return in2b;
		} finally {
			// 关闭文件流并返回
			IOUtils.closeQuietly(swapStream);
			IOUtils.closeQuietly(in);
		}
	}

	public String convertInputStreamToString(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

		} finally {
			IOUtils.closeQuietly(in);
		}
		return sb.toString();
	}

	public String convertInputStreamToString(InputStream in, String charset) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

		} finally {
			IOUtils.closeQuietly(in);
		}
		return sb.toString();
	}

	/**
	 * 将文件流写入本地存储
	 * 
	 * @param in
	 * @param targertPath
	 * @since 2016年3月31日
	 * @author liaoke@yikuyi.com
	 * @throws IOException
	 */
	public void writeInputStreamToFile(InputStream in, final String targertPath) throws IOException {
		FileOutputStream swapStream = null;
		try {
			swapStream = new FileOutputStream(targertPath);
			byte[] buff = new byte[100];
			int rc = 0;
			while ((rc = in.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
		} finally {
			// 关闭文件流并返回
			IOUtils.closeQuietly(swapStream);
			IOUtils.closeQuietly(in);
		}
	}

	public void writeStringToFile(String in, final String targertPath) throws IOException {
		writeBytesToFile(in.getBytes(), targertPath);
	}

	public void writeBytesToFile(byte[] in, final String targertPath) throws IOException {
		OutputStream fileWritter = null;
		try {
			fileWritter = new FileOutputStream(targertPath);
			fileWritter.write(in);
		} finally {
			// 关闭文件流并返回
//			try {
//				fileWritter.close();
//			} catch (Exception e) {
//			}
			IOUtils.closeQuietly(fileWritter);
		}
	}

	public String getFilePath(String path, String filename) {
		File pathFile = new File(path);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}

		if (path.lastIndexOf(File.separator) == (path.length() - 1)) {
			return (path + filename);
		} else {
			return (path + File.separator + filename);
		}
	}

	/**
	 * 删除目录，请小心操作
	 * 
	 * @param path
	 * @return
	 * @since 2016年3月31日
	 * @author longyou@yikuyi.com
	 */
	public boolean deleteDir(String path) {
		if (!new File(path).isDirectory())
			return false;
		File[] files = new File(path).listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				deleteDir(file.getAbsolutePath());
				file.delete();
			} else {
				if (!file.delete()) {
					return false;
				}
			}
		}
		return new File(path).delete();
	}

	public String getRootPath(String path) {
		int index = path.replace("\\", "/").indexOf("/");
		if (index == 0) {
			index = path.replace("\\", "/").indexOf("/", 1);
			return path.substring(1, index);
		} else if (index > 0) {
			return path.substring(0, index);
		} else {
			return path;
		}
	}

	/**
	 * UUID的生成长度
	 * 
	 * @param length
	 * @return
	 * @since 2016年4月7日
	 * @author liaoke@yikuyi.com
	 */
	public String generateUUID(int length) {
		String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
				"q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
				"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z" };
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		int roopTimes = Double.valueOf(Math.ceil(length/8.0)).intValue()  ;
		int count=1;
		for(int j=0;j<roopTimes;j++){
			
			for (int i = 0; i < 8; i++) {
				String str = uuid.substring(i * 4, i * 4 + 4);
				int x = Integer.parseInt(str, 16);
				shortBuffer.append(chars[x % 0x3E]);
				if(count==length) break;
				count++;
			}
		}
		return shortBuffer.toString();
	}

	public String generateShortUUID() {
		return generateUUID(8);
	}

}
