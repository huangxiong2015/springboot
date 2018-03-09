package com.yikuyi.product.promotion.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yikuyi.promotion.model.PromotionModule;

/**
 * 促销活动相关服务
 * 
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Mapper
public interface PromotionModuleDao {

	/**
	 * 根据promotionId查询促销模块信息
	 * 
	 * @param promoModuleId
	 * @return
	 * @since 2017年10月11日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public PromotionModule getPromotionModule(String promoModuleId);

	/**
	 * 根据promoModuleId修改模块信息
	 * 
	 * @param module
	 * @since 2017年10月10日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public void updatePromotionModule(PromotionModule module);

	/**
	 * Copy草稿数据
	 * 
	 * @param promoId
	 * @since 2017年10月11日
	 * @author jik.shu@yikuyi.com
	 */
	public void copyDraftByPromoId(String promoId);

	/**
	 * 删除数据通过活动ID
	 * 
	 * @param promoId
	 * @since 2017年10月11日
	 * @author jik.shu@yikuyi.com
	 */
	public void deleteByPromoId(String promoId);

	/**
	 * 查询相关list
	 * @param module
	 * @return
	 * @since 2017年10月11日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public List<PromotionModule> getPromotionModuleList(PromotionModule module);
}