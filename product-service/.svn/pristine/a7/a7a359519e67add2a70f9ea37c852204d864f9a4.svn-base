package com.yikuyi.product.resource;

import com.github.pagehelper.PageInfo;
import com.yikuyi.promotion.model.Promotion;
import com.yikuyi.promotion.model.Promotion.PromotionType;
import com.yikuyi.promotion.vo.PromotionVo;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers({ "Content-Type: application/json;charset=UTF-8" })
public interface PromotionClient {

	/**
	 * 根据活动id查询促销活动信息
	 * 
	 * @param promotionId
	 *            活动id
	 * @return 活动信息
	 */
	@RequestLine("GET /v1/promotions/{promotionId}")
	public Promotion getPromotion(@Param("promotionId") String promotionId);

	/**
	 * 根据组合条件查询活动列表
	 * 
	 * @param promotionType
	 * @param timeStatus
	 *            NOTSTARTED , ONGOING , COMPLETE
	 * @param page
	 * @param pageSize
	 * @param authToken
	 * @return
	 * @since 2018年2月6日
	 * @author jik.shu@yikuyi.com
	 */
	@RequestLine("GET /v1/promotions?promotionType={promotionType}&timeStatus={timeStatus}&page={page}&pageSize={pageSize}")
	@Headers({ "Authorization: Basic {authToken}" })
	public PageInfo<PromotionVo> getPromotions(@Param("promotionType") PromotionType promotionType, @Param("timeStatus") String timeStatus, @Param("page") int page, @Param("pageSize") int pageSize, @Param("authToken") String authToken);
}