package com.yikuyi.product.promotion;

import com.yikuyi.promotion.model.Promotion;
import com.yikuyi.promotion.model.PromotionModule;
import com.yikuyi.promotion.vo.PromotionPreviewVo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

/**
 * 促销活动模块服务
 * @author zr.helinmei@yikuyi.com
 * @version 1.0.0
 */
public interface IPromotionModuleResource {
	

	/**
	 * 查询促销模块详情
	 * @param promoModuleId
	 * @return
	 * @since 2017年10月11日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "查询促销模块详情", notes = "查询促销模块详情", response = PromotionModule.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public PromotionModule getPromotionModule (@ApiParam(value="promoModuleId",required=true) String promoModuleId);
	
	/**
	 * 查询活动详情
	 * @param promoModuleId
	 * @return
	 * @since 2017年10月11日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "查询活动详情", notes = "查询活动详情", response = Promotion.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public PromotionPreviewVo getPromotionDetail (@ApiParam(value="活动id",required=true) String promotionId,
			@ApiParam(value="模块id",required=true) String promoModuleId,@ApiParam(value="模块类型",required=true) String promoModuleType,@ApiParam(value="正式还是非正式",required=false) String formal);
	

}
