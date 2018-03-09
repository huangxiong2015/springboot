package com.yikuyi.product.goods.dao;

import org.apache.ibatis.annotations.Mapper;
import com.yikuyi.product.model.ProductSearchResult;

/**
 * 商品搜索结果页日志记录
 * @author zr.wenjiao@yikuyi.com
 * @version 1.0.0
 */
@Mapper
@FunctionalInterface
public interface ProductSearchResultDao {
	
	/**
	 * 新增搜索日志记录
	 * @param searchResult
	 * @since 2017年5月11日
	 * @author zr.wenjiao@yikuyi.com
	 */
	public void insert(ProductSearchResult searchResult);

}
