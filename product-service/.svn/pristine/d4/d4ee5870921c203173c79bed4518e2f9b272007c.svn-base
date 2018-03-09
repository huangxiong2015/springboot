package com.yikuyi.product.promotion.impl;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yikuyi.product.promotion.IPromoModuleProductDraftResource;
import com.yikuyi.product.promotion.bll.PromoModuleProductDraftManager;
import com.yikuyi.promotion.model.PromoModuleProduct.ModuleProductStatus;
import com.yikuyi.promotion.model.PromotionModuleContentDraft;
import com.ykyframework.exception.BusinessException;
@RestController
@RequestMapping("v1/promoModuleProductDraft")
public class PromoModuleProductDraftResource implements IPromoModuleProductDraftResource{
	
	@Autowired
	private PromoModuleProductDraftManager productDraftManager;
	
	
	/**
	 * 创建商品模块保存草稿
	 * @param PromotionModuleDraft
	 * @return
	 * @since 2017年10月11日
	 * @author zr.helinmei@yikuyi.com
	 * @throws BusinessException 
	 */
	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public void save(@RequestBody PromotionModuleContentDraft moduleDraft) throws BusinessException {
		productDraftManager.save(moduleDraft);
	}

	/**
	 * 商品上传的文件解析
	 * @param promoModuleId
	 * @param fileUrl
	 * @param oriFileName
	 * @return
	 * @since 2017年10月11日
	 * @author tb.lijing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/products/parse", method = RequestMethod.POST)
	public void parseFile(@RequestParam(value = "promoModuleId", required = true) String promoModuleId,
			@RequestParam(value = "promotionId", required = true) String promotionId, 
			@RequestParam(value = "fileUrl", required = true)String fileUrl, 
			@RequestParam(value = "oriFileName", required = true) String oriFileName)throws BusinessException{
		productDraftManager.parseFile(promoModuleId, promotionId, fileUrl, oriFileName);
	}
	
	/**
	 * 批量删除活动装修商品草稿信息
	 * @param promoModuleId
	 * @param promotionId
	 * @param promoModuleProductIds
	 * @since 2017年10月14日
	 * @author tb.lijing@yikuyi.com
	 */
	@Override
	@RequestMapping(value = "/{promoModuleId}/promotionId/{promotionId}/products/delete", method = RequestMethod.DELETE)
	public void deletePromoModuleProductDraft(@PathVariable(value = "promoModuleId", required = true) String promoModuleId, 
			@PathVariable(value = "promotionId", required = true) String promotionId,
			@RequestBody(required = true) List<String> promoModuleProductIds) {
		productDraftManager.deletePromoModuleProductDraft(promoModuleId,promotionId,promoModuleProductIds);
	}

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
	@Override
	@RequestMapping(value = "/products/export", method = RequestMethod.GET)
	public void exportPromoModuleProducts(@RequestParam(required = false) String promoModuleProductIds,
			@RequestParam(required = true) String promoModuleId,@RequestParam(required = true) String promotionId,
			@RequestParam(required = false) ModuleProductStatus status, HttpServletResponse response) throws IOException {
		productDraftManager.exportProducts(promoModuleProductIds, promoModuleId, promotionId, status, response);
	}
}
