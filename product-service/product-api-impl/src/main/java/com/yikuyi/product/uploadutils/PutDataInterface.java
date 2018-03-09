/*
 * Created: 2017年2月22日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.uploadutils;

import com.yikuyi.product.basicmaterial.bll.LeadBasicMaterialParser;

@FunctionalInterface
public interface PutDataInterface {
	/**
	 * 这个方法主要是实现讲key,value参数存储在指定的doc中,不带返回参数()
	 * <p> args 为扩展参数,如果需要特殊参数,在调用处后面追加 {@link LeadBasicMaterialParser parseToRawData}
	 * <p>
	 * @param doc
	 * @param key
	 * @param value
	 * @param args
	 * @since 2017年1月12日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public void operation(Object obj , String key , String value, Object...args);
	
}