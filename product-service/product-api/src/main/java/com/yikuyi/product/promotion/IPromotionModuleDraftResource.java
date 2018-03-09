package com.yikuyi.product.promotion;

import com.yikuyi.promotion.model.PromotionModuleContentDraft;
import com.yikuyi.promotion.model.PromotionModuleDraft;
import java.util.List;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 促销活动模块草稿服务
 * @author zr.helinmei@yikuyi.com
 * @version 1.0.0
 */
public interface IPromotionModuleDraftResource {
	/**
	 * 新增活动模块
	 * @param promotion
	 * @return
	 * @since 2017年10月10日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "新增活动模块", notes = "新增活动模块", response = PromotionModuleDraft.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void insert(@ApiParam(value = "新增活动模块", required = true) PromotionModuleDraft moduleDraft);

	
	/**
	 * 编辑草稿模块
	 * @param promotion
	 * @return
	 * @since 2017年10月10日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "编辑草稿模块", notes = "编辑草稿模块", response = PromotionModuleContentDraft.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void save(@ApiParam(value = "编辑草稿模块", required = true) PromotionModuleContentDraft moduleDraft);
	
	
	/**
	 * 删除促销活动模块数据
	 * @param promoModuleId
	 * @since 2017年10月10日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "删除促销活动模块数据", notes = "删除促销活动模块数据", response = Void.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public void deletePromotionModuleAndDraft(@ApiParam(value = "promoModuleId", required = true) String promoModuleId);
	
	
	/**
	 * 查询促销模块草稿详情
	 * @param promoModuleId
	 * @return
	 * @since 2017年10月11日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@ApiOperation(value = "查询促销模块草稿详情", notes = "查询促销模块草稿详情", response = PromotionModuleDraft.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public PromotionModuleDraft getPromotionModuleDraft (@ApiParam(value="promoModuleId",required=true) String promoModuleId);
	

	/**
	 * 模块排序接口
	 * @param promotion
	 * @return
	 * @since 2017年10月11日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "模块排序接口", notes = "模块排序接口", response = List.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void promotinModuleOrderSeq(@ApiParam(value = "模块排序接口", required = true) List<String> ids);
	
	
	

}
