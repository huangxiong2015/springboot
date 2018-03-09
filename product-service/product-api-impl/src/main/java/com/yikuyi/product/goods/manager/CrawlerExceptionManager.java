package com.yikuyi.product.goods.manager;

import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.yikuyi.product.vo.RawData;

@Service
@Transactional
public class CrawlerExceptionManager {

	private static final String COLLECTION_NAME = "crawler_exception";
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	/**
	 * 插入错误信息到异常日志表
	 * @param data
	 * @param result
	 * @param spuId
	 * @param errorDesc
	 * @since 2017年8月14日
	 * @author injor.huang@yikuyi.com
	 */
	public void insertErrorlog(RawData data,String from,String spuId,String errorDesc){
		JSONObject jsonData = new JSONObject();
		jsonData.put("manufacturer", data.getManufacturer());
		jsonData.put("manufacturerPartNumber", data.getManufacturerPartNumber());
		jsonData.put("spuId", spuId);
		jsonData.put("from", from);
		if(CollectionUtils.isNotEmpty(data.getVendorCategories())){
			jsonData.put("categories", data.getVendorCategories());
		}
		jsonData.put("errorDesc", errorDesc);
		jsonData.put("createdTimeMillis", Long.toString(new Date().getTime()));
		mongoTemplate.save(jsonData, CrawlerExceptionManager.COLLECTION_NAME);
		
	}
	
}
