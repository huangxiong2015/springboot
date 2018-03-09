package com.yikuyi.product.specialoffer.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.yikuyi.specialoffer.model.SpecialOfferProductDraft;

/**
 * 专属特价规则商品信息(草稿)
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Repository
public interface SpecialOfferProductDraftRepository extends MongoRepository<SpecialOfferProductDraft, String> {
	/**
	 * 分页查询专属特价商品草稿数据
	 * @param params
	 * @param pageable
	 * @return
	 * @since 2017年12月20日
	 * @author tb.lijing@yikuyi.com
	 */
	@Query("?0")
	public Page<SpecialOfferProductDraft> findSpecialOfferProductDraftByPage(Object params,Pageable pageable);
	
	
	/**
	 * 根据ruleId查询list
	 * @param ruleId
	 * @return
	 * @since 2017年12月25日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Query("{'ruleId':?0}")
	public List<SpecialOfferProductDraft> findSpecialOfferProductDraftByRuleId(String ruleId);
	
	/**
	 * 查询list
	 * @param ruleId
	 * @param status
	 * @return
	 * @since 2017年12月26日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Query("{'ruleId':?0,'status':?1}")
	public List<SpecialOfferProductDraft> findSpecialOfferProductDraftByCondition(String ruleId,String status);
	
	
	/**
	 * 删除规则相关的商品信息
	 * @param promoId
	 * @since 2017年10月11日
	 * @author jik.shu@yikuyi.com
	 */
	@Query(value = "{'ruleId':?0}", delete = true)
	public void deleteSpecialOfferProductDraftByruleId(String ruleId);
}