package com.yikuyi.product.activity.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.activity.model.Activity;
import com.yikuyi.activity.model.Activity.Status;
import com.yikuyi.activity.model.ActivityProductDraft;
import com.yikuyi.activity.vo.ActivityProductDraftVo;
import com.yikuyi.activity.vo.ActivityProductVo;
import com.yikuyi.activity.vo.ActivityVo;
import com.yikuyi.activity.vo.MockActivityDataVo;
import com.yikuyi.product.activity.IActivityResource;
import com.yikuyi.product.activity.bll.ActivityManager;
import com.yikuyi.product.activity.bll.RegisterActivityMockManager;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.exception.DataNotFoundException;

/**
 * 活动相关服务
 * 
 * @author zr.wenjiao@yikuyi.com
 * @version 1.0.0
 */
@RestController
@RequestMapping("v1/activities")
public class ActivityResource implements IActivityResource {
	
	private static final Logger logger = LoggerFactory.getLogger("activity");
	
	
	@Autowired
	private ActivityManager activityManager;

	@Autowired
	private RegisterActivityMockManager registerActivityMockManager;

	/**
	 * 活动管理启用、停用 当传入的活动状态为启用时，先判断活动结束时间是否小于当前时间
	 * 结束时间已过不能启用活动，反则改变活动的状态为启用中，并判断活动开始时间是否在当天或者明天，如果是则把活动放入redis中
	 * 当传入的活动状态为停用时，改变活动的状态为停用中，并把活动从redis缓存中清除
	 * 
	 * @param record
	 * @since 2017年6月9日
	 * @author zr.wenjiao@yikuyi.com
	 * @throws ParseException
	 */
	@Override
	@RequestMapping(value = "/{activityId}/{status}", method = RequestMethod.PUT)
	public void updateStatus(@PathVariable(required = true) String activityId, @PathVariable(required = true) Activity.Status status) throws BusinessException, ParseException {
		activityManager.updateActivityStatus(activityId,status);
	}

	/**
	 * 将草稿活动信息转化成正式活动信息
	 * 
	 * @author zr.aoxianbing@yikuyi.com
	 * @param activityId 活动id
	 * @throws BusinessException
	 */
	@Override
	@RequestMapping(value = "/{activityId}/draft", method = RequestMethod.PUT)
	public String draftToFormal(@PathVariable(required = true) String activityId) throws BusinessException {
		String userId = RequestHelper.getLoginUserId();
		LoginUser user = RequestHelper.getLoginUser();
		String userName = null;
		if (user != null) {
			userName = user.getUsername();
		}
		return activityManager.draftToFormal(activityId, userId, userName);
	}

	/**
	 * 判断时区是否有效，并更新无活动商品时区的状态为无效
	 * 
	 * @param activityId
	 * @param periods
	 * @throws BusinessException
	 * @since 2017年7月26日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/{activityId}/periods", method = RequestMethod.PUT)
	public void updatePeriodsStatus(@PathVariable(required = true) String activityId) throws BusinessException {
		activityManager.updatePeriodsStatus(activityId);
	}

	/**
	 * 查询活动管理列表
	 * 
	 * @since 2017年6月8日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public PageInfo<ActivityVo> list(@RequestParam(value = "name", required = false) String name, 
									 @RequestParam(value = "type", required = false) String type, 
									 @RequestParam(value = "prStatus", required = false) String prStatus,
									 @RequestParam(value = "startTime", required = false) String startTime, 
									 @RequestParam(value = "endTime", required = false) String endTime, 
									 @RequestParam(value = "page", required = true, defaultValue = "1") int page,
									 @RequestParam(value = "pageSize", required = true, defaultValue = "20") int pageSize) throws Exception {
		return activityManager.findActivityByEntity(name, type, prStatus, startTime, endTime, page, pageSize);
	}

	/**
	 * 查询正式活动详情
	 * 
	 * @since 2017年6月8日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/{activityId}/standard", method = RequestMethod.GET)
	public ActivityVo getSearchActivityStandard(@PathVariable(required = true) String activityId) {
		return activityManager.findActivityStandardById(activityId);
	}

	/**
	 * 查询草稿活动详情
	 * 
	 * @since 2017年6月8日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/{activityId}/draft", method = RequestMethod.GET)
	public ActivityVo getSearchActivityDraft(@PathVariable(required = true) String activityId) {
		return activityManager.findActivityDraftById(activityId);
	}

	/**
	 * 修改活动详情
	 * 
	 * @since 2017年6月8日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/{activityId}/draft", method = RequestMethod.POST)
	public ActivityVo updateActivityDraft(@RequestBody(required = true) ActivityVo activityVo) throws Exception {
		LoginUser userInfo = RequestHelper.getLoginUser();
		activityVo.setLastUpdateUserName(userInfo.getUsername());
		return activityManager.updatActivityDraft(activityVo);

	}

	/**
	 * 保存草稿活动详情
	 * @since 2017年6月8日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/draft", method = RequestMethod.POST)
	public ActivityVo saveActivityDraft(@RequestBody(required = true) ActivityVo activityVo) throws Exception {
		LoginUser userInfo = RequestHelper.getLoginUser();
		activityVo.setCreatorName(userInfo.getUsername());
		activityVo.setLastUpdateUserName(userInfo.getUsername());
		activityVo.setStatus(Status.ENABLE);
		return activityManager.saveActivityDraft(activityVo);
	}

	/**
	 * 保存草稿活动详情
	 * 
	 * @since 2017年6月8日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST)
	public Activity saveActivity(@RequestBody(required = true) ActivityVo activityVo) throws BusinessException {
		return activityManager.saveActivity(activityVo);
	}

	/**
	 * 修改活动详情
	 * 
	 * @since 2017年6月8日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/{activityId}", method = RequestMethod.PUT)
	public Activity updateActivity(@RequestBody(required = true) ActivityVo activityVo) throws BusinessException {
		return activityManager.updatActivity(activityVo);
	}

	/**
	 * 活动商品上传的文件解析
	 * 
	 * @param activityId
	 * @param periodsId
	 * @param fileUrl
	 * @param oriFileName
	 * @since 2017年6月8日
	 * @author tb.lijing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "{activityId}/products/parse", method = RequestMethod.POST)
	public String parseFile(@PathVariable(value = "activityId", required = true) String activityId, @RequestParam(value = "periodsId", required = true) String periodsId, @RequestParam(value = "fileUrl", required = true) String fileUrl,
			@RequestParam(value = "oriFileName", required = true) String oriFileName) throws BusinessException {
		return activityManager.parseFile(activityId, periodsId, fileUrl, oriFileName);
	}

	/**
	 * 查询活动商品已上传的正式表数据
	 * 
	 * @param activityId
	 * @param periodsId
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2017年6月8日
	 * @author tb.lijing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "{activityId}/products/standard", method = RequestMethod.GET)
	public PageInfo<ActivityProductVo> findActivityProduct(@PathVariable(value = "activityId", required = false) String activityId, 
			@RequestParam(value = "periodsId", required = false) String periodsId,
			@RequestParam(value = "priceFlag", required = false) String priceFlag, 
			@RequestParam(value = "activityType", required = false) String activityType,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page, 
			@RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize) {
		return activityManager.findActivityProductList(activityId, periodsId, priceFlag, activityType);
	}

	/**
	 * 查询活动商品已上传的草稿数据
	 * 
	 * @param activityId
	 * @param periodsId
	 * @param page
	 * @param pageSize
	 * @return
	 * @since 2017年6月9日
	 * @author tb.lijing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "{activityId}/products/draft", method = RequestMethod.GET)
	public PageInfo<ActivityProductDraftVo> findActivityProductDraft(@PathVariable(value = "activityId", required = true) String activityId, @RequestParam(value = "periodsId", required = false) String periodsId,
			@RequestParam(value = "status", required = false) ActivityProductDraft.Status status, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10000") int pageSize, @RequestParam(value = "processId", required = false) String processId) {
		ActivityProductDraft vo = new ActivityProductDraft();
		vo.setActivityId(activityId);
		vo.setPeriodsId(periodsId);
		vo.setStatus(status);
		vo.setProcessId(processId);
		RowBounds rowBounds = new RowBounds((page - 1) * pageSize, pageSize);
		return activityManager.findActivityProductDraftByCondition(vo, rowBounds);
	}

	/**
	 * 导出活动草稿商品
	 * 
	 * @author zr.aoxianbing@yikuyi.com
	 * @param ids
	 *            活动商品id集合
	 * @param activityId
	 *            活动id
	 * @param periodsId
	 *            时间段id
	 * @param status
	 *            状态
	 */
	@Override
	@RequestMapping(value = "/{activityId}/export", method = RequestMethod.GET)
	public void exportProducts(@RequestParam(required = false) String ids, @PathVariable(required = true) String activityId, @RequestParam(required = true) String periodsId,
			@RequestParam(required = false) ActivityProductDraft.Status status, HttpServletResponse response) throws IOException {
		String userId = RequestHelper.getLoginUserId();
		if (userId == null || "".equals(userId)) {
			logger.error("无法获取用户的id");
			return;
		}
		activityManager.exportProducts(ids, activityId, periodsId, status, response);
	}

	/**
	 * 定时刷新redis中的活动信息 先查询已经结束的活动并从redis中清空 后查询即将生效的活动并存放redis中
	 * 
	 * @since 2017年6月12日
	 * @author zr.wenjiao@yikuyi.com
	 * @throws BusinessException
	 */
	@Override
	@RequestMapping(value = "/refresh/task", method = RequestMethod.GET)
	public void activityTask() throws BusinessException {
		activityManager.activityTask();
	}

	/**
	 * 删除活动管理
	 * 
	 * @since 2017年6月8日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/{activityId}/delete/activity", method = RequestMethod.DELETE)
	public void deleteActivity(@PathVariable(required = true) String activityId) {
		activityManager.deleteActivity(activityId);
	}

	/**
	 * 删除活动商品草稿信息
	 * 
	 * @param activityProductId
	 * @since 2017年6月14日
	 * @author tb.lijing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/{activityProductId}/delete", method = RequestMethod.DELETE)
	public void deleteActivityProductDraft(@PathVariable(required = true) String activityProductId, @RequestParam(value = "activityId", required = true) String activityId,
			@RequestParam(value = "periodsId", required = true) String periodsId) {
		activityManager.deleteActivityProductDraft(activityProductId, activityId, periodsId);
	}

	/**
	 * 删除活动商品草稿信息
	 * 
	 * @param activityProductId
	 * @since 2017年6月14日
	 * @author tb.lijing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/{activityId}/periods/{periodsId}/products", method = RequestMethod.DELETE)
	public void deleteActivityProductDraft(@PathVariable(value = "activityId", required = true) String activityId, @PathVariable(value = "periodsId", required = true) String periodsId,
			@RequestBody(required = true) List<String> activityProductIds, @RequestParam(value = "activityType", required = false) String activityType) {
		activityManager.deleteActivityProductDraft(activityProductIds, activityId, periodsId, activityType);
	}

	/**
	 * 查询当天正式获得详情（秒杀页面）
	 * 
	 * @since 2017年6月13日
	 * @author zr.zhanghua@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/todayactivity", method = RequestMethod.GET)
	public ActivityVo getTodayActivityStandard() {
		ActivityVo vo = activityManager.getTodayActivity();
		if (vo == null) {
			throw new DataNotFoundException("今天没有生效的活动");
		}
		return vo;
	}

	@Override
	@RequestMapping(value = "/todayactivity/{activityId}", method = RequestMethod.GET)
	public ActivityVo getActivityStandardById(@PathVariable(value = "activityId", required = true) String activityId) {
		ActivityVo vo = activityManager.getActivityStandardById(activityId);
		if (vo == null) {
			throw new DataNotFoundException("活动不存在或者已经结束");
		}
		return vo;
	}

	/**
	 * 查询代金券
	 * 
	 * @since 2017年8月1日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/listGift", method = RequestMethod.GET)
	public PageInfo<MockActivityDataVo> listGift(@RequestParam(value = "activity", defaultValue = "REGISTER") String activity,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "size", required = false, defaultValue = "20") int size) {
		RowBounds rowBounds = new RowBounds((page - 1) * size, size);
		return registerActivityMockManager.listGift(activity, rowBounds);
	}

	/**
	 * 保存代金券
	 * 
	 * @since 2017年8月1日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/generateMockData", method = RequestMethod.GET)
	public void saveMockAndRealData() throws ParseException {
		registerActivityMockManager.generateMockData();
	}

}
