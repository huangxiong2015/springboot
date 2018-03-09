package com.yikuyi.product.activity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yikuyi.activity.model.ActivityPeriods;

/**
 * @author tongkun
 * 活动区间dao
 */
@Mapper
public interface ActivityPeriodsDao {

	/**
	 * 保存区间信息
	 * @param record
	 * @return
	 * @since 2017年6月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
    int save(ActivityPeriods record);

    /**
     * 判断区间是否存在
     * @param record
     * @return
     * @since 2017年6月20日
     * @author zr.aoxianbing@yikuyi.com
     */
	int getPeriodsByInterval(ActivityPeriods record);
	/**
	 * 根据活动id删除活动区间信息
	 * @param activityId
	 * @since 2017年6月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	void deleteByActivityId(@Param(value = "activityId") String activityId);
	List<ActivityPeriods> findActivityPeriods(String activityId);
}