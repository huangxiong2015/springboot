package com.yikuyi.product.goods.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.yikuyi.product.goods.IProductDetailCrawlerResource;
import com.ykyframework.mqservice.sender.MsgSender;

import io.swagger.annotations.ApiParam;

/**
 * 商品详情页爬虫更新服务
 * @author zr.wanghong
 * @version 1.0.0
 * @since 2017年6月12日
 */
@RestController
@RequestMapping("v1/crawler")
public class SingleDetailCrawlerResource implements IProductDetailCrawlerResource{
	@Autowired
	private MsgSender msgSender;
	
	@Value("${mqConsumeConfig.crawlerSyncProduct.topicName}")
	private String crawlerSyncProductTopicName;
	
	/**
	 * 发送MQ消息，根据skuId或商品ID爬取单条商品最新数据
	 */
	@Override
	@RequestMapping(value = "/detail", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public JSONObject run(@ApiParam("skuId或商品ID") @RequestParam("param") String param){
		StringBuilder stringBuilder = new StringBuilder();
		if(StringUtils.contains(param, "?")){
			stringBuilder.append(param).append("&yds=m");
		}else{
			stringBuilder.append(param).append("?yds=m");
		}
		msgSender.sendMsg(crawlerSyncProductTopicName, stringBuilder.toString(), null);
		JSONObject json  = new JSONObject();
		json.put("message", "数据更新成功!");
		return json;
	}
	
}
