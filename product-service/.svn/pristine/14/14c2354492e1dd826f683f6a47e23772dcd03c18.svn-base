/*
 * Created: 2017年12月20日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.specialoffer.manager;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.yikuyi.product.common.utils.UtilsHelp;
import com.yikuyi.product.specialoffer.repository.SpecialOfferProductRepository;
import com.yikuyi.specialoffer.model.SpecialOfferProduct;

@Service
public class SpecialOfferProductManager {
	
	@Autowired
	private SpecialOfferProductRepository specialOfferProductRepository;
	
	/**
	 * 查询专属特价商品数据
	 * @param ruleId
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2017年12月20日
	 * @author tb.lijing@yikuyi.com
	 */
	public PageInfo<SpecialOfferProduct> findSpecialOfferProductByPage(String ruleId, int page, int pageSize){
		Query query  = new Query();
		Criteria criteria = new Criteria();
		criteria.and("ruleId").is(ruleId);
		int pageNo = 0;
		if(page>0) 
			pageNo = page-1;
		Sort sort = new Sort(Direction.DESC,"updatedTimeMillis");
		PageRequest pageable = new PageRequest(pageNo,pageSize,sort);
		query.addCriteria(criteria);
		Page<SpecialOfferProduct> pageInfo = specialOfferProductRepository.findSpecialOfferProductByPage(query.getQueryObject(), pageable);
		List<SpecialOfferProduct> SpecialOfferProducts = pageInfo.getContent();
		if(CollectionUtils.isEmpty(SpecialOfferProducts)){
			return new PageInfo<>(Collections.emptyList());
		}else{
			SpecialOfferProducts.stream().forEach(a -> {
				a.setCreatedTimeMillis(UtilsHelp.timeStamp2Date(a.getCreatedTimeMillis()));
				a.setUpdatedTimeMillis(UtilsHelp.timeStamp2Date(a.getUpdatedTimeMillis()));
			});
		}
		PageInfo<SpecialOfferProduct> pageResult = new PageInfo<>(SpecialOfferProducts);		
		pageResult.setPageNum(page);
		pageResult.setPageSize(pageSize);
		pageResult.setTotal(pageInfo.getTotalElements());	
		return pageResult;
	}
}
