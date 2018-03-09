package com.yikuyi.product.promotion.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.yikuyi.promotion.model.PromoModuleProduct;
import com.yikuyi.promotion.vo.PromotionProductVo;

/**
 * 促销活动相关服务
 * 
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Mapper
public interface PromoModuleProductDao {

	/**
	 * Copy草稿表数据
	 * 
	 * @param promoId
	 * @since 2017年10月11日
	 * @author jik.shu@yikuyi.com
	 */
	public void copyDraftByPromoId(String promoId);

	/**
	 * 根据活动ID删除
	 * 
	 * @param promoId
	 * @since 2017年10月11日
	 * @author jik.shu@yikuyi.com
	 */
	public void deleteByPromoId(String promoId);

	/**
	 * 通过活动ID获取下面所有商品
	 * 
	 * @param promoId
	 * @since 2017年10月11日
	 * @author jik.shu@yikuyi.com
	 */
	public List<PromoModuleProduct> getAllProductByPromoId(String promoId);

	/**
	 * 查询商品列表
	 * 
	 * @param PromoModuleProductDraft
	 * @param rowBounds
	 * @return
	 * @since 2017年10月11日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<PromotionProductVo> listModuleProduct(PromotionProductVo param, RowBounds rowBounds);

}