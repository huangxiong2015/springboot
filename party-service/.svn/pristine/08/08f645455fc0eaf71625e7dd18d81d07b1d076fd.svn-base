/*
 * Created: 2016年11月28日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.supplier.bll;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.yikuyi.info.InfoClientBuilder;
import com.yikuyi.news.model.News;
import com.yikuyi.party.supplier.SupplierMailVo;
import com.yikuyi.party.supplier.SupplierVo;
import com.yikuyi.party.supplier.dao.SupplierDao;
import com.ykyframework.mqservice.sender.MsgSender;

/**
 * 供应商业务处理类 <br>
 * (因为供应商数据频繁查询，所以大部分数据存储在jvm中) <br>
 * 1、项目启动通过监听加载所有供应商数据 <br>
 * 2、通过mq监听实时更新
 * 
 * @author jik.shu@yikuyi.com
 * @version 1.0.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SupplierManager {

	private Cache<String, SupplierVo> supplierSimpleCache = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.HOURS).build();

	@Autowired
	private InfoClientBuilder infoClientBuilder;

	@Autowired
	private MsgSender msgSender;

	@Value("${mqConsumeConfig.supplierCache.topicName}")
	private String supplierCacheTopmic;

	@Autowired
	private SupplierDao supplierDao;

	/**
	 * 查询供应商名称和ID的映射(jvm缓存5分钟)
	 * 
	 * @return
	 * @since 2018年1月18日
	 * @author jik.shu@yikuyi.com
	 * @throws ExecutionException
	 */
	public Map<String, String> getSupplierName() {
		return supplierSimpleCache.asMap().values().stream().collect(Collectors.toMap(SupplierVo::getSupplierName, SupplierVo::getSupplierId, (key1, key2) -> key2));
	}

	/**
	 * 根据供应商显示名称查询供应商
	 * 
	 * @return
	 * @since 2018年1月18日
	 * @author jik.shu@yikuyi.com
	 * @throws ExecutionException
	 */
	public SupplierVo getSupplierByName(String name, boolean showName) {
		return supplierSimpleCache.asMap().values().stream().filter(v -> showName ? v.isShowName() : true).filter(v -> name.equalsIgnoreCase(v.getSupplierName())).findFirst().orElse(null);
	}

	/**
	 * 查询供应商名称和ID的映射(jvm缓存5分钟)
	 * 
	 * @return
	 * @since 2018年1月18日
	 * @author jik.shu@yikuyi.com
	 * @throws ExecutionException
	 */
	public Map<String, String> getSupplierDisplayName(Boolean showName) {
		return supplierSimpleCache.asMap().values().stream().filter(v -> null == showName ? true : v.isShowName() == showName)
				.collect(Collectors.toMap(SupplierVo::getSupplierId, v -> v.isShowName() ? String.valueOf(v.getSupplierName()) : String.valueOf(v.getSupplierCode())));
	}

	/**
	 * 查询所有自动基础的供应商(jvm缓存5分钟)
	 * 
	 * @return
	 * @since 2018年1月19日
	 * @author jik.shu@yikuyi.com
	 */
	public Set<String> getAutoIntegrateSupplier(Boolean auto) {
		// 兼容null为N的数据情况，非Y都是N
		return supplierSimpleCache.asMap().values().stream().filter(v -> null == auto ? true : auto == "Y".equalsIgnoreCase(v.getIsAutoIntegrateQty())).map(SupplierVo::getSupplierId).collect(Collectors.toSet());
	}

	/**
	 * 查询所有有效分销商ID(jvm缓存5分钟)
	 * 
	 * @return
	 * @since 2018年1月19日
	 * @author jik.shu@yikuyi.com
	 */
	public Set<String> getSupplierIds() {
		return supplierSimpleCache.asMap().keySet();
	}

	/**
	 * 获取供应商简单信息(jvm缓存5分钟)
	 * 
	 * @param supplierIds
	 * @return
	 * @since 2018年1月22日
	 * @author jik.shu@yikuyi.com
	 */
	public Map<String, SupplierVo> getSupplierSimple(Collection<String> supplierIds) {
		if (CollectionUtils.isEmpty(supplierIds)) {
			return supplierSimpleCache.asMap();
		} else {
			return supplierSimpleCache.getAllPresent(supplierIds);
		}
	}

	/**
	 * 查询所有供应商联系人邮箱
	 * 
	 * @param supplierId
	 * @return
	 * @since 2018年1月22日
	 * @author jik.shu@yikuyi.com
	 * @throws ExecutionException
	 */
	public SupplierMailVo getSuplierrelationShipMail(String supplierId) {
		return supplierDao.getSuplierrelationShipMail(supplierId);
	}

	/**
	 * 发送mq，刷新jvm缓存
	 * 
	 * @param supplierIds
	 * @since 2018年1月30日
	 * @author jik.shu@yikuyi.com
	 */
	public void refreshSupplierCache(@RequestBody List<String> supplierIds) {
		msgSender.sendMsg(supplierCacheTopmic, (Serializable) supplierIds, null);
	}

	/**
	 * 初始化或者新增或者更新供应商数据缓存
	 * 
	 * @param supplierIds
	 * @since 2018年1月18日
	 * @author jik.shu@yikuyi.com
	 */
	public synchronized Map<String, SupplierVo> initSupplierCache(Collection<String> supplierIds) {
		// 调用接口获取分销商数据
		News news = new News();
		news.setNewsIds(Collections.emptyList());
		news.setStatus("PUBLISHED");
		news.setCategoryTypeId("VENDOR");
		Set<String> newsList = infoClientBuilder.builderNewsClient().newsBatch(news).stream().map(News::getNewsId).collect(Collectors.toSet());
		List<SupplierVo> suppliers = supplierDao.searchSupplierSimple(supplierIds);
		supplierSimpleCache.putAll(suppliers.stream().map(v -> {
			v.setVendorDetail(newsList.contains(v.getSupplierId()));
			return v;
		}).collect(Collectors.toMap(SupplierVo::getSupplierId, Function.identity())));
		return supplierSimpleCache.asMap();
	}
}