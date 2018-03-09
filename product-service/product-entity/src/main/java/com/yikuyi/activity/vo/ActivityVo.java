package com.yikuyi.activity.vo;

import com.yikuyi.activity.model.Activity;
import com.yikuyi.promotion.vo.PromotionFlagVo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 活动vo
 * 
 * @author zr.zhanghua@yikuyi.com
 * @version 1.0.0
 */
public class ActivityVo extends Activity {

	private static final long serialVersionUID = 2244607098524882949L;

	/**
	 * 所有商品的Map结构Key
	 */
	public static final String ACTIVITY_PRODUCT_CACHE = "activityProductCache";
	/**
	 * 所有商品的Map结构Key
	 */
	public static final String ACTIVITY_PRODUCT_QTY_CACHE = "activityProductQtyCache:";
	/**
	 * 所有活动供应商策略Key
	 */
	public static final String ACTIVITY_SUPPLIER_CACHE = "activitySupplierCache";

	private String productCount;

	@ApiModelProperty(value = "活动管理列表活动时间 ")
	private String activityTime;

	@ApiModelProperty(value = "活动进行状态  NS:未开始 ,GOING:进行中 ,OVER:已结束,STOP: 停止")
	private String prStatus;

	@ApiModelProperty(value = "页面来源")
	private String from;

	@ApiModelProperty(value = "活动开始时间(18:00)")
	private String startTime;

	@ApiModelProperty(value = "活动结束时间(19:00)")
	private String endTime;
	
	@ApiModelProperty("模块开始时间")
	private String moduelStartTime;
	
	@ApiModelProperty("模块结束时间")
	private String moduelEndTime;

	// 活动对应的供应商规则缓存KEY
	private String supplierRuleKey;

	// 新版本图片标识结构
	private PromotionFlagVo promotionFlag;
	
	//模块ID
	private String moduleId;
	
	public String getModuelStartTime() {
		return moduelStartTime;
	}

	public void setModuelStartTime(String moduelStartTime) {
		this.moduelStartTime = moduelStartTime;
	}

	public String getModuelEndTime() {
		return moduelEndTime;
	}

	public void setModuelEndTime(String moduelEndTime) {
		this.moduelEndTime = moduelEndTime;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public PromotionFlagVo getPromotionFlag() {
		return promotionFlag;
	}

	public void setPromotionFlag(PromotionFlagVo promotionFlag) {
		this.promotionFlag = promotionFlag;
	}

	public String getSupplierRuleKey() {
		return supplierRuleKey;
	}

	public void setSupplierRuleKey(String supplierRuleKey) {
		this.supplierRuleKey = supplierRuleKey;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getProductCount() {
		return productCount;
	}

	public void setProductCount(String productCount) {
		this.productCount = productCount;
	}

	public String getActivityTime() {
		return activityTime;
	}

	public void setActivityTime(String activityTime) {
		this.activityTime = activityTime;
	}

	public String getPrStatus() {
		return prStatus;
	}

	public void setPrStatus(String prStatus) {
		this.prStatus = prStatus;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
}