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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
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
import com.yikuyi.brand.model.ProductBrand;
import com.yikuyi.product.brand.manager.BrandManager;
import com.yikuyi.product.goods.IProductStandResource;
import com.yikuyi.product.goods.manager.ProductStandManager;
import com.yikuyi.product.model.ProductStand;
import com.yikuyi.product.model.SpuWhiteList;
import com.yikuyi.product.vo.ProductStandRequest;
import com.yikuyi.product.vo.RawData;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.exception.SystemException;
/**
 * 物料管理入口
 * @author zr.wujiajun@yikuyi.com
 * @version 1.0.0
 */
@RestController
@RequestMapping("v1/products/stand")
public class ProductStandResource implements IProductStandResource {
	private static final Logger logger = LoggerFactory.getLogger(ProductStandResource.class);

	@Autowired
	private ProductStandManager productStandManager;
	
	@Autowired
	private BrandManager brandManager;

	@Override
	@RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public PageInfo<ProductStand> list(@RequestParam(value="manufacturerPartNumber", required=false)String manufacturerPartNumber,
			@RequestParam(value="manufacturerPartNumberExact", required=false)String manufacturerPartNumberExact,
			@RequestParam(value="manufacturer", required=false)String manufacturer,
			@RequestParam(value="startDate", required=false)String startDate,
			@RequestParam(value="endDate", required=false)String endDate,
			@RequestParam(value="status", required=false)Integer status,
			@RequestParam(value="auditStatus", required=false)Integer auditStatus,
			@RequestParam(value="cate1Name", required=false)Integer cate1Name,
			@RequestParam(value="cate2Name", required=false)Integer cate2Name,
			@RequestParam(value="cate3Name", required=false)Integer cate3Name,
			@RequestParam(value="auditUserName", required=false)String auditUserName,
			@RequestParam(value="page", required=false, defaultValue="1") int page,
			@RequestParam(value="pageSize",required=false, defaultValue="20") int pageSize) {
		try {
			if(StringUtils.isNotBlank(auditUserName)){
				auditUserName = URLDecoder.decode(auditUserName,"UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("解码异常：",e);
		}
		
		return productStandManager.list(new ProductStandRequest(manufacturerPartNumber,manufacturerPartNumberExact, manufacturer, startDate, endDate, status, auditStatus, cate1Name, cate2Name, cate3Name, auditUserName,null), page, pageSize);
	}
	
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ProductStand getProductStand(@PathVariable(value = "id") String id) {
		return productStandManager.getProductStand(id);
	}
	
	@Override
	public String createProductStand(@RequestBody List<RawData> rawdatas) {
		return productStandManager.createProductStand(rawdatas);
	}

	@Override
	@RequestMapping(value="/batch" ,method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<ProductStand> batchQuery(@RequestBody List<RawData> datas) {
		//没传数据，返回空
		if(CollectionUtils.isEmpty(datas)){
			return new ArrayList<>();
		}
		//品牌和型号 存在没传的数据
		for(RawData rd : datas){
			if(StringUtils.isBlank(rd.getManufacturer()) || StringUtils.isBlank(rd.getManufacturerPartNumber())){
				return new ArrayList<>();
			}			
		}
		Map<String,ProductBrand> brandAliasMap = null;
		try{
			brandAliasMap = brandManager.getAliasBrandMap();
			if(brandAliasMap == null){
				return new ArrayList<>();
			}
		}catch(Exception e){
			logger.error("Exception",e);
			throw new SystemException("Exception",e);
		}	
		return productStandManager.findProductStandByRawDatas(datas, brandAliasMap,null);
	}

	@Override
	public void updateProductStand(@RequestBody ProductStand productStand) {
		productStandManager.updateProductStand(productStand);
	}

	/**
	 * 导出xls文件
	 */
	@Override
	@RequestMapping(value="/excel/download" ,method = RequestMethod.GET)
	public void exportExcel(@RequestParam(value="manufacturerPartNumber", required=false)String manufacturerPartNumber,
			@RequestParam(value="manufacturer", required=false)String manufacturer,
			@RequestParam(value="startDate", required=false)String startDate,
			@RequestParam(value="endDate", required=false)String endDate,
			@RequestParam(value="status", required=false)Integer status,
			@RequestParam(value="auditStatus", required=false)Integer auditStatus,
			@RequestParam(value="cate1Name", required=false)Integer cate1Name,
			@RequestParam(value="cate2Name", required=false)Integer cate2Name,
			@RequestParam(value="cate3Name", required=false)Integer cate3Name,
			@RequestParam(value="ids", required=false)String ids)throws BusinessException {
		String userName = RequestHelper.getLoginUser().getUsername();
		productStandManager.export(new ProductStandRequest(manufacturerPartNumber,null, manufacturer, startDate, endDate, status, 
				auditStatus, cate1Name, cate2Name, cate3Name, null,ids),userName);
    	
	}

	/**
	 * 批量验证型号是否存在
	 */
	@Override
	@RequestMapping(value="/list/existManufacturerPartNumber" ,method = RequestMethod.POST)
	public List<String> existManufacturerPartNumberList(@RequestBody List<String> manufacturerPartNumberList) {
		return productStandManager.existManufacturerPartNumberList(manufacturerPartNumberList);
	}

	/**
	 * 新增特殊SPU
	 * @param manufacturerPartNumber
	 * @param manufacturerId
	 * @since 2017年12月8日
	 * @author tb.lijing@yikuyi.com
	 * @throws BusinessException 
	 */
	@Override
	@RequestMapping(value="/saveWhiteList" ,method = RequestMethod.POST)
	public void saveWhiteList(@RequestBody SpuWhiteList spuWhiteList) throws BusinessException {
		productStandManager.saveWhiteList(spuWhiteList);
	}

	/**
	 * 查询特殊SPU
	 * @return
	 * @since 2017年12月8日
	 * @author tb.lijing@yikuyi.com
	 */
	@Override
	@RequestMapping(value="/findWhiteListInfo" ,method = RequestMethod.GET)
	public PageInfo<SpuWhiteList> findWhiteListInfo(@RequestParam(value="manufacturerPartNumber", required=false)String manufacturerPartNumber, @RequestParam(value="manufacturer", required=false)String manufacturer, 	@RequestParam(value="page", required=false, defaultValue="1") int page,
			@RequestParam(value="pageSize",required=false, defaultValue="20") int pageSize) {
		return productStandManager.findWhiteListInfo(manufacturerPartNumber, manufacturer, page, pageSize);
	}

	

}