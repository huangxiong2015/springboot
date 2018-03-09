package com.yikuyi.product.strategy.reposity;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.yikuyi.strategy.model.StrategyProduct;

/**
 * 包邮模块
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Repository
public interface StrategyProductRepository extends MongoRepository<StrategyProduct, String> {

	/**
	 * 查询正式商品分页
	 * @param params
	 * @param pageable
	 * @return
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Query("?0")
	Page<StrategyProduct> findStrategyProductByPage(Object params,Pageable pageable);
	
	/**
	 * 根据strategyId查询list
	 * 
	 * @param strategyId
	 * @return
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Query("{'strategyId':?0}")
	public List<StrategyProduct> findStrategyProductByStrategyId(String strategyId);
	
	
	/**
	 * 删除相关的商品信息
	 * @param promoId
	 * @since 2017年10月11日
	 * @author jik.shu@yikuyi.com
	 */
	@Query(value = "{'strategyId':?0}", delete = true)
	public void deleteStrategyProductBystrategyId(String strategyId);
	
	
	/**
	 * 根据strategyId查询list
	 * 
	 * @param strategyId
	 * @return
	 * @since 2018年1月8日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Query("{'strategyId':?0},{'_id':0,'productId':1}")
	public List<StrategyProduct> findProductIdByStrategyId(String strategyId);
	

	
	
}