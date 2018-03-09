package com.yikuyi.product.activity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yikuyi.activity.model.Activity;
import com.yikuyi.activity.model.ActivityProductDraft;
import com.yikuyi.activity.vo.ActivityVo;

/**
 * @author tongkun
 * 活动草稿dao
 */
@Mapper
public interface ActivityDraftDao {

    int save(Activity record);

    /**
     * 根据活动id查询活动信息
     * @param activityId
     * @return
     * @since 2017年6月20日
     * @author zr.aoxianbing@yikuyi.com
     */
	Activity getActivityById(@Param(value = "id") String activityId);

	/**
	 * 根据活动id删除活动信息
	 * @param activityId
	 * @since 2017年6月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	void deleteById(@Param(value = "id") String activityId);
    
    /**
     * 查询活动草稿详情
     * @param activityId
     * @return
     * @since 2017年6月8日
     * @author zr.zhanghua@yikuyi.com
     */
    public ActivityVo findActivityById(String  activityId);
    
    /**
     * 根据activityId和periodsId查询活动商品草稿数据
     * @param activityId
     * @param periodsId
     * @return
     * @since 2017年6月20日
     * @author tb.lijing@yikuyi.com
     */
    
    public List<ActivityProductDraft> findActivityDraftByActivityIdAndPeriodsId(@Param(value = "activityId") String activityId,@Param(value = "periodsId") String periodsId);

}