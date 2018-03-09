package com.yikuyi.product.promotion.bll;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.io.netty.util.internal.StringUtil;
import com.yikuyi.product.promotion.dao.PromotionModuleDao;
import com.yikuyi.product.promotion.repository.PromotionModuleContentDraftRepository;
import com.yikuyi.product.promotion.repository.PromotionModuleContentRepository;
import com.yikuyi.promotion.model.PromotionModule;
import com.yikuyi.promotion.model.PromotionModuleContent;
import com.yikuyi.promotion.model.PromotionModuleContentDraft;
import com.yikuyi.promotion.vo.PromotionPreviewVo;

@Service
public class PromotionModuleManager {
	

	
	@Autowired
	private PromotionModuleDao promotionModuleDao;
	
	@Autowired
	private PromotionModuleContentDraftRepository moduleDraftRepository;
	
	@Autowired
	private PromotionModuleContentRepository moduleRepository;

	/**
	 * 查询模块详情
	 * @param promoModuleId
	 * @return
	 * @since 2017年10月11日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public PromotionModule getPromotionModule(String promoModuleId) {
		return promotionModuleDao.getPromotionModule(promoModuleId);
	}

	
	/**
	 * 
	 * @param promoModuleId
	 * @return
	 * @since 2017年10月11日
	 */
	public PromotionPreviewVo getPromotionDetail(String promotionId,String promoModuleId,String promoModuleType,String formal) {
		PromotionPreviewVo previewVo = new PromotionPreviewVo();
		JSONObject  jsonObject = null;
		if("Y".equals(formal)){
			 PromotionModuleContent draftList= moduleRepository.findOne(promoModuleId);
			 if(null != draftList){
			   jsonObject = draftList.getPromotionContent();
			 }
		}else{
		   PromotionModuleContentDraft  draftList= moduleDraftRepository.findOne(promoModuleId);
		   if(null != draftList){
		     jsonObject = draftList.getPromotionContent();
		   }
		}
		
		if(null == jsonObject){
			return null;
		}
		JSONObject secondJson = jsonObject.getJSONObject("contentSet");
		if(null != secondJson){
			String imageUrl = (String)secondJson.get("bannerImg");
			previewVo.setImageUrl(imageUrl);
			String showDesc = (String)secondJson.get("showDesc");
			previewVo.setShowRule(showDesc);
			if(!StringUtil.isNullOrEmpty(showDesc) && "true".equals(showDesc)){
				String content = (String)secondJson.get("content");
				previewVo.setRuleContent(content);
			}
		}
		return previewVo;
	}
	
	
}
