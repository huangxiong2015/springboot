package com.yikuyi.product.strategy.manager;

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
import com.yikuyi.product.strategy.reposity.StrategyProductRepository;
import com.yikuyi.strategy.model.StrategyProduct;
import com.yikuyi.strategy.model.StrategyProduct.ProductStatus;

/**
 * 包邮/限购模块
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Service
public class StrategyProductManager {
	
	@Autowired
	private StrategyProductRepository strategyProductRepository;
	
	/**
	 * 正式商品列表
	 * @param strategyId
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public PageInfo<StrategyProduct> list(String strategyId, int page, int pageSize) {
		Query query  = new Query();
		Criteria criteria = new Criteria();
		criteria.and("strategyId").is(strategyId);
		criteria.and("productStatus").ne(ProductStatus.DETETED.toString());
		int pageNo = 0;
		if(page>0){
			pageNo = page-1;
		}
		Sort sort = new Sort(Direction.DESC,"updatedTimeMillis");
		PageRequest pageable = new PageRequest(pageNo,pageSize,sort);
		query.addCriteria(criteria);
		Page<StrategyProduct> pageInfo = strategyProductRepository.findStrategyProductByPage(query.getQueryObject(), pageable);
		List<StrategyProduct> list = pageInfo.getContent();
		if(CollectionUtils.isEmpty(list)){
			PageInfo<StrategyProduct> pageResult = new PageInfo<>(Collections.emptyList());
			pageResult.setPageNum(page);
			pageResult.setPageSize(pageSize);
			pageResult.setTotal(pageInfo.getTotalElements());	
			return pageResult;
		}else{
			handleList(list);
		}
		PageInfo<StrategyProduct> pageResult = new PageInfo<>(list);		
		pageResult.setPageNum(page);
		pageResult.setPageSize(pageSize);
		pageResult.setTotal(pageInfo.getTotalElements());	
		return pageResult;
	}
	

	/**
	 * 处理数据
	 * @param list
	 * @since 2018年1月10日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
    private void handleList(List<StrategyProduct> list) {
    	for (StrategyProduct strategyProduct : list) {
    		strategyProduct.setCreatedTimeMillis(UtilsHelp.timeStamp2Date(strategyProduct.getCreatedTimeMillis()));
    		strategyProduct.setUpdatedTimeMillis(UtilsHelp.timeStamp2Date(strategyProduct.getUpdatedTimeMillis()));
		}
		
	}


		 
}