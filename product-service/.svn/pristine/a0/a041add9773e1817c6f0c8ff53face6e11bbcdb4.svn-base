package com.yikuyi.product.rule.common.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.yikuyi.rule.price.ProductPriceRule;

@Mapper
public interface ProductPriceRuleDao{
	int insert(ProductPriceRule record);
	
	/**
	 * 查询规则列表公共方法
	 * @param pricePurposeType 策略类型（交期或定价）
	 * @param offset 开始位置
	 * @param limit 数据条数
	 * @param startDate 创建时间-开始时间
	 * @param endDate 创建时间-结束时间
	 * @param ruleStatus 规则状态
	 * @param vendorId 供应商ID
	 * @param ruleName 规则名称
	 * @return
	 * @since 2017年7月25日
	 * @author zr.wanghong
	 */
	List<ProductPriceRule> findList(@Param("pricePurposeType") String pricePurposeType,@Param("offset") Integer offset,@Param("limit") Integer limit,
			@Param("startDate") Date startDate,
			@Param("endDate") Date endDate,
			@Param("ruleStatus") String ruleStatus,
			@Param("vendorId") String vendorId,@Param("ruleName") String ruleName);
	
	/**
	 * 列表查询策略
	 * @param paramMap 参数map
	 * @return
	 * @since 2017年8月12日
	 * @author zr.wanghong
	 */
	List<ProductPriceRule> findList(Map<String,Object> paramMap);
	
	int update(ProductPriceRule record);
	
	ProductPriceRule getDetailById(@Param("priceRuleId") String priceRuleId);
	
	Map<String,Object> findSameRule(Map<String,Object> map);
	
	int checkRuleNameIsExist(ProductPriceRule record);
	
	Long findListCount(@Param("pricePurposeType") String pricePurposeType,
			@Param("startDate") Date startDate,
			@Param("endDate") Date endDate,
			@Param("ruleStatus") String ruleStatus,
			@Param("vendorId") String vendorId,@Param("ruleName") String ruleName);
	
	Long findListCount(Map<String,Object> paramMap);
	
	int getVendorRuleInfo(Map<String,Object> map);
}