package com.yikuyi.product.promotion;

import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.yikuyi.promotion.model.Promotion;
import com.yikuyi.promotion.model.Promotion.PromotionStatus;
import com.yikuyi.promotion.model.Promotion.PromotionType;
import com.yikuyi.promotion.vo.PromotionProductVo;
import com.yikuyi.promotion.vo.PromotionVo;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 促销活动相关服务
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
public interface IPromotionResource {
	
	
	/**
	 * 促销活动管理列表
	 * @param promotionName
	 * @param promotionStatus
	 * @param startCreateDate
	 * @param endCreateDate
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 * @since 2017年10月9日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "促销活动管理列表", notes = "促销活动管理列表",response = Promotion.class,responseContainer = "List")
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PageInfo<PromotionVo> list(@RequestParam(value = "活动类型", required = false) PromotionType promotionType,@ApiParam(value="促销名称", required=false) String promotionName,
			@ApiParam(value = "活动状态 :无效DISABLE,有效ENABLE,删除DELETE;多个用逗号隔开", required = false) String promotionStatus,
			@ApiParam(value = "时间状态 :未开始NOTSTARTED,进行中ONGOING,已结束COMPLETE;最多只能选一个", required = false) String timeStatus,
			@ApiParam(value = "开始创建时间", required = false) String createDateStart,
			@ApiParam(value = "结束创建时间", required = false) String createDateEnd,			
			@ApiParam(value = "当前页", required = false) int page,
			@ApiParam(value = "当前页数量", required = false) int pageSize);
	
	/**
	 * 创建活动
	 * @param promotion
	 * @return
	 * @since 2017年10月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "创建活动", notes = "创建活动", response = Promotion.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void save(@ApiParam(value = "创建活动", required = true) Promotion promotion);
	
	
	/**
	 * 在草稿表中复制促销活动草稿信息
	 * @param promoModuleId
	 * @return
	 * @since 2017年10月10日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "在草稿表中复制促销活动草稿信息", notes = "在草稿表中复制促销活动草稿信息", response = Void.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public String draftCopy(@ApiParam(value = "promtionId", required = true)  String promtionId);

	
	/**
	 * 修改活动
	 * @param promotion
	 * @return
	 * @since 2017年10月11日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "修改活动", notes = "修改活动", response = Promotion.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void update(@ApiParam(value = "修改活动", required = true) Promotion promotion);
	
	/**
	 * 发布，停用，删除活动
	 * @since 2017年10月11日
	 * @author jik.shu@yikuyi.com
	 */
	@ApiOperation(value = "发布，停用，删除活动", notes = "发布，停用，删除活动", response = Void.class)
	public void updatePromotionStatus(@ApiParam(value = "promoId", required = true) String promoId, @ApiParam(value = "status", required = true) PromotionStatus status) throws BusinessException;


	/**
	 * 查询活动详情
	 * @param promoModuleId
	 * @return
	 * @since 2017年10月11日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "查询活动详情", notes = "查询活动详情", response = Promotion.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public Promotion getPromotion (@ApiParam(value="promotionId",required=true) String promotionId);
	
	/**
	 * 查询活动商品列表
	 * @param promoModuleId
	 * @return
	 * @since 2017年10月11日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "查询活动商品列表", notes = "查询活动商品列表", response = Promotion.class,responseContainer = "List")
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public PageInfo<PromotionProductVo> listModuleProduct(@ApiParam(value="活动id", required=true) String promotionId,
			@ApiParam(value = "模块id", required = true) String promoModuleId,
			@ApiParam(value = "是否是查询草稿", required = false) String draft,
			@ApiParam(value = "是否计算价格", required = false) String price,			
			@ApiParam(value = "当前页", required = false) int page,
			@ApiParam(value = "当前页数量", required = false) int pageSize);
	
	/**
	 * 根据prmotionId查询所有的草稿模块json数据
	 * @param promotionId
	 * @return
	 * @since 2017年10月11日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "查询所有的草稿模块json数据", notes = "查询所有的草稿模块json数据", response = String.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public String getPromotionModuleDraft2Json (@ApiParam(value="promotionId",required=true) String promotionId);
	
	
	/**
	 * 根据prmotionId查询所有的模块json数据
	 * @param promotionId
	 * @return
	 * @since 2017年10月11日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "查询所有的模块json数据", notes = "查询所有的模块json数据", response = String.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public String getPromotionModule2Json (@ApiParam(value="promotionId",required=true) String promotionId);
	
	
	/**
	 * 
	 * 定时器处理缓存中的活动信息
	 * 在活动启动前一天添加缓存信息
	 * 在活动结束后一天清除缓存
	 * @since 2017年10月13日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "定时器清除缓存中的活动信息", notes = "定时器清除缓存中的活动信息", response = Void.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public void handlePromotionCache(@ApiParam(value="endDate",required=false) String endDate);
	
}
