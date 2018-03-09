/*
 * Created: 2017年6月9日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.activity.bll;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import com.ictrade.tools.excel.IRowReader;
import com.yikuyi.template.model.ProductTemplate;

/**
 * 商品上传业务处理类
 * 
 * @author tb.lijing@yikuyi.com
 * @version 1.0.0
 */
public class ActivityProductReader implements IRowReader {

	private ProductTemplate emTemplate;
	
	private int uploadLimitCount;

	/**
	 * 读取时的错误信息
	 */
	private String errorMsg = null;

	private List<String[]> datas2 = new ArrayList<>();

	private int rowNum = 0;
	
	/* 当前sheet最多行数  */
	private int maxRowNum = 0;
	
	/**
	 * 初始化方法
	 * @param emTemplate  上传模版
	 * @param index   上传数量限制，如果为0，不做限制
	 */
	public ActivityProductReader(ProductTemplate emTemplate, int index) {
		this.emTemplate = emTemplate;
		this.uploadLimitCount = index;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public List<String[]> getDatas() {
		return datas2;
	}

	@Override
	public void getRows(int sheetIndex, String sheetName, int curRow, List<String> rowlist) {
		if(uploadLimitCount != 0 && uploadLimitCount < maxRowNum){
			errorMsg = "文件上传总数必须小于"+uploadLimitCount+"限制";
			return;
		}
		boolean isBlank = true;
		for (String s : rowlist) {
			if (s != null && !"".equals(s.trim())) {// 当整行数据都为空时，跳过
				isBlank = false;
				break;
			}
		}
		if (isBlank){
			return;
		}
		if (curRow == 0 && rowlist != null && emTemplate != null) {
			List<String> tempTitle = rowlist.stream().map(v->v.trim().replaceAll("\\*", "")).collect(Collectors.toList());
			if(!CollectionUtils.isEqualCollection(tempTitle, emTemplate.getTemplate().get("defaultTitle").keySet())){
				errorMsg = "模板内的标题不正确，请下载标准模板！";
				return;
			}
		}else if (curRow != 0 && rowlist != null && (rowNum < uploadLimitCount || uploadLimitCount == 0)) {// 当小于100条时，读取数据
			datas2.add(rowlist.toArray(new String[] {}));
			rowNum++;
		}
	}

	@Override
	public void setMaxNum(int sheetIndex, int num) {
		this.maxRowNum =  num;
	}
	
}