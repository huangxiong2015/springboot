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
package com.ykyframework.utils.file.inter;

import java.io.IOException;
import java.io.InputStream;

/** 文件操作接口
 * @author liaoke@yikuyi.com
 * @version 1.0.0
 */
public interface FileOperationInter {
	
	/**
	 * @param in 输入流
	 * @param target 写入路径
	 * @return 文件名称
	 * @throws Exception
	 * @since 2016年3月31日
	 * @author liaoke@yikuyi.com
	 */
	public String write(InputStream in,String targetPath) throws IOException;
	
	public String write(String in,String targetPath) throws IOException;
	
	public String write(byte[] in,String targetPath) throws IOException;

	/**
	 * @param filePath 文件路径（含文件名）
	 * @return
	 * @throws Exception
	 * @since 2016年3月31日
	 * @author liaoke@yikuyi.com
	 */
	public InputStream read(String filePath) throws IOException;
	
	
}
