package com.yikuyi.product.promotion;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;

import com.yikuyi.promotion.model.PromoModuleProduct.ModuleProductStatus;
import com.yikuyi.promotion.model.PromotionModuleContentDraft;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 促销活动商品草稿服务
 * @author zr.helinmei@yikuyi.com
 * @version 1.0.0
 */
public interface IPromoModuleProductDraftResource {

	/**
	 * 编辑商品草稿模块
	 * @param promotion
	 * @return
	 * @since 2017年10月10日
	 * @author zr.helinmei@yikuyi.com
	 */
	@ApiOperation(value = "编辑商品草稿模块", notes = "编辑商品草稿模块", response = PromotionModuleContentDraft.class)
	@ApiResponses(value ={ 
			@ApiResponse(code = 400, message = "请求参数不正确", response = Void.class) ,
			@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class) 
			})
	public void save(@ApiParam(value = "编辑商品草稿模块", required = true) PromotionModuleContentDraft moduleDraft)throws BusinessException;
	
	/**
	 * 商品上传的文件解析
	 * @param promoModuleId
	 * @param fileUrl
	 * @param oriFileName
	 * @return
	 * @since 2017年10月11日
	 * @author tb.lijing@yikuyi.com
	 */
	@ApiOperation(value = "商品上传的文件解析", notes = "作者：李京<br>活动商品编辑页面用的接口。将阿里云文件下载，然后解析，匹配数据，存入草稿", response = Void.class)
	public void parseFile(@ApiParam("促销模块id") String promoModuleId,@ApiParam("促销活动Id") String promotionId,@ApiParam("阿里云文件路径")String fileUrl,@ApiParam("文件原始名称")String oriFileName)throws BusinessException;
	
	/**
	 * 批量删除活动装修商品草稿信息
	 * @param promoModuleId
	 * @param promotionId
	 * @param promoModuleProductIds
	 * @since 2017年10月14日
	 * @author tb.lijing@yikuyi.com
	 */
	@ApiOperation(value = "批量删除活动装修商品草稿信息", notes = "作者：李京<br>活动装修商品新增和编辑页面用的接口。将活动装修商品草稿信息批量删除", response = Void.class)
	public void deletePromoModuleProductDraft(@PathVariable(value = "促销模块id", required = true) String promoModuleId, @PathVariable(value = "促销活动Id", required = true) String promotionId,@ApiParam(value = "促销模块商品id数组", required = true) List<String> promoModuleProductIds);
	
	/**
	 * 导出活动装修商品草稿信息
	 * @param promoModuleProductIds
	 * @param promoModuleId
	 * @param promotionId
	 * @param status
	 * @param response
	 * @throws IOException
	 * @since 2017年10月18日
	 * @author tb.lijing@yikuyi.com
	 */
	@ApiOperation(value = "导出活动装修商品草稿信息", notes = "作者：李京<br>导出活动装修商品草稿信息", response = Void.class)
	@ApiResponse(code = 404, message = "访问的页面不存在", response = Void.class)
	public void exportPromoModuleProducts(@ApiParam(value = "promoModuleProductIds", required = false) String promoModuleProductIds
			                   ,@ApiParam(value = "促销模块id", required = true) String promoModuleId
			                   ,@ApiParam(value = "促销活动Id", required = true) String promotionId
			                   ,@ApiParam(value = "status", required = false) ModuleProductStatus status
			                   ,HttpServletResponse response
			                   ) throws IOException;

}
