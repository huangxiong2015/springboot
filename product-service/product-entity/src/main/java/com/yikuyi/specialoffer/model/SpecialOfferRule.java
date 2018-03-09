package com.yikuyi.specialoffer.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
/**
 * 供应商信息
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@Document(collection = "special_offer_rule")
public class SpecialOfferRule implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4465966457101465366L;

	@Id
	@ApiModelProperty(value = "规则id")
	@JsonProperty("id")
	private String id;
	
	@ApiModelProperty(value = "供应商Id")
	private String vendorId;
	
	@ApiModelProperty(value = "型号：MPN,规则（即产品线）：RULE")
	private RuleType ruleType;
	
	@ApiModelProperty(value = "上传：UPLOAD,手动输入：INPUT")
	private MpnType mpnType;
	
	@ApiModelProperty(value = "制造商,结构数据[]")
	private List<Integer> mfrIds;
	
	@ApiModelProperty(value = "仓库,结构数据[]")
	private List<String> sourceIds;
	
	@ApiModelProperty(value = "次小类,结构数据[]")
	private List<String> catIds;
	
	@ApiModelProperty(value = "型号,结构数据[],冗余关系表前3个")
	private String mpn;
	
	@ApiModelProperty(value = "规则备注")
	private String desc;
	
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
	
	public enum RuleType{
		/**
		 * 型号
		 */
		MPN,
		/**
		 * 规则(即产品线)
		 */
		RULE
	}
	
	public enum MpnType{
		/**
		 * 上传
		 */
		UPLOAD,
		/**
		 * 手动输入
		 */
		INPUT
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public RuleType getRuleType() {
		return ruleType;
	}

	public void setRuleType(RuleType ruleType) {
		this.ruleType = ruleType;
	}

	public MpnType getMpnType() {
		return mpnType;
	}

	public void setMpnType(MpnType mpnType) {
		this.mpnType = mpnType;
	}
	
	public List<Integer> getMfrIds() {
		return mfrIds;
	}

	public void setMfrIds(List<Integer> mfrIds) {
		this.mfrIds = mfrIds;
	}

	public List<String> getSourceIds() {
		return sourceIds;
	}

	public void setSourceIds(List<String> sourceIds) {
		this.sourceIds = sourceIds;
	}

	public List<String> getCatIds() {
		return catIds;
	}

	public void setCatIds(List<String> catIds) {
		this.catIds = catIds;
	}

	public String getMpn() {
		return mpn;
	}

	public void setMpn(String mpn) {
		this.mpn = mpn;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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