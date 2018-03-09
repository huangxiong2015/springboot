package com.yikuyi.product.specialoffer.manager;

import java.util.HashMap;
import java.util.Map;

import org.jasig.inspektr.audit.annotation.Audit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.RequestHelper;
import com.yikuyi.product.specialoffer.repository.SpecialOfferRepository;
import com.yikuyi.specialoffer.model.SpecialOffer;
import com.yikuyi.specialoffer.model.SpecialOffer.RuleStatus;

/**
 * 专属特价
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Service
public class SpecialOfferManager {
	
	@Autowired
	private SpecialOfferRepository specialOfferRepository;
	
	@Autowired
	private SpecialOfferCacheManager cacheManager;
	
	private static final String SUCCESS = "success";

	/**
	 * 专属特价详情
	 * @param id
	 * @return
	 * @since 2017年12月19日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public SpecialOffer getSpecialOffer(String id) {
		return specialOfferRepository.findOne(id);
	}
	
	/**
	 * 编辑文案内容
	 * @param id
	 * @param ruleText
	 * @return
	 * @since 2017年12月15日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Audit(action = "SpecialOffer Modifyqqq;;;'#id'qqq;;;编辑文案内容为:'#ruleText'",actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public String editRuleText(@com.framework.springboot.audit.Param("id") String id, @com.framework.springboot.audit.Param("ruleText")String ruleText) {
		String result = "fail";
		SpecialOffer specialOfferOld = specialOfferRepository.findOne(id);
		if(specialOfferOld!=null){
			SpecialOffer entity = new SpecialOffer();
			entity.setId(id);
			entity.setRuleStatus(specialOfferOld.getRuleStatus());
			entity.setRuleText(ruleText);
			entity.setCreator(specialOfferOld.getCreator());
			entity.setCreatorName(specialOfferOld.getCreatorName());
			entity.setUpdatedTimeMillis(String.valueOf(System.currentTimeMillis()));
			entity.setCreatedTimeMillis(specialOfferOld.getCreatedTimeMillis());
			entity.setLastUpdateUser(specialOfferOld.getLastUpdateUser());
			entity.setLastUpdateUserName(specialOfferOld.getLastUpdateUserName());
			SpecialOffer specialOffer = specialOfferRepository.save(entity);
			if(specialOffer!=null){
				result = SUCCESS;
			}
			Map<String, String> map = new HashMap<>();
			map.put("type",specialOfferOld.getRuleStatus().toString());
			map.put("ruleText", ruleText);
			//添加缓存
			cacheManager.initSpecialOfferTextCache(id, map);
		}
		return result;
	}

	/**
	 * 编辑状态
	 * @param id
	 * @param ruleStatus
	 * @return
	 * @since 2017年12月15日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Audit(action = "SpecialOffer Modifyqqq;;;'#id'qqq;;;编辑规则状态:'#statusName'",actionResolverName = "DEFAULT_ACTION_RESOLVER", resourceResolverName = "DEFAULT_RESOURCE_RESOLVER")
	public String editRuleStatus(@com.framework.springboot.audit.Param("id") String id,
			@com.framework.springboot.audit.Param("ruleStatus") RuleStatus ruleStatus,
			@com.framework.springboot.audit.Param("statusName") String statusName) {
		String result = "fail";
		LoginUser userInfo = RequestHelper.getLoginUser();
		SpecialOffer specialOfferOld = specialOfferRepository.findOne(id);
		if(specialOfferOld!=null){
			SpecialOffer entity = new SpecialOffer();
			entity.setId(id);
			entity.setRuleStatus(ruleStatus);
			entity.setRuleText(specialOfferOld.getRuleText());
			entity.setCreator(specialOfferOld.getCreator());
			entity.setCreatorName(specialOfferOld.getCreatorName());
			entity.setUpdatedTimeMillis(String.valueOf(System.currentTimeMillis()));
			entity.setCreatedTimeMillis(specialOfferOld.getCreatedTimeMillis());
			entity.setLastUpdateUser(specialOfferOld.getLastUpdateUser());
			entity.setLastUpdateUserName(specialOfferOld.getLastUpdateUserName());
			SpecialOffer specialOffer = specialOfferRepository.save(entity);
			if(specialOffer!=null){
				result = SUCCESS;
			}
			Map<String, String> map = new HashMap<>();
			map.put("type",ruleStatus.toString());
			map.put("ruleText", specialOfferOld.getRuleText());
			//添加缓存
			cacheManager.initSpecialOfferTextCache(id, map);
		}else{
			SpecialOffer entity = new SpecialOffer();
			entity.setId(id);
			entity.setRuleStatus(ruleStatus);
			entity.setRuleText("专属特价");
			entity.setCreator(userInfo.getId());
			entity.setCreatorName(userInfo.getUsername());
			entity.setUpdatedTimeMillis(String.valueOf(System.currentTimeMillis()));
			entity.setCreatedTimeMillis(String.valueOf(System.currentTimeMillis()));
			entity.setLastUpdateUser(userInfo.getId());
			entity.setLastUpdateUserName(userInfo.getUsername());
		    specialOfferRepository.save(entity);
		    result = SUCCESS;
		}
		return result;
	}


		 
}