/*
 * Created: 2017年4月1日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.material.bll;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.document.vo.DocumentLogVo;
import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.product.basicmaterial.bll.LeadBasicMaterialExcelReaderTest;
import com.yikuyi.product.brand.manager.BrandManager;
import com.yikuyi.product.template.bll.ProductTemplateManager;
import com.yikuyi.template.model.ProductTemplate;
import com.ykyframework.exception.SystemException;
import com.ykyframework.model.IdGen;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LeadMaterialExcelReaderTest {


	@Autowired
	ProductTemplateManager productTemplateManager;
	
	@Autowired
	private MaterialManager materialManager;
	
	@Autowired
	private BrandManager brandManager;
	/**
	 * 测试读取文件
	 * @param fileUrl
	 * @param emTemplate
	 * @throws SystemException
	 * @since 2017年4月1日
	 * @author tongkun@yikuyi.com
	 */
	@Test
	public void testReadByFile() {
		Map<String, Integer> quickFindKeyMap = new HashMap<>();// 初始化存储过滤重复Map
		Map<String, String> facilityMap = new HashMap<>();
		int minLineNo = 0;
		String isLastLine = "false";
		DocumentLogVo documentLogVo = new DocumentLogVo();
		documentLogVo.setCountCacheList(0);
		documentLogVo.setIsCancel(false);
		LeadMaterialExcelReader reader = new LeadMaterialExcelReader();
		ProductTemplate emTemplate = productTemplateManager.geTemplate("basisTemplateId");
		String filepath = LeadBasicMaterialExcelReaderTest.class.getClassLoader()
				.getResource("com/yikuyi/product/basicmaterial/bll/testexcel2007.xlsx").getFile();
		MaterialVo materialVo = new MaterialVo();
		materialVo.setDocId(String.valueOf(IdGen.getInstance().nextId()));
		Map<String, ProductBrand> brandMap = brandManager.getAliasBrandMap();// 品牌
		int dataSize = reader.readByFile(filepath, emTemplate, materialManager, materialVo, minLineNo, quickFindKeyMap, brandMap, facilityMap, documentLogVo, isLastLine);
		Assert.assertEquals(4, dataSize);
	}
}
