package com.yikuyi.product.promotion.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.yikuyi.promotion.model.Promotion;
import com.yikuyi.promotion.vo.PromotionParamVo;
import com.yikuyi.promotion.vo.PromotionVo;

/**
 *促销活动相关服务
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Mapper
public interface PromotionDao {

	/**
	 * 查询促销活动列表
	 * @param promotionVo
	 * @param rowBounds
	 * @return
	 * @since 2017年10月9日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public List<PromotionVo> getPromotionVoList(PromotionParamVo param, RowBounds rowBounds);
	
	
	/**
	 * 根据promotionId查询促销活动信息
	 * @param promotionId
	 * @return
	 * @since 2017年10月10日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public Promotion getPromotion(String promotionId);
	
	/**
	 * 创建活动
	 * @param promotion        
	 * @return
	 * @since 2017年10月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void insert(Promotion promotion);


	
	/**
	 * 编辑活动
	 * @param promotion        
	 * @return
	 * @since 2017年10月11日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void update(Promotion promotion);

	/**
	 * 查询List(不带分页)
	 * @param promotionVo
	 * @return
	 * @since 2017年10月13日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public List<Promotion> getPromotionList(PromotionVo promotionVo);

	/**
	 * 活动未开始的list
	 * @param promotionVo
	 * @return
	 * @since 2017年10月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public List<Promotion> getPromotionNotStartList();

	/**
	 * 系统时间为活动结束时间的第二天凌晨时刻的list
	 * @param promotionVo
	 * @return
	 * @since 2017年10月14日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public List<Promotion> getPromotionNotEndList(@Param("endDate") String endDate);

	  
}