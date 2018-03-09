package com.yikuyi.product.promotion.bll;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.framewrok.springboot.web.RequestHelper;
import com.yikuyi.product.promotion.dao.PromotionModuleDraftDao;
import com.yikuyi.product.promotion.repository.PromotionModuleContentDraftRepository;
import com.yikuyi.promotion.model.PromotionModuleContentDraft;
import com.yikuyi.promotion.model.PromotionModuleDraft;
import com.yikuyi.promotion.model.PromotionModuleDraft.PromoModuleStatus;
import com.ykyframework.model.IdGen;

@Service
public class PromotionModuleDraftManager {
		
	private static final Logger logger = LoggerFactory.getLogger(PromotionModuleDraftManager.class);
	

	
	@Autowired
	private PromotionModuleDraftDao promotionModuleDraftDao;
	
	@Autowired
	private PromotionModuleContentDraftRepository moduleDraftRepository;
	
	/**
	 * 创建活动
	 * @param promotion
	 * @param rowBounds
	 * @return
	 * @since 2017年10月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void insert(PromotionModuleDraft promotionDraft) {
		String id = String.valueOf(IdGen.getInstance().nextId());
		String userId = RequestHelper.getLoginUserId();
		promotionDraft.setPromoModuleId(id);
		promotionDraft.setCreatorName(RequestHelper.getLoginUser().getUsername());
		promotionDraft.setCreator(userId);
		promotionDraft.setCreatedDate(new Date());
		promotionDraft.setLastUpdateDate(new Date());
		promotionDraft.setPromoModuleStatus(PromoModuleStatus.ENABLE);
		promotionDraft.setOrderSeq(999);  //添加模块的时候默认排序为999，使其在列表中沉到最后
		promotionModuleDraftDao.insert(promotionDraft);
		
	}

	/**
	 * 编辑草稿模块
	 * @param promotion
	 * @param rowBounds
	 * @return
	 * @since 2017年10月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void save(PromotionModuleContentDraft moduleContentDraft) {
		logger.info("前台传值{}:"+moduleContentDraft.getPromoModuleId()+"===="+moduleContentDraft.getPromotionId()+"====="+moduleContentDraft.getPromoModuleType());
		PromotionModuleContentDraft module = moduleDraftRepository.findOne(moduleContentDraft.getPromoModuleId());
		String userId = RequestHelper.getLoginUserId();
		moduleContentDraft.setStatus("ENABLE");
		if(null != module){
			logger.info("后台查询出来的值{}:"+module.getPromoModuleId()+"===="+module.getPromotionId());
			moduleContentDraft.setLastUpdateDate(new Date());
			moduleContentDraft.setLastUpdateUser(userId);
			moduleDraftRepository.save(moduleContentDraft);	
		}else{
			moduleContentDraft.setCreatedDate(new Date());
			moduleContentDraft.setCreator(userId);
			moduleContentDraft.setLastUpdateDate(new Date());
			moduleContentDraft.setLastUpdateUser(userId);
			moduleDraftRepository.insert(moduleContentDraft);
		}
		
		
	}

	/**
	 * 删除促销活动模块数据
	 * @param promoModuleId
	 * @since 2017年10月11日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public void deletePromotionModuleAndDraft(String promoModuleId) {		
		//修改促销模块草稿数据状态
		PromotionModuleDraft draft = new PromotionModuleDraft();
		draft.setPromoModuleId(promoModuleId);
		draft.setPromoModuleStatus(PromotionModuleDraft.PromoModuleStatus.DELETE);
		promotionModuleDraftDao.updatePromotionModuleDraft(draft);
		PromotionModuleContentDraft promotionModuleContentDraft = new PromotionModuleContentDraft();
		promotionModuleContentDraft.setPromoModuleId(promoModuleId);
		promotionModuleContentDraft.setStatus(PromotionModuleDraft.PromoModuleStatus.DELETE.toString());
		moduleDraftRepository.save(promotionModuleContentDraft);
		
	}
	
	/**
	 * 模块排序接口。
	 * @param promotion
	 * @param rowBounds
	 * @return
	 * @since 2017年10月9日
	 * @author zr.helinmei@yikuyi.com
	 */
	public void promotinModuleOrderSeq(List<String> ids){
		if(CollectionUtils.isEmpty(ids)){
			return;
		}
		String userId = RequestHelper.getLoginUserId();
		for(int i=0;i<ids.size();i++){
			PromotionModuleDraft draft = new PromotionModuleDraft();
			draft.setPromoModuleId(ids.get(i));
			draft.setOrderSeq(i+1);
			draft.setLastUpdateDate(new Date());
			draft.setLastUpdateUser(userId);
			draft.setLastUpdateUserName(RequestHelper.getLoginUser().getUsername());
			promotionModuleDraftDao.updatePromotionModuleDraft(draft);
		}
	}

	
	/**
	 *  查询促销模块草稿详情
	 * @param promoModuleId
	 * @return
	 * @since 2017年10月11日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public PromotionModuleDraft getPromotionModuleDraft(String promoModuleId) {
		return promotionModuleDraftDao.getPromotionModuleDraft(promoModuleId);
	}
	
	
	
}
