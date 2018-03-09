package com.yikuyi.product.specialoffer.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.yikuyi.product.specialoffer.ISpecialOfferRuleResource;
import com.yikuyi.product.specialoffer.manager.SpecialOfferRuleManager;
import com.yikuyi.specialoffer.model.SpecialOfferRule;
import com.yikuyi.specialoffer.model.SpecialOfferRule.RuleType;
import com.yikuyi.specialoffer.vo.SpecialOfferRuleVo;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.model.IdGen;

/**
 * 专属特价
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@RestController
@RequestMapping("v1/specialOfferRule")
public class SpecialOfferRuleResource implements ISpecialOfferRuleResource {
	
	@Autowired
	private SpecialOfferRuleManager specialOfferRuleManager;

	/**
	 * 专属特价列表信息
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public PageInfo<SpecialOfferRuleVo> list(@RequestParam(value = "vendorId", required = false)String vendorId,
			@RequestParam(value = "ruleType", required = false)RuleType ruleType, 
			@RequestParam(value = "createdDateStart", required = false)String createdDateStart, 
			@RequestParam(value = "createDateEnd", required = false)String createDateEnd, 
			@RequestParam(value = "page", required = true, defaultValue = "1")int page, 
			@RequestParam(value = "pageSize", required = true, defaultValue = "20")int pageSize) {
		return specialOfferRuleManager.list(vendorId,ruleType,createdDateStart,createDateEnd,page,pageSize);
	}
	
	/**
	 * 查询规则详情
	 */
	@Override
	@RequestMapping(value="/detail",method = RequestMethod.GET)
	public SpecialOfferRuleVo getSpecialOfferRuleVo(@RequestParam(value = "id", required = true)String id) {
		return specialOfferRuleManager.getSpecialOfferRuleVo(id);
	}

	/**
	 * 删除专属特价规则
	 */
	@Override
	@RequestMapping(method = RequestMethod.DELETE)
	public void deleteRuleById(@RequestParam(value = "id", required = true)String id,
			@RequestParam(value = "vendorId", required = true)String vendorId) throws BusinessException{
		specialOfferRuleManager.deteteRuleById(id,vendorId);
	}

	/**
	 * 添加专属特价规则信息
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST)
	public void addSpecialOfferRule(@RequestParam(value = "ruleId", required = true)String ruleId,
			@RequestBody(required=true)SpecialOfferRule specialOfferRule) throws BusinessException{
		 	specialOfferRuleManager.addSpecialOfferRule(ruleId,specialOfferRule);
	}

	/**
	 * 生成规则id
	 * @return
	 * @since 2017年12月19日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/getRuleId",method = RequestMethod.GET)
	public String getRuleId() {
		return "\""+String.valueOf(IdGen.getInstance().nextId())+"\"";
	}

	/**
	 * 编辑专属特价规则信息
	 */
	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public void updateSpecialOfferRule(@RequestBody(required=true)SpecialOfferRule specialOfferRule) throws BusinessException{
		specialOfferRuleManager.updateSpecialOfferRule(specialOfferRule);
	}

	
	/**
	 * 将正式表中的数据迁移到草稿表中
	 */
	@Override
	@RequestMapping(value="copyProductToDraft",method = RequestMethod.POST)
	public void copyProductToDraft(@RequestParam(value = "ruleId", required = true)String ruleId) throws BusinessException{
		specialOfferRuleManager.copyProductToDraft(ruleId);
	}
	

}