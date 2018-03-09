/*
 * Created: 2017年2月23日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.goods.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framewrok.springboot.web.RequestHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.product.goods.IProductStandAuditResource;
import com.yikuyi.product.goods.manager.ProductStandAuditManager;
import com.yikuyi.product.model.ProductStandAudit;
import com.yikuyi.product.vo.ProductStandAuditRequest;
import com.yikuyi.product.vo.ProductStandRequest;
import com.ykyframework.exception.BusinessException;
/**
 * 物料审核
 * @author zr.wujiajun@yikuyi.com
 * @version 1.0.0
 */
@RestController
@RequestMapping("v1/products/standaudit")
public class ProductStandAuditResource implements IProductStandAuditResource {

	@Autowired
	private ProductStandAuditManager productStandAuditManager;
	private static final Logger logger = LoggerFactory.getLogger(ProductStandAuditResource.class);
	
	/**
	 * 列表查询
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public PageInfo<ProductStandAudit> search(@RequestParam(value="manufacturerPartNumber", required=false)String manufacturerPartNumber,
			@RequestParam(value="manufacturer", required=false)String manufacturer,
			@RequestParam(value="startDate", required=false)String startDate,
			@RequestParam(value="endDate", required=false)String endDate,
			@RequestParam(value="status", required=false)Integer status,
			@RequestParam(value="auditStatus", required=false)Integer auditStatus,
			@RequestParam(value="cate1Name", required=false)Integer cate1Name,
			@RequestParam(value="cate2Name", required=false)Integer cate2Name,
			@RequestParam(value="cate3Name", required=false)Integer cate3Name,
			@RequestParam(value="auditUserName", required=false)String auditUserName,
			@RequestParam(value="page", required=true, defaultValue="1") int page,
			@RequestParam(value="pageSize",required=true, defaultValue="20") int pageSize) {
		try {
			if(StringUtils.isNotBlank(auditUserName)){
				auditUserName = URLDecoder.decode(auditUserName,"UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("解码异常：",e);
		}
		return productStandAuditManager.findProductStandAuditByPage(new ProductStandRequest(manufacturerPartNumber,null, manufacturer, startDate, endDate, status, auditStatus, cate1Name, cate2Name, cate3Name, auditUserName,null), page, pageSize);
	}
	
	/**
	 * 根据ID查询物料信息
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ProductStandAudit getProductStandById(@PathVariable(value = "id") String id)throws BusinessException {
		return productStandAuditManager.getProductStandById(id);
	}

	/**
	 * 更新物料的数据
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ProductStandAudit updateProductStandAudit(@PathVariable(value = "id") String id,@RequestBody ProductStandAudit productStandAudit)throws BusinessException {
		String userId = RequestHelper.getLoginUserId();
		String userName = RequestHelper.getLoginUser().getUsername();
		return productStandAuditManager.updateProductStandAudit(id,userName,userId,productStandAudit);
	}

	/**
	 * 根据Ids批量审核
	 */
	@Override
	@RequestMapping(value = "/audit", method = RequestMethod.PUT)
	public void audit(@RequestBody ProductStandAuditRequest auditRequest) throws BusinessException {
		String userId = RequestHelper.getLoginUserId();
		String userName = RequestHelper.getLoginUser().getUsername();
		productStandAuditManager.audit(auditRequest,userId,userName);
	}

}
