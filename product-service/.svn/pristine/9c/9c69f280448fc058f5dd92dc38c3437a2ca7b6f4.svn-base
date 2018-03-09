package com.yikuyi.product.goods.manager;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.party.supplier.SupplierVo;
import com.yikuyi.product.goods.manager.SearchAsyncManager.KeywordMatch;
import com.yikuyi.search.KeywordMatchVo;
import com.ykyframework.model.AbstractEntity;

@Service
public class KeyworkMatchManager {

	private static final Logger logger = LoggerFactory.getLogger(KeyworkMatchManager.class);

	@Autowired
	private SearchAsyncManager searchAsyncManager;

	/**
	 * 根据关键字做意图识别,并且替换原有的的keyword,cat,manufacturer,vendorId
	 * 
	 * @param keyword
	 * @param cat
	 * @param manufacturer
	 * @param vendorId
	 * @return
	 */
	public KeywordMatchVo keywordMatch(String keyword, String cat, String manufacturer, String vendorId) {
		KeywordMatchVo rstVo = new KeywordMatchVo();
		if (StringUtils.isEmpty(keyword)) {
			return rstVo;
		}
		Future<AbstractEntity> catFuture = searchAsyncManager.keywordMatch(StringUtils.isNotEmpty(cat) ? null : keyword, KeywordMatch.CAT);
		Future<AbstractEntity> brandFuture = searchAsyncManager.keywordMatch(StringUtils.isNotEmpty(manufacturer) ? null : keyword, KeywordMatch.BRAND);
		Future<AbstractEntity> supplierFuture = searchAsyncManager.keywordMatch(StringUtils.isNotEmpty(vendorId) ? null : keyword, KeywordMatch.SUPPLIER);

		getFutureResult(keyword, 1000l, TimeUnit.MILLISECONDS, brandFuture, KeywordMatch.BRAND).ifPresent(v -> rstVo.setBrand((ProductBrand) v));
		if(null != rstVo.getBrand()){
			return rstVo;
		}
		getFutureResult(keyword, 1000l, TimeUnit.MILLISECONDS, catFuture, KeywordMatch.CAT).ifPresent(v -> rstVo.setCat((ProductCategory) v));
		if(null != rstVo.getCat()){
			return rstVo;
		}
		getFutureResult(keyword, 1000l, TimeUnit.MILLISECONDS, supplierFuture, KeywordMatch.SUPPLIER).ifPresent(v -> rstVo.setSupplier((SupplierVo) v));
		if (null == rstVo.getBrand() && null == rstVo.getCat() && null == rstVo.getSupplier()) {
			rstVo.setKeyword(keyword);
		}
		return rstVo;
	}

	/**
	 * 获取异步查询结果
	 * 
	 * @param keyword
	 * @param timeout
	 * @param unit
	 * @param future
	 * @return
	 * @since 2018年2月7日
	 * @author jik.shu@yikuyi.com
	 */
	private Optional<AbstractEntity> getFutureResult(String keyword, long timeout, TimeUnit unit, Future<? extends AbstractEntity> future, KeywordMatch kType) {
		try {
			long l1 = System.currentTimeMillis();
			AbstractEntity temp = future.get(timeout, unit);
			logger.debug("{}-查询耗时:{},{}", kType, keyword, System.currentTimeMillis() - l1);
			return Optional.ofNullable(temp);
		} catch (Exception e) {
			logger.error("{}-异步意图识别异常:{},{}", kType, keyword, e.getMessage(), e);
			future.cancel(true);
		}
		return Optional.empty();
	}
}
