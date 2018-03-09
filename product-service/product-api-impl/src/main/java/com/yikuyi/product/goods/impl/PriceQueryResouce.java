package com.yikuyi.product.goods.impl;

import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.yikuyi.product.goods.IPriceQueryResouce;
import com.yikuyi.product.goods.manager.PriceQueryManager;
import com.yikuyi.product.model.Product;
import com.yikuyi.rule.price.PriceInfo;

@RestController
@RequestMapping("v1/products/batch")
public class PriceQueryResouce implements IPriceQueryResouce{

	
	@Autowired
	private PriceQueryManager priceQueryManager;
	
	@Override
    @RequestMapping(value="/price", method=RequestMethod.POST)
	@Produces({MediaType.APPLICATION_JSON})
	public List<PriceInfo> queryPrice(@RequestBody List<String> ids) throws Exception {
		return priceQueryManager.queryPriceWithActivityInfo(ids);
	}
	
	@Override
	@RequestMapping(value="/priceTemplate", method=RequestMethod.GET)
	@Produces({MediaType.APPLICATION_JSON})
	public JSONArray queryPriceTemplate(@RequestParam(name="ids",required=true) List<String> ids) {
		return priceQueryManager.queryPriceTemplate(ids);
	}
	
	@RequestMapping(value="/priceTemplate", method=RequestMethod.PUT)
	@Produces({MediaType.APPLICATION_JSON})
	public JSONArray queryPriceTemplate(@RequestBody Product product) {
		return priceQueryManager.queryPriceTemplate(product);
	}
	
}
