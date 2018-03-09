package com.yikuyi.product.promotion.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.yikuyi.promotion.model.PromotionModuleContentDraft;

@Repository
public interface PromotionModuleContentDraftRepository extends MongoRepository<PromotionModuleContentDraft, String>{

	/**
	 * 更加活动ID查询所有ModuleContent内容
	 * @param promotionId
	 * @return
	 * @since 2017年10月11日
	 * @author jik.shu@yikuyi.com
	 */
	@Query("{'promotionId':?0,'status':'ENABLE'}")
	public List<PromotionModuleContentDraft> findModuleContentsByPromoId(String promotionId);
}