/*
 * Created: 2017年1月19日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.vendors.bll;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yikuyi.party.vendor.dao.PartyProductLineDao;
import com.yikuyi.party.vendor.vo.PartyProductLine;
import com.yikuyi.party.vendor.vo.PartyProductLineModel;
import com.yikuyi.party.vendor.vo.PartyProductLineVo;

@Service
@Transactional
public class PartyProductLineManager {
	private static final Logger logger = LoggerFactory.getLogger(PartyProductLineManager.class);

	@Autowired
	private PartyProductLineDao partyProductLineDao;

	/**
	 * 根据产品线ID 查询产品信息
	 * @param partyProductLineId
	 * @return
	 * @since 2017年8月11日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public PartyProductLine findById(String partyProductLineId) {
		if(StringUtils.isEmpty(partyProductLineId)){
			logger.error("查询产品线失败，查询条件partyProductLineId为null");
			return null;
		}
		return partyProductLineDao.findById(partyProductLineId);
	}
	
	/**
	 * 查询产品线集合
	 * @param partyProductLine
	 * @return
	 * @since 2017年8月11日
	 * @author zr.chenxuemin@yikuyi.com
	 */
	public List<PartyProductLine> findByEntity(PartyProductLine partyProductLine){
		return partyProductLineDao.findByEntity(partyProductLine);
	}
	/**
	 * 查询唯一产品线集合
	 * @param partyProductLine
	 * @return
	 * @since 2017年9月27日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<PartyProductLineVo> onlyProductLine(PartyProductLine partyProductLine){
		return partyProductLineDao.onlyProductLine(partyProductLine);
	}


	/**
	 * 查询【分销商/供应商】产品线类别去掉重复的数据
	 * @param partyProductLine
	 * @return
	 * @since 2017年10月24日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	public List<PartyProductLineModel> onlyProductCategoryList(PartyProductLineModel partyProductLine) {
		return partyProductLineDao.onlyProductCategoryList(partyProductLine);
	}

	
	/**
	 * 根据制造商id,查询所有分销商过滤重复
	 * @param brandId
	 * @return
	 * @since 2017年10月24日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<PartyProductLineVo> listSupplierByBrandId(String brandId){
		return partyProductLineDao.listSupplierByBrandId(brandId);
	}

	
	/**
	 * 根据制造商id,查询所有分销商过滤重复
	 * @param brandId
	 * @return
	 * @since 2017年10月24日
	 * @author zr.helinmei@yikuyi.com
	 */
	public List<PartyProductLineVo> listCategoryByBrandId(String brandId){
		return partyProductLineDao.listCategoryByBrandId(brandId);
	}
	
	/**
	 * mov设置，选择产品线查询品牌
	 * @param partyProductLine
	 * @return
	 * @since 2018年1月12日
	 * @author tb.lijing@yikuyi.com
	 */
/*	public List<?> findProductLineBrand(PartyProductLine partyProductLine){
		//查询代理产品线数据
		partyProductLine.setType(Type.PROXY);
		List<PartyProductLineVo> proxyList = partyProductLineDao.onlyProductLine(partyProductLine);
		//查询不代理产品线数据
		partyProductLine.setType(Type.NOT_PROXY);
		List<PartyProductLineVo> notProxyList = partyProductLineDao.onlyProductLine(partyProductLine);
		if(CollectionUtils.isEmpty(proxyList) && CollectionUtils.isEmpty(notProxyList)){
			List<ProductBrand> listBrands = productClientBuilder.brandResource().findAll();
			return listBrands;
		}
		//当只有代理产品线数据(白名单)没有不代理产品线数据（黑名单）时，直接返回代理产品线数据(白名单)
		if(CollectionUtils.isNotEmpty(proxyList) && CollectionUtils.isEmpty(notProxyList)){
			return proxyList;
		}
		//将不代理产品线数据（黑名单）放入map中
		Map<String,String> map = new HashMap<>();
		if(CollectionUtils.isNotEmpty(notProxyList)){
			for(PartyProductLineVo notProxyVo : notProxyList){
				map.put(notProxyVo.getBrandId(), notProxyVo.getBrandName());
			}
		}
		//当有代理产品线数据(白名单)和不代理产品线数据（黑名单）时，进行处理
		if(CollectionUtils.isNotEmpty(proxyList) && CollectionUtils.isNotEmpty(notProxyList)){
			Iterator<PartyProductLineVo> it = proxyList.iterator();
			while(it.hasNext()){
				PartyProductLineVo proxyVo = it.next();
				if(map.containsKey(proxyVo.getBrandId())){
					it.remove();
				}
			}
			if(CollectionUtils.isNotEmpty(proxyList)){
				return proxyList;
			}else{
				List<ProductBrand> listBrands = productClientBuilder.brandResource().findAll();
				Iterator<ProductBrand> itProductBrands = listBrands.iterator();
				while(itProductBrands.hasNext()){
					ProductBrand productBrand = itProductBrands.next();
					if(map.containsKey(productBrand.getId().toString())){
						itProductBrands.remove();
					}
				}
				return listBrands;
			}
		}
		return null;
	}*/

}