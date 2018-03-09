package com.yikuyi.product.activity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.yikuyi.activity.model.Activity;
import com.yikuyi.activity.vo.ActivityVo;

/**
 * @author tongkun
 * 活动dao
 */
@Mapper
public interface ActivityDao {
	
    int save(Activity record);
    
    void update(Activity record);
    
    /**
     * 根据id获取对象
     * @param activityId 活动id
     * @return 活动对象
     * @since 2017年6月20日
     * @author tongkun@yikuyi.com
     */
    public Activity getById(String activityId);
    
    /**
     * 根据活动ID查询，时区是启用的活动
     * @param activityId
     * @return
     * @since 2017年9月4日
     * @author jik.shu@yikuyi.com
     */
    Activity getActivityInfo(@Param("activityId") String activityId);
    
	/**
	 * 根据活动id删除活动信息
	 * @param activityId
	 * @since 2017年6月20日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	void deleteById(@Param(value = "id") String activityId);
    /**
     * 查询活动管理列表
     * @param activityVo
     * @param rowBouds
     * @return
     * @since 2017年6月8日
     * @author zr.zhanghua@yikuyi.com
     */
    public List<ActivityVo> findActivityByEntity(ActivityVo  activityVo,RowBounds rowBouds);
    
    /**
     * 查询活动详情
     * @param activityId
     * @return
     * @since 2017年6月8日
     * @author zr.zhanghua@yikuyi.com
     */
    public ActivityVo findActivityById(String  activityId);
    
    /**
     * 获取要启动的活动
     * @return
     * @since 2017年10月14日
     * @author jik.shu@yikuyi.com
     */
    public List<Activity> getStartActivity();
    
    /**
     * 获取要昨天停止的活动
     * @return
     * @since 2017年10月14日
     * @author jik.shu@yikuyi.com
     */
    public List<Activity> getEndActivity();
    
    /**
     * 删除活动管理
     * @param activityId
     * @since 2017年6月13日
     * @author zr.zhanghua@yikuyi.com
     */
    public void deleteActivity(String  activityId);

    
    /**
     * 按照创建时间排序,只查询在当前时间内活动
     * @return
     * @since 2017年8月29日
     * @author jik.shu@yikuyi.com
     */
    public List<Activity> getEnableActivity();
    
    /**
     * 查询当天活动详情
     * @return
     * @since 2017年6月13日
     * @author zr.zhanghua@yikuyi.com
     */
    public ActivityVo getTodayActivity();
    
    /**
     * 根据ID查询处在活动的信息
     * @return
     * @since 2017年6月13日
     * @author zr.zhanghua@yikuyi.com
     */
    public ActivityVo getActivityStandardById(String activityId);
    
    /**
     * 查询活动时间是否有重叠
     * @return
     * @since 2017年6月14日
     * @author zr.zhanghua@yikuyi.com
     */
    public int findActivityDate(ActivityVo activityVo);
    
    /**
     * 查询活动名称是否有存在
     * @return
     * @since 2017年6月14日
     * @author zr.zhanghua@yikuyi.com
     */
    public int findActivityName(ActivityVo activityVo);
    
 

}