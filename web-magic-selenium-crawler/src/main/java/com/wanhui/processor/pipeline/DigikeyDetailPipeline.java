package com.wanhui.processor.pipeline;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 对解析好的数据进行持久化
 * @author hx
 *
 */
@Component
public class DigikeyDetailPipeline implements Pipeline{
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public void process(ResultItems resultItems, Task arg1) {
		JSONObject digikeyJSON = new JSONObject();

		String manufacturer = resultItems.get("manufacturer");
		String manufacturerPartNumber = resultItems.get("manufacturerPartNumber");
		// vendor自己的分类
		JSONArray vendorCategories = resultItems.get("vendorCategories");
		Integer quantity = resultItems.get("quantity");//库存
		
		//详情url
		String vendorDetailsLink = resultItems.get("vendorDetailsLink");
		// 价格
		JSONArray price = resultItems.get("prices");
		digikeyJSON.put("manufacturer", manufacturer);
		digikeyJSON.put("manufacturerPartNumber", manufacturerPartNumber);
		digikeyJSON.put("prices", price);
		digikeyJSON.put("vendorCategories", vendorCategories);
		digikeyJSON.put("quantity", quantity);
		digikeyJSON.put("vendorName", "digikey");
		digikeyJSON.put("vendorId", "digikey");
		digikeyJSON.put("vendorDetailsLink", vendorDetailsLink);
		digikeyJSON.put("createdTimeMillis", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));

//		mongoTemplate.save(digikeyJSON,"product_injor_test");
	}

}
