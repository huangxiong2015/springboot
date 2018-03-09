package com.yikuyi.product.promotion.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
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

import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.product.promotion.IPromotionResource;
import com.yikuyi.product.promotion.bll.PromotionManager;
import com.yikuyi.promotion.model.Promotion;
import com.yikuyi.promotion.model.Promotion.PromotionStatus;
import com.yikuyi.promotion.model.Promotion.PromotionType;
import com.yikuyi.promotion.vo.PromotionParamVo;
import com.yikuyi.promotion.vo.PromotionProductVo;
import com.yikuyi.promotion.vo.PromotionVo;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.exception.SystemException;

@RestController
@RequestMapping("v1/promotions")
public class PromotionResource implements IPromotionResource {

	private static final Logger logger = LoggerFactory.getLogger(PromotionResource.class);

	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private PromotionManager promotionManager;

	/**
	 * 促销活动管理列表
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public PageInfo<PromotionVo> list(@RequestParam(value = "promotionType", required = false) PromotionType promotionType,@RequestParam(value = "promotionName", required = false) String promotionName,
			@RequestParam(value = "promotionStatus", required = false) String promotionStatus,
			@RequestParam(value = "timeStatus", required = false) String timeStatus,
			@RequestParam(value = "createDateStart", required = false) String createDateStart,
			@RequestParam(value = "createDateEnd", required = false) String createDateEnd,
			@RequestParam(value = "page", required = true, defaultValue = "1") int page,
			@RequestParam(value = "pageSize", required = true, defaultValue = "20") int pageSize) {
		PromotionParamVo paramVo = new PromotionParamVo();
		paramVo.setPromotionName(promotionName);
		paramVo.setPromotionType(promotionType);
		if (StringUtils.isNotBlank(promotionStatus)) {
			paramVo.setPromotionStatusList(Arrays.asList(promotionStatus.trim().split(",")));
		}
		paramVo.setTimeStatus(timeStatus);
		if (StringUtils.isNotBlank(createDateStart)) {
			Date startTimed;
			try {
				startTimed = format.parse(createDateStart);
			} catch (ParseException e) {
				logger.error("时间转换异常", e);
				throw new SystemException(e);
			}
			paramVo.setCreateDateStart(startTimed);
		}
		if (StringUtils.isNotBlank(createDateEnd)) {
			Date endTimed;
			try {
				endTimed = format.parse(createDateEnd);
			} catch (ParseException e) {
				logger.error("时间转换异常", e);
				throw new SystemException(e);
			}
			paramVo.setCreateDateEnd(endTimed);
		}
		RowBounds rowBounds = new RowBounds((page - 1) * pageSize, pageSize);
		return promotionManager.getPromotionList(paramVo, rowBounds);
	}

	/**
	 * 创建活动
	 * 
	 * @param promotion
	 * @return
	 * @since 2017年10月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST)
	public void save(@RequestBody Promotion promotion) {
		promotionManager.insert(promotion);
	}

	/**
	 * 在草稿表中复制促销活动草稿信息
	 */
	@Override
	@RequestMapping(value = "/reproduction/{promotionId}", method = RequestMethod.POST)
	public String draftCopy(@PathVariable(value = "promotionId", required = true) String promotionId) {
		String userId = RequestHelper.getLoginUserId();
		return promotionManager.draftCopy(promotionId, userId);
	}

	/**
	 * 编辑活动
	 * 
	 * @param promotion
	 * @return
	 * @since 2017年10月10日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody Promotion promotion) {
		promotionManager.update(promotion);
	}

	@Override
	@RequestMapping(value = "/{promoId}/status", method = RequestMethod.PUT)
	public void updatePromotionStatus(@PathVariable(required = true) String promoId,
			@RequestParam(required = true) PromotionStatus status) throws BusinessException {
		promotionManager.updatePromotionStatus(promoId, status);
	}

	/**
	 * 单个活动查询
	 * 
	 * @param promotion
	 * @return
	 * @since 2017年10月11日
	 * @author zr.helinmei@yikuyi.com
	 */
	@RequestMapping(value = "/{promotionId}", method = RequestMethod.GET)
	public Promotion getPromotion(@PathVariable(required = true) String promotionId) {
		return promotionManager.getPromotion(promotionId);
	}

	@Override
	@RequestMapping(value = "/{promotionId}/module/{promoModuleId}/product", method = RequestMethod.GET)
	public PageInfo<PromotionProductVo> listModuleProduct(
			@PathVariable(value = "promotionId", required = true) String promotionId,
			@PathVariable(value = "promoModuleId", required = true) String promoModuleId,
			@RequestParam(value = "draft", required = false) String draft,
			@RequestParam(value = "price", required = false) String price,
			@RequestParam(value = "page", required = true, defaultValue = "1") int page,
			@RequestParam(value = "pageSize", required = true, defaultValue = "20") int pageSize) {
		PromotionProductVo draftEntity = new PromotionProductVo();
		draftEntity.setPromotionId(promotionId);
		draftEntity.setPromoModuleId(promoModuleId);
		RowBounds rowBounds = new RowBounds((page - 1) * pageSize, pageSize);
		return promotionManager.listModuleProduct(draft, price, draftEntity, rowBounds);
	}

	/**
	 * 根据prmotionId查询所有的草稿模块json数据
	 */
	@Override
	@RequestMapping(value = "/{promotionId}/module/draft", method = RequestMethod.GET)
	public String getPromotionModuleDraft2Json(
			@PathVariable(value = "promotionId", required = true) String promotionId) {
		return promotionManager.getPromotionModuleDraft2Json(promotionId);
	}

	/**
	 * 根据prmotionId查询所有的模块json数据
	 */
	@Override
	@RequestMapping(value = "/{promotionId}/module", method = RequestMethod.GET)
	public String getPromotionModule2Json(@PathVariable(value = "promotionId", required = true) String promotionId) {
		return promotionManager.getPromotionModule2Json(promotionId);
	}

	/**
	 * 定时器处理缓存中的活动信息 在活动启动前一天添加缓存信息 在活动结束后一天清除缓存
	 */
	@Override
	@RequestMapping(value = "/cache", method = RequestMethod.PUT)
	public void handlePromotionCache(@RequestParam(value = "endDate", required = false) String endDate) {
		promotionManager.handlePromotionCache(endDate);
	}

}