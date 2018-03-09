package com.yikuyi.rule.logistics.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("LogisticsInfo")
@JsonInclude(Include.NON_NULL)  //实体中为null的字段不进行序列化
public class LogisticsInfo {
	
	@ApiModelProperty(value="运费模板Id")
	private String id;

	@ApiModelProperty(value="运费模板名称")
	private String logisticsRuleName;
	
	@ApiModelProperty(value="是否包邮")
	private Boolean pinkage;
	
	@ApiModelProperty(value="运费模板描述")
	private String description;
	
	@ApiModelProperty(value="状态(ENABLED：启用，DISABLED：停用，DELETED：删除)")
	private String status;
	
	@ApiModelProperty(value="最后更新时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastUpdateDate;
	
	@ApiModelProperty(value="运费规则")
	private List<LogisticsCondInfo> condInfo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogisticsRuleName() {
		return logisticsRuleName;
	}

	public void setLogisticsRuleName(String logisticsRuleName) {
		this.logisticsRuleName = logisticsRuleName;
	}

	public Boolean getPinkage() {
		return pinkage;
	}

	public void setPinkage(Boolean pinkage) {
		this.pinkage = pinkage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public List<LogisticsCondInfo> getCondInfo() {
		return condInfo;
	}

	public void setCondInfo(List<LogisticsCondInfo> condInfo) {
		this.condInfo = condInfo;
	}

}
