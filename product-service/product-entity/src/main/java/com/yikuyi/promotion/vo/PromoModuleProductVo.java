package com.yikuyi.promotion.vo;

import com.yikuyi.promotion.model.PromoModuleProduct;

public class PromoModuleProductVo extends PromoModuleProduct {

	private static final long serialVersionUID = 3956717154513089160L;
	
	private DiscountVo discountVo;
	
	private boolean useStockQty;
	
	private PromotionFlagVo promotionFlag;
	
	private PromotionModuleEffectiveVo promotionModuleEffectiveVo;

	public PromotionModuleEffectiveVo getPromotionModuleEffectiveVo() {
		return promotionModuleEffectiveVo;
	}

	public void setPromotionModuleEffectiveVo(PromotionModuleEffectiveVo promotionModuleEffectiveVo) {
		this.promotionModuleEffectiveVo = promotionModuleEffectiveVo;
	}

	public PromotionFlagVo getPromotionFlag() {
		return promotionFlag;
	}

	public void setPromotionFlag(PromotionFlagVo promotionFlag) {
		this.promotionFlag = promotionFlag;
	}

	public DiscountVo getDiscountVo() {
		return discountVo;
	}

	public void setDiscountVo(DiscountVo discountVo) {
		this.discountVo = discountVo;
	}

	public boolean isUseStockQty() {
		return useStockQty;
	}

	public void setUseStockQty(boolean useStockQty) {
		this.useStockQty = useStockQty;
	}
	
}