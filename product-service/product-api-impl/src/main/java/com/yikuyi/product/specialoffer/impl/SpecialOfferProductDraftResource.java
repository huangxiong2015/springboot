/*
 * Created: 2017年12月20日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.specialoffer.impl;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.yikuyi.product.specialoffer.ISpecialOfferProductDraftResource;
import com.yikuyi.product.specialoffer.manager.SpecialOfferProductDraftManager;
import com.yikuyi.specialoffer.model.SpecialOfferProduct.Status;
import com.yikuyi.specialoffer.model.SpecialOfferProductDraft;
import com.yikuyi.specialoffer.vo.SpecialOfferProductDraftVo;
import com.ykyframework.exception.BusinessException;

@RestController
@RequestMapping("v1/specialOfferProductDraft")
public class SpecialOfferProductDraftResource implements ISpecialOfferProductDraftResource{
	@Autowired
	private SpecialOfferProductDraftManager specialOfferProductDraftManager;
	/**
	 * 上传的文件解析
	 */
	@Override
	@RequestMapping(value = "/parse", method = RequestMethod.POST)
	public void parseFile(@RequestBody SpecialOfferProductDraftVo spDraftVo) throws BusinessException {
		specialOfferProductDraftManager.parseFile(spDraftVo);
	}

	/**
	 * 分页查询专属特价商品草稿数据
	 */
	@Override
	@RequestMapping(value="/getDraft",method = RequestMethod.GET)
	public PageInfo<SpecialOfferProductDraft> findSpecialOfferProductDraftByPage(@RequestParam(value = "ruleId", required = true)String ruleId, 
			@RequestParam(value = "page", required = false, defaultValue = "1")int page, 
			@RequestParam(value = "pageSize", required = false, defaultValue = "20")int pageSize) {
		return specialOfferProductDraftManager.findSpecialOfferProductDraftByPage(ruleId, page, pageSize);
	}

	/**
	 * 删除专属特价商品草稿数据
	 */
	@Override
	@RequestMapping(value="/delete",method = RequestMethod.DELETE)
	public void deleteSpecialOfferProductDraft(@RequestBody(required = true)List<String> ids) {
		specialOfferProductDraftManager.deleteSpecialOfferProductDraft(ids);
	}

	/**
	 * 导出专属特价商品草稿数据
	 */
	@Override
	@RequestMapping(value="/export",method = RequestMethod.GET)
	public void exportSpecialOfferProductDrafts(@RequestParam(value = "ids", required = false)String ids, 
			@RequestParam(value = "ruleId", required = true)String ruleId, 
			@RequestParam(value = "status", required = false)Status status,
			HttpServletResponse response) throws IOException {
		specialOfferProductDraftManager.exportSpecialOfferProductDrafts(ids, ruleId, status, response);
	}
	
	/**
	 * 添加草稿商品信息
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST)
	public void addSpecialOfferProductDraft(@RequestBody(required=true)SpecialOfferProductDraft productDraft) {
		specialOfferProductDraftManager.addSpecialOfferProductDraft(productDraft);
	}

}