package com.yikuyi.product.goods.manager;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ictrade.enterprisematerial.EsProductManager;
import com.ictrade.enterprisematerial.InventorySearchManager;
import com.ictrade.vo.EsResult;
import com.ictrade.vo.SkuSearchCondition;
import com.yikuyi.party.PartyClientBuilder;
import com.yikuyi.product.brand.manager.BrandManager;
import com.yikuyi.product.category.manager.CategoryManager;
import com.ykyframework.model.AbstractEntity;

@Service
public class SearchAsyncManager {

	private static final Logger logger = LoggerFactory.getLogger(SearchAsyncManager.class);

	@Autowired
	private InventorySearchManager inventorySearchManager;

	@Autowired
	private EsProductManager esProductManager;

	@Autowired
	private BrandManager brandManager;

	@Autowired
	private CategoryManager categoryManager;

	@Autowired
	private PartyClientBuilder partyClientBuilder;

	@Async
	public Future<JSONObject> searchProductInfo(JSONObject conJson) {
		JSONObject resultInfo = new JSONObject();
		try {
			long startTime = System.currentTimeMillis();
			resultInfo = inventorySearchManager.search(conJson);
			logger.info("查询商品信息完成，耗时:{}", System.currentTimeMillis() - startTime);
		} catch (Exception e) {
			logger.error("searchProductInfo error{},{}", e.getMessage(), e);
		}
		return new AsyncResult<>(resultInfo);
	}

	@Async
	public Future<EsResult<Document>> searchProductInfo(SkuSearchCondition condition, int from, int size) {
		EsResult<Document> esResult = null;
		try {
			long startTime = System.currentTimeMillis();
			esResult = esProductManager.searchByMpnMfr(condition, from, size);
			logger.info("查询商品信息完成，耗时:{}", System.currentTimeMillis() - startTime);
		} catch (Exception e) {
			logger.error("searchProductInfo error{},{}", e.getMessage(), e);
		}
		return new AsyncResult<>(esResult);
	}

	/**
	 * 根据关键字匹配标准分类
	 * 
	 * @param keyword
	 * @return
	 */
	@Async
	public Future<AbstractEntity> keywordMatch(String keyword, KeywordMatch type) {
		try {
			if (StringUtils.isEmpty(keyword)) {
				return new AsyncResult<>(null);
			}
			if (KeywordMatch.CAT.equals(type)) {
				return new AsyncResult<>(categoryManager.getCateByCateName(keyword));
			} else if (KeywordMatch.BRAND.equals(type)) {
				return new AsyncResult<>(Optional.ofNullable(brandManager.getBrandByAliasName(Arrays.asList(keyword.trim().toUpperCase()))).map(v -> v.get(keyword.trim().toUpperCase())).orElse(null));
			} else if (KeywordMatch.SUPPLIER.equals(type)) {
				return new AsyncResult<>(partyClientBuilder.supplierClient().getSupplierByName(keyword, null));
			} else {
				return new AsyncResult<>(null);
			}
		} catch (Exception e) {
			logger.error("keywordMatch type {} error:{}", type, e.getMessage(), e);
		}
		return new AsyncResult<>(null);
	}

	enum KeywordMatch {
		CAT, BRAND, SUPPLIER
	}
}
