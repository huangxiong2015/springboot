package com.yikuyi.product.specialoffer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.yikuyi.specialoffer.model.SpecialOfferRule;
import com.yikuyi.specialoffer.vo.SpecialOfferRuleVo;

/**
 * 专属特价规则
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Repository
public interface SpecialOfferRuleRepository extends MongoRepository<SpecialOfferRule, String> {
	
	/**
	 * 分页查询专属特价列表信息
	 * @param params
	 * @param pageable
	 * @return
	 * @since 2017年12月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Query("?0")
	public Page<SpecialOfferRuleVo> findSpecialOfferRuleByPage(Object params,Pageable pageable);

	
	
}