package com.yikuyi.product.strategy.reposity;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.yikuyi.strategy.model.StrategyProductDraft;

/**
 * 包邮草稿商品模块
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Repository
public interface StrategyProductDraftRepository extends MongoRepository<StrategyProductDraft, String> {

	/**
	 * 查询正式商品分页
	 * @param params
	 * @param pageable
	 * @return
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Query("?0")
	Page<StrategyProductDraft> findStrategyProductDraftByPage(Object params,Pageable pageable);
	

	/**
	 * 查询list
	 * @param ruleId
	 * @param status
	 * @return
	 * @since 2017年12月26日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Query("{'strategyId':?0,'productStatus':?1}")
	public List<StrategyProductDraft> findStrategyProductDraftByCondition(String strategyId,String productStatus);
	
	/**
	 * 根据strategyId查询list
	 * @param startegyId
	 * @return
	 * @since 2018年1月9日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Query("{'strategyId':?0}")
	public List<StrategyProductDraft> findStrategyProductDraftByStrategyId(String strategyId);
	
	/**
	 * 删除相关的商品信息
	 * @param strategyId
	 * @since 2018年1月9日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Query(value = "{'strategyId':?0}", delete = true)
	public void deleteStrategyProductDraftByStrategyId(String strategyId);
	
	/**
	 * 根据条件查询数量
	 * @param strategyId
	 * @return
	 * @since 2018年1月10日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Query(value = "{'strategyId':?0,'productStatus':?1}" , count = true)
	public int findNumStrategyId(String strategyId,String productStatus);
	
	
}