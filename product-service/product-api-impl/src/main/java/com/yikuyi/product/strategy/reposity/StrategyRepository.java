package com.yikuyi.product.strategy.reposity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.yikuyi.strategy.model.Strategy;
import com.yikuyi.strategy.vo.StrategyVo;

/**
 * 包邮模块
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Repository
public interface StrategyRepository extends MongoRepository<Strategy, String> {

	/**
	 * 查询分页列表
	 * @param queryObject
	 * @param pageable
	 * @return
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Query("?0")
	Page<StrategyVo> findStrategyByPage(Object params,Pageable pageable);
	


	
	
}