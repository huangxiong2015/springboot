package com.yikuyi.product.activity.dao;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author tongkun
 * 活动商品草稿dao
 */
@Mapper
public interface ActivitySaleHistoryDao {

	/**
	 * 将今天的活动商品保存为历史
	 * @return
	 * @since 2017年6月19日
	 * @author tongkun@yikuyi.com
	 */
	public int saveTodayProductsToHistory();
}