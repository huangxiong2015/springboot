package com.yikuyi.product.activity;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageInfo;
import com.yikuyi.activity.model.Activity;
import com.yikuyi.activity.model.ActivityProduct;
import com.yikuyi.activity.model.ActivityProductDraft;
import com.yikuyi.activity.vo.ActivityProductDraftVo;
import com.yikuyi.activity.vo.ActivityProductVo;
import com.yikuyi.activity.vo.ActivityVo;
import com.yikuyi.activity.vo.MockActivityDataVo;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

/**
 * 活动相关服务
 * @author zr.wenjiao@yikuyi.com
 * @version 1.0.0
 */
public interface IActivityResource {
	
	@ApiOperation(value = "活动管理启用、停用", notes = "作者：文娇<br>活动管理列表启用、停用功能", response = Void.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public void updateStatus(@ApiParam(value = "活动Id") String activityId,@ApiParam(value="状态(UNABLE:无效 ENABLE:有效 DELETE:删除)") Activity.Status status) throws BusinessException, ParseException;

	/**
	 * 将草稿活动信息转化成正式活动信息
	 * @author zr.aoxianbing@yikuyi.com
	 * @param activityId 活动id
	 */
	@ApiOperation(value = "将草稿活动信息转化成正式活动信息", notes = "作者：敖县兵<br>将草稿活动信息转化成正式活动信息", response = Void.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public String draftToFormal(@ApiParam(value = "活动Id", required = true)  String activityId) throws BusinessException;
	
	/**
	 * 判断时区是否有效，并更新无活动商品时区的状态为无效
	 * @param activityId
	 * @param periods
	 * @throws BusinessException
	 * @since 2017年7月26日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@ApiOperation(value = "判断时区是否有效，并更新无活动商品时区的状态为无效", notes = "作者：敖县兵<br>判断时区是否有效，并更新无活动商品时区的状态为无效", response = Void.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public void updatePeriodsStatus(@ApiParam(value = "活动Id", required = true) String activityId) throws BusinessException;
	/**
	 * 查询活动管理列表
	 * @since 2017年6月8日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@ApiOperation(value = "活动管理列表", notes = "作者：张华<br>活动管理列表", response = Activity.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)	
	public PageInfo<ActivityVo> list(
			@ApiParam(value="活动名称", required=false) String name,
			@ApiParam(value = "活动类型", required = false) String type,
			@ApiParam(value = "活动状态", required = false) String prStatus,
			@ApiParam(value = "开始时间", required = false) String startTime,
			@ApiParam(value = "结束时间", required = false) String endTime,			
			@ApiParam(value = "当前页", required = false) int page,
			@ApiParam(value = "当前页数量", required = false) int pageSize)throws Exception;
	
	/**
	/**
	 * 删除活动管理列表
	 * @since 2017年6月13日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@ApiOperation(value = "删除活动管理列表", notes = "作者：张华<br>删除活动管理列表", response = Void.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public void deleteActivity(@ApiParam(value="id主键",required=true) String activityId);
	
	/**
	 * 查询草稿活动详情
	 * @since 2017年6月8日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@ApiOperation(value = "查询草稿活动详情", notes = "作者：张华<br>查询草稿活动详情", response = ActivityVo.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public ActivityVo getSearchActivityDraft (@ApiParam(value="id主键",required=true) String activityId);
			

	/**
	 * 查询正式活动详情
	 * @since 2017年6月8日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@ApiOperation(value = "查询正式活动详情", notes = "作者：张华<br>查询正式活动详情", response = ActivityVo.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public ActivityVo getSearchActivityStandard (@ApiParam(value="id主键",required=true) String activityId );
	
	/**
	 * 查询当天正式活动详情
	 * @since 2017年6月8日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@ApiOperation(value = "查询当天正式活动详情", notes = "作者：张华<br>查询秒杀页面当天正式活动详情", response = ActivityVo.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public ActivityVo getTodayActivityStandard ();
	
	/**
	 * 查询当天正式活动详情
	 * @since 2017年6月8日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@ApiOperation(value = "查询当天正式活动详情", notes = "作者：张华<br>查询秒杀页面当天正式活动详情", response = ActivityVo.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public ActivityVo getActivityStandardById(@ApiParam(value="活动ID",required=true) String activityId);
	
	/**
	 * 修改活动草稿详情
	 * @since 2017年6月8日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@ApiOperation(value = "修改活动草稿详情", notes = "作者：张华<br>修改活动草稿详情", response = String.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public ActivityVo updateActivityDraft(@ApiParam(value="activity对象",required=true) ActivityVo activityVo)throws Exception;
	
	/**
	 * 保存活动草稿详情
	 * @since 2017年6月8日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@ApiOperation(value = "保存活动草稿详情", notes = "作者：张华<br>保存活动草稿详情", response = String.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public ActivityVo saveActivityDraft(@ApiParam(value="activity对象",required=true) ActivityVo activityVo)throws Exception;
	
	/**
	 * 保存活动正式信息
	 * @param activityVo
	 * @return
	 * @throws Exception
	 * @since 2017年8月28日
	 * @author jik.shu@yikuyi.com
	 */
	@ApiOperation(value = "保存活动正式信息", notes = "作者：舒作<br>保存活动正式信息", response = Activity.class)
	public Activity saveActivity(@ApiParam(value="activity对象",required=true) ActivityVo activityVo)throws BusinessException;
	
	/**
	 * 修改活动正式信息
	 * @param activityVo
	 * @return
	 * @throws Exception
	 * @since 2017年8月28日
	 * @author jik.shu@yikuyi.com
	 */
	@ApiOperation(value = "修改活动正式信息", notes = "作者：舒作<br>修改活动正式信息", response = Activity.class)
	public Activity updateActivity(@ApiParam(value="activity对象",required=true) ActivityVo activityVo)throws BusinessException;
	
	/**
	 * 活动商品上传的文件解析
	 * @param activityId
	 * @param periodsId
	 * @param fileUrl
	 * @param oriFileName
	 * @since 2017年6月8日
	 * @author tb.lijing@yikuyi.com
	 */
	@ApiOperation(value = "活动商品上传的文件解析", notes = "作者：李京<br>秒杀活动商品编辑页面用的接口。将阿里云文件下载，然后解析，匹配数据，存入草稿", response = Void.class)
	public String parseFile(@ApiParam("活动id") String activityId,@ApiParam("时间区间id") String periodsId,@ApiParam("阿里云文件路径")String fileUrl,@ApiParam("文件原始名称")String oriFileName) throws BusinessException;
	
	/**
	 * 查询活动商品已上传的正式表数据
	 * @param activityId
	 * @param periodsId
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2017年6月8日
	 * @author tb.lijing@yikuyi.com
	 */
	@ApiOperation(value = "活动商品信息查看列表", notes = "作者：李京<br>秒杀活动商品编辑页面用的接口。将活动商品信息返回到页面", response = ActivityProduct.class)
	public PageInfo<ActivityProductVo> findActivityProduct (@ApiParam(value = "活动id", required = false)String activityId,@ApiParam(value = "时间区间id", required = false)String periodsId,@ApiParam(value = "是否需要价格:Y(不区分大小写):需要，否则不需要", required = false)String priceFlag,
			@ApiParam(value = "活动类型:10001:商品秒杀 ，10000:商品促销", required = false)String activityType,
			@ApiParam(value = "当前页", required = false) Integer page,
			@ApiParam(value = "当前页数量", required = false) Integer pageSize);
	
	/**
	 * 查询活动商品已上传的草稿数据
	 * @param activityId
	 * @param periodsId
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2017年6月9日
	 * @author tb.lijing@yikuyi.com
	 */
	@ApiOperation(value = "活动商品信息草稿查看列表", notes = "作者：李京<br>秒杀活动商品编辑页面用的接口。将活动商品草稿信息返回到页面", response = ActivityProduct.class)
	public PageInfo<ActivityProductDraftVo> findActivityProductDraft(@ApiParam(value = "活动id", required = true)String activityId,@ApiParam(value = "时间区间id", required = false)String periodsId,@ApiParam(value = "状态:UNABLE(无效), ENABLE(有效)", required = false)ActivityProductDraft.Status status,
			@ApiParam(value = "当前页", required = false) int page,
			@ApiParam(value = "当前页数量", required = false) int pageSize,
			@ApiParam(value = "文件上传批次号", required = false) String processId);
	
	
	/**
	 * 导出商品
	 * @author zr.aoxianbing@yikuyi.com
	 * @param ids 活动商品id集合
	 * @param activityId 活动id
	 * @param periodsId 时间段id
	 * @param status 状态
	 */
	@ApiOperation(value = "导出草稿商品", notes = "作者：敖县兵<br>导出草稿商品", response = Void.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public void exportProducts(@ApiParam(value = "ids", required = false) String ids
			                   ,@ApiParam(value = "activityId", required = true) String activityId
			                   ,@ApiParam(value = "periodsId", required = true) String periodsId
			                   ,@ApiParam(value = "status", required = false) ActivityProductDraft.Status status
			                   ,HttpServletResponse response
			                   ) throws IOException;
	
	@ApiOperation(value = "定时刷新redis中的活动信息", notes = "作者：文娇<br>定时刷新redis中的活动信息", response = Void.class)
	@ApiResponse(code = 404, message = "访问的服务不存在", response = Void.class)
	public void activityTask() throws BusinessException;
	
	/**
	 * 删除活动商品草稿信息
	 * @param activityProductId
	 * @since 2017年6月14日
	 * @author tb.lijing@yikuyi.com
	 */
	@ApiOperation(value = "删除活动商品草稿信息", notes = "作者：李京<br>秒杀活动商品新增和编辑页面用的接口。将活动商品草稿信息删除", response = Void.class)
	public void deleteActivityProductDraft(@ApiParam(value = "activityProductId", required = true) String activityProductId,
			@ApiParam(value = "activityId", required = true) String activityId,@ApiParam(value = "periodsId",required=true)String periodsId);
	
	/**
	 * 批量删除活动商品草稿信息
	 * @param activityProductIds
	 * @since 2017年7月20日
	 * @author jik.shu@yikuyi.com
	 */
	@ApiOperation(value = "批量删除活动商品草稿信息", notes = "作者：舒作<br>秒杀活动商品新增和编辑页面用的接口。将活动商品草稿信息批量删除", response = Void.class)
	public void deleteActivityProductDraft(@ApiParam(value = "activityId", required = true) String activityId,@ApiParam(value = "periodsId",required=true)String periodsId,
			@ApiParam(value = "activityProductIds", required = true) List<String> activityProductIds,@ApiParam(value = "activityType",required=false)String activityType);

	/**
	 * 查询代金券
	 * @since 2017年8月1日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "查询活动代金券列表", notes = "作者：何林妹<br>查询活动代金券列表", response = MockActivityDataVo.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public PageInfo<MockActivityDataVo> listGift (@ApiParam(value = "活动", required = false) String activity,@ApiParam(value = "当前页", required = false) int page,
			@ApiParam(value = "当前页数量", required = false) int size);
	
	
	/**
	 * 保存代金券
	 * @since 2017年8月1日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "保存代金券", notes = "作者：何林妹<br>保存代金券", response = Void.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public void saveMockAndRealData()throws ParseException;


}
