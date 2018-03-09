package com.yikuyi.specialoffer.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
/**
 * 供应商信息
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Document(collection = "special_offer")
public class SpecialOffer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6949591213799214185L;

	@Id
	@ApiModelProperty(value = "供应商id")
	@JsonProperty("id")
	private String id;
	
	@ApiModelProperty(value = "ALL/INOPERATIVE/APPOINT")
	private RuleStatus ruleStatus;
	
	@ApiModelProperty(value = "专属特价")
	private String ruleText;
	
	@ApiModelProperty(value = "创建者")
	private String creator;
	
	@ApiModelProperty(value = "创建者名称")
	private String creatorName;
	
	@ApiModelProperty(value="创建时间戳")
	private String createdTimeMillis;
	
	@ApiModelProperty(value="更新时间戳")
	private String updatedTimeMillis;
	
	@ApiModelProperty(value = "更新者")
	private String lastUpdateUser;
	
	@ApiModelProperty(value = "更新者名称")
	private String lastUpdateUserName;
	
	public enum RuleStatus{
		/**
		 * 所有商品
		 */
		ALL,
		/**
		 * 不生效
		 */
		INOPERATIVE,
		/**
		 * 指定商品
		 */
		APPOINT
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RuleStatus getRuleStatus() {
		return ruleStatus;
	}

	public void setRuleStatus(RuleStatus ruleStatus) {
		this.ruleStatus = ruleStatus;
	}

	public String getRuleText() {
		return ruleText;
	}

	public void setRuleText(String ruleText) {
		this.ruleText = ruleText;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
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

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public String getLastUpdateUserName() {
		return lastUpdateUserName;
	}

	public void setLastUpdateUserName(String lastUpdateUserName) {
		this.lastUpdateUserName = lastUpdateUserName;
	}
	
}