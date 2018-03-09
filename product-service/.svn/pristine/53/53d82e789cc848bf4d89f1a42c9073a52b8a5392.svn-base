package com.yikuyi.product.goods.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.yikuyi.activity.model.Activity;
import com.yikuyi.product.model.Product;
import com.yikuyi.product.vo.ProductVo;
import com.yikuyi.rule.price.PriceInfo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class PriceQueryAsyncManager {

	@Autowired
	private PriceQueryManager priceQueryManager;
	
	@Autowired
	private JedisPool jedisPool;

	@Async
	public Future<Void> asyncSetPrices(List<PriceInfo> priceinfos, ProductVo productVo,List<Activity> actList) {
		PriceInfo priceInfo = new PriceInfo();
		// 1.计算市场价
		List<ProductVo> productVoList = new ArrayList<>();
		productVoList.add(productVo);
		List<PriceInfo> originalResalePriceInfo = priceQueryManager.queryPriceByEntities(productVoList, false);
		if (!CollectionUtils.isEmpty(originalResalePriceInfo)) {
			priceInfo = originalResalePriceInfo.get(0);
			// 将原市场价放入备份字段中
			priceInfo.setOriginalResalePrices(priceInfo.getResalePrices());
		}
		priceinfos.add(priceInfo);
		// 设置价格信息
		priceQueryManager.setPriceInfoByCache(productVo, priceInfo,actList);
		return new AsyncResult<>(null);
	}
	
	@Async
	public Future<Void> asyncSetPricesV2(List<PriceInfo> priceinfos, ProductVo productVo) {
		PriceInfo priceInfo = new PriceInfo();
		// 1.计算市场价
		List<ProductVo> productVoList = new ArrayList<>();
		productVoList.add(productVo);
		List<PriceInfo> originalResalePriceInfo = priceQueryManager.queryPriceByEntities(productVoList, false);
		if (!CollectionUtils.isEmpty(originalResalePriceInfo)) {
			priceInfo = originalResalePriceInfo.get(0);
			// 将原市场价放入备份字段中
			priceInfo.setOriginalResalePrices(priceInfo.getResalePrices());
		}
		priceinfos.add(priceInfo);
		// 设置价格信息
		priceQueryManager.setPriceInfoByCacheV2(productVo, priceInfo);
		return new AsyncResult<>(null);
	}
	

	
	/**
	 * 异步清理价格缓存
	 * @param product
	 */
	@Async
	public void evictProductPrice(Collection<Product> product){
		if(CollectionUtils.isEmpty(product)){
			return;
		}
		String[] keys = product.stream().filter(Objects::nonNull).filter(v->StringUtils.isNotEmpty(v.getId())).map(v-> "productPriceCache:ProductPriceCache-" + v.getId() ).collect(Collectors.toList()).toArray(new String[]{});
		try(Jedis jedis = jedisPool.getResource()) {
			jedis.del(keys);
		}
	}
}