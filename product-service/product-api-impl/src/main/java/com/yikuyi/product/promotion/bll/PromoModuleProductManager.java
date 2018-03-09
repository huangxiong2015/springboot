
package com.yikuyi.product.promotion.bll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.yikuyi.product.promotion.dao.PromoModuleProductDraftDao;
import com.yikuyi.product.promotion.repository.PromotionModuleContentDraftRepository;
import com.yikuyi.promotion.model.PromoModuleProduct.ModuleProductStatus;
import com.yikuyi.promotion.model.PromoModuleProductDraft;
import com.yikuyi.promotion.model.PromotionModuleContentDraft;
import com.yikuyi.promotion.vo.DiscountVo;
import com.yikuyi.promotion.vo.PromoModuleProductVo;
import com.yikuyi.promotion.vo.PromotionFlagVo;
import com.yikuyi.promotion.vo.PromotionModuleEffectiveVo;
import com.ykyframework.exception.SystemException;

@Service
public class PromoModuleProductManager {
	
	@Autowired
	private PromoModuleProductDraftDao promoModuleProductDraftDao;
	
	@Autowired
	private PromotionModuleContentDraftRepository promotionModuleContentDraftRepository;
	
	/**
	 * 根据模块排序获取活动下所有有效商品，并且技术补充折扣和标识信息
	 * @param promotionId
	 * @return
	 * @since 2017年10月13日
	 * @author jik.shu@yikuyi.com
	 */
	public List<PromoModuleProductVo> getAllModuleProduct(String promotionId){
		try {
			//update by zr.wanghong 此处有个bug，应该查询草稿表，用于list比较判断是新增还是修改相关的处理
			PromoModuleProductDraft promoModuleProductDraft = new PromoModuleProductDraft();
			promoModuleProductDraft.setPromotionId(promotionId);
			promoModuleProductDraft.setStatus(ModuleProductStatus.ENABLE);
			List<PromoModuleProductDraft> productList = promoModuleProductDraftDao.findRealDraftProduct(promoModuleProductDraft);
			if(CollectionUtils.isEmpty(productList)){
				return Collections.emptyList();
			}
			Set<String> modules = productList.stream().map(PromoModuleProductDraft::getPromoModuleId).collect(Collectors.toSet());
			
			//update by zr.wanghong 规则也查询草稿的
			Iterable<PromotionModuleContentDraft> moduleContent = promotionModuleContentDraftRepository.findAll(modules);
			Iterator<PromotionModuleContentDraft> iteratorModule = moduleContent.iterator();
			ObjectMapper ob = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
			Map<String,DiscountVo> discountMap = new HashMap<>();
			Map<String,Boolean> useStockQtyMap = new HashMap<>();
			Map<String,PromotionFlagVo> promotionFlagVoMap = new HashMap<>();
			
			Map<String, PromotionModuleEffectiveVo> promModuleEffectiveVoMap = new HashMap<>();
			while (iteratorModule.hasNext()) {
				PromotionModuleContentDraft content = iteratorModule.next();
				if(null == content || null== content.getPromotionContent()){
					continue;
				}
				JSONObject contentSetObj = content.getPromotionContent().getJSONObject("contentSet");
				DiscountVo discountVo = ob.readValue(contentSetObj.getString("discount"), DiscountVo.class);
				discountMap.put(content.getPromoModuleId(), discountVo);
				boolean useStockQty = contentSetObj.getJSONObject("uploadData").getBoolean("useStockQty");
				useStockQtyMap.put(content.getPromoModuleId(), useStockQty);
				PromotionFlagVo promotionFlagVo = ob.readValue(contentSetObj.getString("promotionFlag"), PromotionFlagVo.class);
				promotionFlagVoMap.put(content.getPromoModuleId(), promotionFlagVo);
				
				if(contentSetObj.containsKey("effectSet")){
					//获取模块的生效时间，放入map中，判断当前时间是否在生效时间内，不在生效时间不加入缓存
					PromotionModuleEffectiveVo promotionModuleEffectiveVo = ob.readValue(contentSetObj.getString("effectSet"), PromotionModuleEffectiveVo.class);
					promModuleEffectiveVoMap.put(content.getPromoModuleId(), promotionModuleEffectiveVo);
				}
				
			}
			
			List<PromoModuleProductVo> rstProductList = new ArrayList<>();
			for(int i=0;i<productList.size();i++){
				PromoModuleProductVo newVo = ob.readValue(ob.writeValueAsString(productList.get(i)), PromoModuleProductVo.class);
				newVo.setDiscountVo(discountMap.get(productList.get(i).getPromoModuleId()));
				newVo.setUseStockQty(null == useStockQtyMap.get(productList.get(i).getPromoModuleId()) ? false : useStockQtyMap.get(productList.get(i).getPromoModuleId()));
				newVo.setPromotionFlag(promotionFlagVoMap.get(productList.get(i).getPromoModuleId()));
				//update by zr.wanghong不在生效时间内则不加入list
				PromotionModuleEffectiveVo effectiveVo = promModuleEffectiveVoMap.get(productList.get(i).getPromoModuleId());
				newVo.setPromotionModuleEffectiveVo(effectiveVo);
				rstProductList.add(newVo);
			}
			return rstProductList;
		} catch (Exception e) {
			throw new SystemException(e.getMessage(),e);
		} 
	}
	
	/**
	 * 根据传人商品合并模块规则
	 * @param productList
	 * @return
	 * @since 2017年10月17日
	 * @author jik.shu@yikuyi.com
	 */
	public List<PromoModuleProductVo> getAllModuleProductByDraft(List<PromoModuleProductDraft> productList) {
		List<PromoModuleProductVo> rstProductList = new ArrayList<>();
		try {
			Set<String> modules = productList.stream().map(PromoModuleProductDraft::getPromoModuleId).collect(Collectors.toSet());
			Iterable<PromotionModuleContentDraft> moduleContent = promotionModuleContentDraftRepository.findAll(modules);
			Iterator<PromotionModuleContentDraft> iteratorModule = moduleContent.iterator();
			ObjectMapper ob = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
			Map<String, DiscountVo> discountMap = new HashMap<>();
			Map<String, Boolean> useStockQtyMap = new HashMap<>();
			Map<String, PromotionFlagVo> promotionFlagVoMap = new HashMap<>();
			while (iteratorModule.hasNext()) {
				PromotionModuleContentDraft content = iteratorModule.next();
				if(null == content){
					continue;
				}
				JSONObject contentSetObj = content.getPromotionContent().getJSONObject("contentSet");
				DiscountVo discountVo = ob.readValue(contentSetObj.getString("discount"), DiscountVo.class);
				discountMap.put(content.getPromoModuleId(), discountVo);
				boolean useStockQty = contentSetObj.getJSONObject("uploadData").getBoolean("useStockQty");
				useStockQtyMap.put(content.getPromoModuleId(), useStockQty);
				PromotionFlagVo promotionFlagVo = ob.readValue(contentSetObj.getString("promotionFlag"), PromotionFlagVo.class);
				promotionFlagVoMap.put(content.getPromoModuleId(), promotionFlagVo);
			}

			for (int i = 0; i < productList.size(); i++) {
				PromoModuleProductVo newVo = ob.readValue(ob.writeValueAsString(productList.get(i)), PromoModuleProductVo.class);
				/*BeanUtils.copyProperties(newVo, productList.get(i));*/
				newVo.setDiscountVo(discountMap.get(productList.get(i).getPromoModuleId()));
				newVo.setUseStockQty(null == useStockQtyMap.get(productList.get(i).getPromoModuleId()) ? false : useStockQtyMap.get(productList.get(i).getPromoModuleId()));
				newVo.setPromotionFlag(promotionFlagVoMap.get(productList.get(i).getPromoModuleId()));
				rstProductList.add(newVo);
			}
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
		return rstProductList;
	}
	
}
