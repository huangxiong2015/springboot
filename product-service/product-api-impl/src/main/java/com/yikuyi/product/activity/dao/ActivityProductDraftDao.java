package com.yikuyi.product.activity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.yikuyi.activity.model.ActivityProduct;
import com.yikuyi.activity.model.ActivityProductDraft;
import com.yikuyi.activity.vo.ActivityProductDraftVo;

/**
 * @author tongkun
 * 活动商品草稿dao
 */
@Mapper
public interface ActivityProductDraftDao {

	public int save(ActivityProductDraft record);

	public ActivityProductDraft getProductById(@Param(value = "id") String productId);

	public int editProductInfo(ActivityProductDraft activityProduct);

	/**
	 * 根据活动id查询有效的商品
	 * @param activityId
	 * @return
	 * @since 2017年6月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<ActivityProduct> getProductByActivityId(@Param(value = "id")String activityId);

	/**
	 * 根据活动id删除活动商品信息
	 * @param activityId
	 * @since 2017年6月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public void deleteByActivityId(@Param(value = "id")String activityId);

	/**
	 * 根据查询条件查询草稿活动商品
	 * @param activityProductDraft
	 * @return
	 * @since 2017年6月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<ActivityProductDraft> getProductByCondition(ActivityProductDraft activityProductDraft);

	/**
	 * 根据id集合查询草稿商品
	 * @param ids
	 * @return
	 * @since 2017年6月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public List<ActivityProductDraft> getProductByIds(@Param(value = "ids")String[] ids);
	
	public int saveActivityProductDraft(List<ActivityProductDraft> list);
	
	/**
	 * 删除活动商品草稿数据
	 * @param activityProductId
	 * @return
	 * @since 2017年6月20日
	 * @author tb.lijing@yikuyi.com
	 */
	public int deleteActivityProductDraft(@Param(value = "activityProductId")String activityProductId);
	
	/**
	 * 批量删除活动商品草稿数据
	 * @param activityProductId
	 * @return
	 * @since 2017年7月20日
	 * @author jik.shu@yikuyi.com
	 */
	public int deleteActivityProductDraftBatch(@Param(value = "list")List<String> activityProductIds);
	
	/**
	 * 查询活动商品草稿数据
	 * @param activityProductDraft
	 * @return
	 * @since 2017年6月20日
	 * @author tb.lijing@yikuyi.com
	 */
	public List<ActivityProductDraftVo> findActivityProductDraftByCondition(ActivityProductDraft activityProductDraft,RowBounds rowBounds);
	
	/**
	 * 修改活动商品草稿数据
	 * @param activityProductDraft
	 * @return
	 * @since 2017年6月20日
	 * @author tb.lijing@yikuyi.com
	 */
	public int updateActivityProductDraftByCondition(ActivityProductDraft activityProductDraft);
	
	/**
	 * 根据条件查询活动草稿数据
	 * @param activityProductDraft
	 * @return
	 * @since 2017年6月20日
	 * @author tb.lijing@yikuyi.com
	 */
	public List<ActivityProductDraft> findProductDraftByCondition(ActivityProductDraft activityProductDraft);
	
	/**
	 * 将正式表的活动商品转移到草稿表中
	 * @param periodsIds
	 * @return
	 * @since 2017年6月20日
	 * @author tongkun@yikuyi.com
	 */
	public int transProductsToDraft(List<String> periodsIds);
}