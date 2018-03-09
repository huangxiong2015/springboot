package com.yikuyi.product.activity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yikuyi.activity.model.ActivityProduct;
import com.yikuyi.activity.vo.ActivityProductVo;

/**
 * @author tongkun
 * 活动商品dao
 */
@Mapper
public interface ActivityProductDao {

    int save(ActivityProduct record);
    
    int update(ActivityProduct record);
    
    int updateQty(ActivityProduct record);
    
    public List<ActivityProduct>findActivityProduct(String activityId);
	/**
	 * 根据活动id删除活动商品信息
	 * @param activityId
	 * @since 2017年6月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	public void deleteByActivityId(@Param(value = "id")String activityId);
    /**
     * 查询活动商品数据
     * @param activityId
     * @param periodsId
     * @return
     * @since 2017年6月20日
     * @author tb.lijing@yikuyi.com
     */
    public List<ActivityProductVo> findActivityProductByCondition(@Param(value = "activityId")String activityId,@Param(value = "periodsIds")String[] periodsIds);

    /**
     * 将数量恢复到最大值
     * @since 2017年6月19日
     * @author tongkun@yikuyi.com
     */
    public void updateQtyToTotal();
    
    /**
     * 把指定的活动库存还原到最多，（目前针对停用场景）
     * @param activityId
     * @since 2017年9月21日
     * @author jik.shu@yikuyi.com
     */
    public void updateQtyToTotalById(@Param(value = "activityId")String activityId);
    
    public List<ActivityProductVo> findActivityProductByArray(@Param(value = "activityId")String activityId,@Param(value = "periodsIds")String[] periodsIds);
}