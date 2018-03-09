package com.yikuyi.rule.mov.vo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

@Document(collection = "mov_rule_template")
public class MovRuleTemplate extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2768302140226869549L;

	@ApiModelProperty(value="规则ID")
	@Id
	private String ruleId;
	
	@ApiModelProperty(value="启用设置区分类型")
	private String movType;
	
	@ApiModelProperty(value="前台用的mov类型")
	private String ruleType;
	
	@ApiModelProperty(value="供应商")
	private String vendorId;
	
	@ApiModelProperty(value="国内最小订单金额")
	private String cnyMovAmount;
	
	@ApiModelProperty(value="国外最小订单金额")
	private String usdMovAmount;
	
	@ApiModelProperty(value="备注")
	private String description;
	
	@ApiModelProperty(value="状态")
	private String status;
	
	@ApiModelProperty(value="制造商集合")
	private List<String> manufacturerIds;
	
	@ApiModelProperty(value="仓库ID")
	private List<String> sourceIds;
	

	@ApiModelProperty(value="创建时间戳")
	private String createdTimeMillis;
	
	@ApiModelProperty(value="更新时间戳")
	private String updatedTimeMillis;
	
	@ApiModelProperty(value="制造商名称集合")
	private String manufacturerName;
	
	@ApiModelProperty(value="仓库名称集合")
	private String sourceName;
	
	@ApiModelProperty(value="模板缓存的key(后台用)")
	private String cacheKey;
	
	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	
	public String getCreatedTimeMillis() {
		return createdTimeMillis;
	}

	public void setCreatedTimeMillis(String createdTimeMillis) {
		this.createdTimeMillis = createdTimeMillis;
	}

	public String getUpdatedTimeMillis() {
		return updatedTimeMillis;
	}

	public void setUpdatedTimeMillis(String updatedTimeMillis) {
		this.updatedTimeMillis = updatedTimeMillis;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCnyMovAmount() {
		return cnyMovAmount;
	}

	public void setCnyMovAmount(String cnyMovAmount) {
		this.cnyMovAmount = cnyMovAmount;
	}

	public String getUsdMovAmount() {
		return usdMovAmount;
	}

	public void setUsdMovAmount(String usdMovAmount) {
		this.usdMovAmount = usdMovAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}


	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getMovType() {
		return movType;
	}

	public void setMovType(String movType) {
		this.movType = movType;
	}

	public List<String> getManufacturerIds() {
		return manufacturerIds;
	}

	public void setManufacturerIds(List<String> manufacturerIds) {
		this.manufacturerIds = manufacturerIds;
	}
	public List<String> getSourceIds() {
		return sourceIds;
	}

	public void setSourceIds(List<String> sourceIds) {
		this.sourceIds = sourceIds;
	}

}
