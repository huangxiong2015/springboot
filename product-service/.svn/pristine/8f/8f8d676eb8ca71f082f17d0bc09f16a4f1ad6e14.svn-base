package com.yikuyi.product.promotion.dao;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.yikuyi.promotion.model.PromoModuleProduct.ModuleProductStatus;
import com.yikuyi.promotion.model.PromoModuleProductDraft;
import com.yikuyi.promotion.vo.PromotionProductVo;

/**
 *促销活动相关服务
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Mapper
public interface PromoModuleProductDraftDao {

	/**
	 * 根据promoModuleId查询促销商品草稿信息
	 * @param promoModuleId
	 * @return
	 * @since 2017年10月10日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public PromoModuleProductDraft getEntityByPromoModuleId(String promoModuleId);

	/**
	 * 商品模块草稿新增
	 * @param moduleDraft        
	 * @return
	 * @since 2017年10月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	 public int insert(PromoModuleProductDraft moduleDraft);
	 
	/**
	 * 商品模块草稿新增
	 * 
	 * @param moduleDraft
	 * @return
	 * @since 2017年10月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	public int inserts(List<PromoModuleProductDraft> moduleDraft);
	
	/**
	 * 查询促销商品list
	 * @param productDraft
	 * @return
	 * @since 2017年10月10日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public List<PromoModuleProductDraft> getPromoModuleProductDraftList(PromoModuleProductDraft productDraft);

	/**
	 * 查询商品列表
	 * @param PromoModuleProductDraft
	 * @param rowBounds
	 * @return
	 * @since 2017年10月11日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<PromotionProductVo> listDraftModuleProduct(PromotionProductVo param, RowBounds rowBounds);
	
	/**
	 * 根据品牌、型号、供应商、仓库查询数据
	 * @param promoModuleProductDraft
	 * @return
	 * @since 2017年10月12日
	 * @author tb.lijing@yikuyi.com
	 */
	public List<PromoModuleProductDraft> findPromoModuleProductDraftByCondition(PromoModuleProductDraft promoModuleProductDraft);
	
	/**
	 * 根据品牌、型号、供应商、仓库查询数据
	 * @param promoModuleProductDraft
	 * @return
	 * @since 2017年10月12日
	 * @author tb.lijing@yikuyi.com
	 */
	public List<PromoModuleProductDraft> findRealDraftProduct(PromoModuleProductDraft promoModuleProductDraft);
	
	/**
	 * 根据PROMO_MODULE_PRODUCT_ID删除数据
	 * @param promoModuleProductId
	 * @return
	 * @since 2017年10月12日
	 * @author tb.lijing@yikuyi.com
	 */
	public int deletePromoModuleProductDraftById(@Param(value = "promoModuleProductId")String promoModuleProductId);
	
	/**
	 * 根据模块和商品ID删除
	 * @param promoModuleId
	 * @param productIds
	 * @return
	 * @since 2017年10月25日
	 * @author jik.shu@yikuyi.com
	 */
	public int deletePromoModuleProductDraft(@Param(value = "promoModuleId")String promoModuleId , @Param(value = "list")Collection<String> productIds);
	
	/**
	 * 批量删除活动装修商品草稿信息
	 * @param promoModuleProductIds
	 * @since 2017年10月12日
	 * @author tb.lijing@yikuyi.com
	 */
	public void deletePromoModuleProductDraftBatch(@Param(value = "list")List<String> promoModuleProductIds);
	
	/**
	 * 根据PROMO_MODULE_PRODUCT_ID查询数据
	 * @param promoModuleProductIds
	 * @return
	 * @since 2017年10月13日
	 * @author tb.lijing@yikuyi.com
	 */
	public List<PromoModuleProductDraft> findPromoModuleProductsByIds(@Param(value = "promoModuleProductIds")String[] promoModuleProductIds,@Param(value = "status")ModuleProductStatus status);
	/**
	 * 查询商品数量
	 * @param account
	 * @return
	 * @since 2017年10月17日
	 * @author zr.helinmei@yikuyi.com
	 */
	public Integer productCount(PromoModuleProductDraft productDraft);
}