package com.yikuyi.product.promotion.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.yikuyi.promotion.model.PromotionModuleContent;

@Repository
public interface PromotionModuleContentRepository extends MongoRepository<PromotionModuleContent, String>{
	
	/**
	 * 删除活动下所有内容
	 * @param promoId
	 * @since 2017年10月11日
	 * @author jik.shu@yikuyi.com
	 */
	@Query(value = "{'promotionId':?0}", delete = true)
	public void deleteByPromoId(String promotionId);
	
	/**
	 * 删除活动下所有内容
	 * @param promoId
	 * @since 2017年10月11日
	 * @author jik.shu@yikuyi.com
	 */
	@Query(value = "{'promotionId':?0,'promoModuleType':?1}")
	public List<PromotionModuleContent> getBatchModuleByPromoIdAndType(String promotionId, String promoModuleType);
}