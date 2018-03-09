package com.yikuyi.product.activity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yikuyi.activity.model.ActivityPeriods;
import com.yikuyi.activity.model.ActivityProductDraft;

/**
 * @author tongkun
 * 活动区间草稿dao
 */
@Mapper
public interface ActivityPeriodsDraftDao {

    int save(ActivityPeriods record);

    /**
     * 根据活动id查询活动区别信息
     * @param activityId
     * @return
     * @since 2017年6月20日
     * @author zr.aoxianbing@yikuyi.com
     */
	List<ActivityPeriods> getPeriodsByActivityId(@Param(value = "activityId") String activityId);

	/**
	 * 根据活动id删除活动区间信息
	 * @param activityId
	 * @since 2017年6月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	void deleteByActivityId(@Param(value = "activityId") String activityId);
	
	/**
	 * 修改状态
	 * @param activityId
	 * @param periodsId
	 * @param status
	 * @since 2017年6月20日
	 * @author tb.lijing@yikuyi.com
	 */
	public void updateActivityPeriodsDraftByCondition(@Param(value = "activityId") String activityId,@Param(value = "periodsId") String periodsId,@Param(value = "status") ActivityProductDraft.Status status);
	
	void update(ActivityPeriods record);
	
	void deleteByPeriodsId(@Param(value = "periodsId") String periodsId);
}