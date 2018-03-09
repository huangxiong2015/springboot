package com.yikuyi.product.template.bll;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yikuyi.product.template.dao.ProductTemplateRepository;
import com.yikuyi.template.model.ProductTemplate;

@Service
public class ProductTemplateManager {
	
	private static final String DEFAULT_TEMPLATE_ID = "defaultId";
	
	@Autowired
	private ProductTemplateRepository productTemplateRepository;
	
	/**
	 * 根据供应商ID获取模版(如果没有配置,返回默认标准配置)
	 * @param vendorId
	 * @return
	 * @since 2016年12月12日
	 * @author zr.shuzuo@yikuyi.com
	 */
	public ProductTemplate geTemplate(String vendorId){
		ProductTemplate rsTemplate = productTemplateRepository.findOne(vendorId);
		if(rsTemplate==null){
			rsTemplate = productTemplateRepository.findOne(DEFAULT_TEMPLATE_ID);
		}
		return rsTemplate;
	}
	
	/**
	 * 根据供应商ID或供应商ID组合仓库ID获取模板(如果没有配置,返回默认标准配置) 
	 * @param vendorId 供应商ID
	 * @param sourceId 仓库ID
	 * @return
	 * @since 2017年8月10日
	 * @author zr.wanghong
	 */
	public ProductTemplate geTemplate(String vendorId,String sourceId){
		ProductTemplate rsTemplate = null;
		if(StringUtils.isNotEmpty(vendorId) && StringUtils.isNotEmpty(sourceId)){
			rsTemplate = productTemplateRepository.findOne(new StringBuffer(vendorId+"_").append(sourceId).toString());
		}
		if(rsTemplate==null){
			rsTemplate = productTemplateRepository.findOne(vendorId);
		}
		if(rsTemplate==null){
			rsTemplate = productTemplateRepository.findOne(DEFAULT_TEMPLATE_ID);
		}
		return rsTemplate;
	}
}