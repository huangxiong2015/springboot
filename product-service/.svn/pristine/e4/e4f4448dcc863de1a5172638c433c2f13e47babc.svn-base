/*
 * Created: 2017年5月9日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.document.bll;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.yikuyi.material.vo.MaterialVo;
import com.yikuyi.material.vo.MaterialVo.MaterialVoType;
import com.yikuyi.product.goods.dao.ProductRepository;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.vo.ProductVo;


/**
 * 同步spu和sku业务处理类
 * @author zr.wanghong
 * @version 1.0.0
 * @since 2017年5月9日
 */
@Service
public class SynProductPropertiesManager {

	private static final Logger logger = LoggerFactory.getLogger(SynProductPropertiesManager.class);
		
	@Autowired
	private ProductRepository productRepository;
	
	
	/**
	 * 同步产品的属性数据，并同步到搜索引擎
	 * 
	 * 1.调用syncAllSpuCategoryAndBrand和syncAllSkuSpu
	 *   全量遍历所有物料，找到所有与现有品牌、分类不一致的数据，将其更新成一致
	 *   全量遍历所有商品，找到对应的物料，更新它的物料信息。没有物料的，更新品牌信息
	 * 2.根据processId查询得到已更新的批次数据
	 * 3.将批次数据同步搜索引擎
	 * @since 2017年5月9日
	 * @author zr.wanghong
	 */
	public void synProductProperties(String processId){
		//String processId = UUID.randomUUID().toString();
		/*ScriptOperations scriptOps = template.scriptOps(); 
		DB db = template.getDb(); 
		//1.调用存储过程
		String spucolName = "product_stand";
		String pccolName = "product_category";
		String pbcolName = "product_brand";
		
		logger.info("开始更新SPU分类和品牌数据!批次号：{}",processId);
		long startTime = new Date().getTime();
		//Double updateNumber = (Double) scriptOps.call("syncAllSpuCategoryAndBrand", processId, spucolName, pccolName, pbcolName);
		//String script = "syncAllSpuCategoryAndBrand('"+processId+"', '"+spucolName+"', '"+pccolName+"', '"+pbcolName+"')";
		//Double updateNumber = (Double)db.eval(script);
		String script = "function(processId, spucolName, pccolName, pbcolName){"
				+ "return syncAllSpuCategoryAndBrand(processId, spucolName, pccolName, pbcolName)}";
		ExecutableMongoScript mongoScript = new ExecutableMongoScript(script);  
		Double updateNumber = (Double)scriptOps.execute(mongoScript,processId, spucolName, pccolName, pbcolName);
		long endTime = new Date().getTime();
		logger.info("更新SPU分类和品牌成功的记录数：{}条!用时:{}毫秒",updateNumber,endTime-startTime);
		
		String skucalName = "product";
		logger.info("开始更新SKU中SPU信息!");
		startTime = new Date().getTime();
		scriptOps.call("syncAllSkuSpu", processId,skucalName,spucolName,pbcolName);
		endTime = new Date().getTime();
		logger.info("更新SKU中SPU信息成功!用时:{}毫秒",endTime-startTime);*/
		
		
		//2.根据processId查询得到已更新的数据，并分批处理
		//分页查询ID
		
		String lastId = StringUtils.EMPTY;
		//已完成同步记录数
		int processedNum = 0;
		
		logger.info("开始分批查询已更新的SKU数据并同步搜索引擎!当前批次号：{}",processId);
		while (true) {
			PageRequest pageRequest = new PageRequest(0,1000);
			List<Product> products = productRepository.findByProcessId(processId,lastId,pageRequest);
			if(CollectionUtils.isEmpty(products)){
				logger.info("已无数据需要同步搜索引擎!");
				break;
			}
			
			Product lastProduct = products.get(products.size()-1);
			lastId = lastProduct.getId();
			
			
			//将products对象转换成ProductVo,并过滤掉非标准的商品
			List<ProductVo> sendList = new ArrayList<>();
			products.stream().forEach( product -> {
				ProductStand spu = product.getSpu();
				Integer status = product.getStatus();
				if(spu != null && StringUtils.isNotEmpty(spu.getId())
						&&status!=null && status.equals(1)){
					sendList.add(new ProductVo(product));
				}
				
			});
			
			if(!CollectionUtils.isEmpty(sendList)){
				processedNum += sendList.size();
				//3.同步搜索引擎
				MaterialVo materialVo = new MaterialVo();
				materialVo.setType(MaterialVoType.UPDATE_DATA);
				materialVo.setMsg(sendList);
				//syncElasticsearchProductManager.syncElasticsearchProductUpdate(materialVo);
				logger.info("同步SKU到搜索引擎成功!已完成同步 ：{}条!当前批次号：{}",processedNum,processId);
			}
		}
	
	}
	
}
