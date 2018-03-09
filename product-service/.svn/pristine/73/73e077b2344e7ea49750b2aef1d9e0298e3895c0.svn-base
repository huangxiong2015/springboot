package com.yikuyi.product.resource;


import com.yikuyi.promotion.vo.PromotionPreviewVo;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
@Headers({"Content-Type: application/json;charset=UTF-8"})
public interface PromotionModuleClient {
	@RequestLine("GET /v1/promotionModule/getPromotionDetail/{promotionId}/{promoModuleId}/{promoModuleType}?formal={formal}")
	public PromotionPreviewVo getPromotionDetail(@Param("promotionId") String promotionId,@Param("promoModuleId")String promoModuleId,@Param("promoModuleType")String promoModuleType,@Param("formal")String formal);
}
