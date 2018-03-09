package com.yikuyi.product.promotion.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yikuyi.promotion.model.PromotionModuleDraft;

/**
 *促销活动相关服务
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Mapper
public interface PromotionModuleDraftDao {

	/**
	 * 根据promoModuleId查询促销模块草稿信息
	 * @param promoModuleId
	 * @return
	 * @since 2017年10月10日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public PromotionModuleDraft getPromotionModuleDraft(String promoModuleId);

	/**
	 * 根据promoModuleId修改模块草稿信息 
	 * @param draft
	 * @since 2017年10月10日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public void updatePromotionModuleDraft(PromotionModuleDraft draft);

	
	/**
	 * 模块草稿新增
	 * @param moduleDraft        
	 * @return
	 * @since 2017年10月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	 public void insert(PromotionModuleDraft moduleDraft);

	 /**
	  * 查询list
	  * @param moduleDraft
	  * @return
	  * @since 2017年10月10日
	  * @author zr.wuxiansheng@yikuyi.com
	  */
	 public List<PromotionModuleDraft> getPromotionModuleDraftList(PromotionModuleDraft moduleDraft);
	

	
  
}