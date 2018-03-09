package com.yikuyi.product.goods.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.product.goods.IPriceQueryResouceV2;
import com.yikuyi.product.goods.manager.PriceQueryManager;
import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.rule.price.PriceInfo;

@RestController
@RequestMapping("v2/products/batch")
public class PriceQueryResouceV2 implements IPriceQueryResouceV2{

	
	@Autowired
	private PriceQueryManager priceQueryManager;
	
	@Override
    @RequestMapping(value="/price", method=RequestMethod.POST)
	public List<PriceInfo> queryPrice(@RequestBody List<ProductVo> productVos) throws Exception {
		//return priceQueryManager.queryPriceNew(productVos,false);
		return priceQueryManager.queryPriceWithActivity(productVos);
	}
	
	/**
	 * 秒杀专场-价格查询
	 */
    @RequestMapping(value="/seckill/price", method=RequestMethod.POST)
	public List<PriceInfo> queryPriceForSeckill(@RequestBody List<ProductVo> productVos){
		return priceQueryManager.queryPriceForSeckill(productVos);
	}
	
	@RequestMapping(value="/noCachePrice", method=RequestMethod.POST)
	public List<PriceInfo> queryPriceNoCache(@RequestBody List<ProductVo> productVos){
		return priceQueryManager.queryPriceNoCache(productVos);
	}
	
}
