package com.yikuyi.party.group.model;


import java.util.Date;
import java.util.List;

import com.yikuyi.party.facility.model.Facility;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author zr.aoxianbing@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("PartyGroup")
public class PartyGroup extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2701771302393355916L;
	
	@ApiModelProperty(value = "id")
	private String partyId;
	
	@ApiModelProperty(value = "组织名称")
	private String groupName;
	
	@ApiModelProperty(value = "组织全称")
	private String groupNameFull;
	
	@ApiModelProperty(value = "供应商编码")
	private String groupCodeNum;
	
	@ApiModelProperty(value = "年营收规模")
	private String description;
	
	@ApiModelProperty(value = "备注")
	private String comments;
	
	@ApiModelProperty(value = "logo 图片地址")
	private String logoImageUrl;
	
	@ApiModelProperty(value = "供应商仓库")
	private List<Facility> facilitylist;
	
	@ApiModelProperty(value = "logo 图片地址小图")
	private String logoImageUrlSmall;
	
	@ApiModelProperty(value = "激活状态")
	private ActiveStatus activeStatus;
	@ApiModelProperty(value = "账号状态")
	private AccountStatus accountStatus;
	@ApiModelProperty(value = "申请日期")
	private Date applyDate;
	@ApiModelProperty(value = "审核日期")
	private Date approvedDate;
	
	@ApiModelProperty(value = "账期状态")
	private AccountPeriodStatus accountPeriodStatus;

	@ApiModelProperty(value = "信用备注")
	private String creditComments;
	
	@ApiModelProperty(value = "交期准确率")
	private String leadtimeAccuracyrate;
	
	public String getLeadtimeAccuracyrate() {
		return leadtimeAccuracyrate;
	}
	public void setLeadtimeAccuracyrate(String leadtimeAccuracyrate) {
		this.leadtimeAccuracyrate = leadtimeAccuracyrate;
	}
	public AccountPeriodStatus getAccountPeriodStatus() {
		return accountPeriodStatus;
	}
	public void setAccountPeriodStatus(AccountPeriodStatus accountPeriodStatus) {
		this.accountPeriodStatus = accountPeriodStatus;
	}
	public enum AccountPeriodStatus{
		/**
		 *  未申请
		 */
		PERIOD_NOT_VERIFIED,
	
		/**
		 *  待审核
		 */
		PERIOD_WAIT_APPROVE,
		/**
		 *  驳回
		 */
		PERIOD_REJECTED,
		
		/**
		 *  已认证
		 */
		PERIOD_VERIFIED,

		/**
		 * 冻结
		 */
		PERIOD_DISABLED
		
		
	}
	public enum AccountStatus{
		/**
		 *  待审核
		 */
		ACCOUNT_WAIT_APPROVE,
		/**
		 *  审核通过
		 */
		ACCOUNT_VERIFIED,
		/**
		 *  未审核
		 */
		ACCOUNT_NOT_VERIFIED,
		/**
		 *  驳回
		 */
		ACCOUNT_REJECTED,
		/** 
		 *  有效  
		 */
		VALID,
		/**
		 *  无效
		 */
		NOT_VALID,
		/**
		 *  未生效
		 */
		INEFFECTIVE
	}
	public enum ActiveStatus{
		/**
		 *  未申请
		 */
		PARTY_NOT_VERIFIED,
	
		/**
		 *  待审核
		 */
		WAIT_APPROVE,
		/**
		 *  驳回
		 */
		REJECTED,
		
		/**
		 *  已认证
		 */
		PARTY_VERIFIED,
		/**
		 * 失效
		 */
		INVALID,	
		/**
		 *  启用
		 */
		START		
	}

	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public Date getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
	public ActiveStatus getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(ActiveStatus activeStatus) {
		this.activeStatus = activeStatus;
	}
	public String getLogoImageUrlSmall() {
		return logoImageUrlSmall;
	}
	public void setLogoImageUrlSmall(String logoImageUrlSmall) {
		this.logoImageUrlSmall = logoImageUrlSmall;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getLogoImageUrl() {
		return logoImageUrl;
	}
	public void setLogoImageUrl(String logoImageUrl) {
		this.logoImageUrl = logoImageUrl;
	}
	public List<Facility> getFacilitylist() {
		return facilitylist;
	}
	public void setFacilitylist(List<Facility> facilitylist) {
		this.facilitylist = facilitylist;
	}
	public AccountStatus getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}
	public String getGroupNameFull() {
		return groupNameFull;
	}
	public void setGroupNameFull(String groupNameFull) {
		this.groupNameFull = groupNameFull;
	}
	public String getGroupCodeNum() {
		return groupCodeNum;
	}
	public void setGroupCodeNum(String groupCodeNum) {
		this.groupCodeNum = groupCodeNum;
	}
	public String getCreditComments() {
		return creditComments;
	}
	public void setCreditComments(String creditComments) {
		this.creditComments = creditComments;
	}
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	
	
	
}
