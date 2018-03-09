package com.yikuyi.rule.delivery.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("DeliveryInfo")
public class DeliveryInfo implements Serializable{
	
	private static final long serialVersionUID = 5754405794213764239L;
	
	@ApiModelProperty(value="交期模板Id")
	private String id;

	@ApiModelProperty(value="交期模板名称")
	private String deliveryRuleName;
	
	@ApiModelProperty(value="供应商")
	private String verdonName;
	
	@ApiModelProperty(value="仓库")
	private String warehouse;
	
	@ApiModelProperty(value="商品类型：0-现货，1-排单")
	private Integer productType;
	
	@ApiModelProperty(value="商品类型为现货，国内交期最少天数")
	private Integer leadTimeMinCH;
	
	@ApiModelProperty(value="商品类型为现货，国内交期最多天数")
	private Integer leadTimeMaxCH;
	
	@ApiModelProperty(value="商品类型为现货，香港交期最少天数")
	private Integer leadTimeMinHK;
	
	@ApiModelProperty(value="商品类型为现货，香港交期最多天数")
	private Integer leadTimeMaxHK;
	
	//@ApiModelProperty(value="商品类型为排单，国内交期天数")
	//private Integer leadTimeCH;
	
	//@ApiModelProperty(value="商品类型为排单，香港交期天数")
	//private Integer leadTimeHK;
	
	@ApiModelProperty(value="商品类型为排单，国内交期最小天数")
	private Integer factoryLeadTimeMinCH;
	
	@ApiModelProperty(value="商品类型为排单，国内交期最多天数")
	private Integer factoryLeadTimeMaxCH;
	
	@ApiModelProperty(value="商品类型为排单，香港交期最小天数")
	private Integer factoryLeadTimeMinHK;
	
	@ApiModelProperty(value="商品类型为排单，香港交期最多天数")
	private Integer factoryLeadTimeMaxHK;
	
	@ApiModelProperty(value="交期模板描述")
	private String description;
	
	@ApiModelProperty(value="状态(ENABLED：启用，DISABLED：停用，DELETED：删除)")
	private String status;
	
	@ApiModelProperty(value="最后更新时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastUpdateDate;
	
	@ApiModelProperty(value="创建时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createdDate;
	
	@ApiModelProperty(value="上传交期为空页面不显示(Y/N)")
	private String isShowLeadTime;
	
	public String getIsShowLeadTime() {
		return isShowLeadTime;
	}

	public void setIsShowLeadTime(String isShowLeadTime) {
		this.isShowLeadTime = isShowLeadTime;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeliveryRuleName() {
		return deliveryRuleName;
	}

	public void setDeliveryRuleName(String deliveryRuleName) {
		this.deliveryRuleName = deliveryRuleName;
	}

	public String getVerdonName() {
		return verdonName;
	}

	public void setVerdonName(String verdonName) {
		this.verdonName = verdonName;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public Integer getLeadTimeMinCH() {
		return leadTimeMinCH;
	}

	public void setLeadTimeMinCH(Integer leadTimeMinCH) {
		this.leadTimeMinCH = leadTimeMinCH;
	}

	public Integer getLeadTimeMaxCH() {
		return leadTimeMaxCH;
	}

	public void setLeadTimeMaxCH(Integer leadTimeMaxCH) {
		this.leadTimeMaxCH = leadTimeMaxCH;
	}

	public Integer getLeadTimeMinHK() {
		return leadTimeMinHK;
	}

	public void setLeadTimeMinHK(Integer leadTimeMinHK) {
		this.leadTimeMinHK = leadTimeMinHK;
	}

	public Integer getLeadTimeMaxHK() {
		return leadTimeMaxHK;
	}

	public void setLeadTimeMaxHK(Integer leadTimeMaxHK) {
		this.leadTimeMaxHK = leadTimeMaxHK;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getFactoryLeadTimeMinCH() {
		return factoryLeadTimeMinCH;
	}

	public void setFactoryLeadTimeMinCH(Integer factoryLeadTimeMinCH) {
		this.factoryLeadTimeMinCH = factoryLeadTimeMinCH;
	}

	public Integer getFactoryLeadTimeMaxCH() {
		return factoryLeadTimeMaxCH;
	}

	public void setFactoryLeadTimeMaxCH(Integer factoryLeadTimeMaxCH) {
		this.factoryLeadTimeMaxCH = factoryLeadTimeMaxCH;
	}

	public Integer getFactoryLeadTimeMinHK() {
		return factoryLeadTimeMinHK;
	}

	public void setFactoryLeadTimeMinHK(Integer factoryLeadTimeMinHK) {
		this.factoryLeadTimeMinHK = factoryLeadTimeMinHK;
	}

	public Integer getFactoryLeadTimeMaxHK() {
		return factoryLeadTimeMaxHK;
	}

	public void setFactoryLeadTimeMaxHK(Integer factoryLeadTimeMaxHK) {
		this.factoryLeadTimeMaxHK = factoryLeadTimeMaxHK;
	}
	
}
