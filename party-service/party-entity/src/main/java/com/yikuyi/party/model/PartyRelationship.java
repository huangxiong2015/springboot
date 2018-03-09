package com.yikuyi.party.model;

import java.util.Date;

import com.yikuyi.party.role.model.RoleTypeEnum;
import com.ykyframework.exception.SystemException;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 会员关系表
 * 
 * @author 张伟
 *
 */
@ApiModel("PartyRelationship")
public class PartyRelationship extends AbstractEntity {

	private PartyRelationship() {
	}

	/**
	 * 默认构建一个有效状态的PartyRelationship对象
	 * @param type
	 * @return
	 * @since 2017年7月18日
	 * @author jik.shu@yikuyi.com
	 */
	public static PartyRelationship build(PartyRelationshipTypeEnum type) {
		switch (type) {
		case AGENT:
			return build(RoleTypeEnum.OPERATION_REP, RoleTypeEnum.SUPPLIER ,type);
		case COS_PLAY:
			return build(RoleTypeEnum.MAIN_ROLE, RoleTypeEnum.CORPORATION ,type);
		case DEVELOPMENT_BY:
			return build(RoleTypeEnum.OPERATION_REP, RoleTypeEnum.SUPPLIER ,type);
		case EMPLOYMENT:
			return build(RoleTypeEnum.EMPLOYEE, RoleTypeEnum.CORPORATION ,type);
		case SUPPLIER_REL:
			return build(RoleTypeEnum.SUPPLIER, RoleTypeEnum.OPERATION_REP ,type);
		case ENTERPRISE_CERTIFIED:
			return build(RoleTypeEnum.CORPORATION, RoleTypeEnum.VIP_CORPORATION ,type);
		case DEPT_CORPORATION_REL:
			return build(RoleTypeEnum.ORGANIZATION_ROLE, RoleTypeEnum.CORPORATION ,type);
		case EXECUTIVE_DEPT_REL:
			return build(RoleTypeEnum.EXECUTIVE_DIRECTOR, RoleTypeEnum.ORGANIZATION_ROLE ,type);
		case DEPT_DEPT_REL:
			return build(RoleTypeEnum.ORGANIZATION_ROLE, RoleTypeEnum.ORGANIZATION_ROLE ,type);
		case USER_DEPT_REL:
			return build(RoleTypeEnum.ENTERPRISE_CUST, RoleTypeEnum.ORGANIZATION_ROLE ,type);
		case ROLE_DEPT_REL:
			return build(RoleTypeEnum.ROLE, RoleTypeEnum.ORGANIZATION_ROLE ,type);
		case REPORTS_TO:
			return build(RoleTypeEnum.ENTERPRISE_CUST, RoleTypeEnum.MAIN_ROLE ,type);
		case AFFILIATED_REL:
			return build(RoleTypeEnum.SUPPLIER, RoleTypeEnum.SUPPLIER ,type);
		case VENDOR_DEPT_REL:
			return build(RoleTypeEnum.SUPPLIER, RoleTypeEnum.DEPT ,type);
		case VENDOR_PRINCIPAL_REL:
			return build(RoleTypeEnum.SUPPLIER, RoleTypeEnum.PRINCIPAL ,type);
		case VENDOR_ENQUIRY_REL:
			return build(RoleTypeEnum.SUPPLIER, RoleTypeEnum.INQUIRY_SPECIALIST ,type);
		case USER_PRODUCTLINE_REL:
			return build(RoleTypeEnum.INDIVIDUAL_CUST, RoleTypeEnum.PRODUCTLINE ,type);
		case VENDOR_CHECK_REL:
			return build(RoleTypeEnum.SUPPLIER, RoleTypeEnum.CHECK ,type);
		case VENDOR_OFFER_REL:
			return build(RoleTypeEnum.SUPPLIER, RoleTypeEnum.QUOTATION_SPECIALIST ,type);
		default:
			throw new SystemException("Enum does not exist.");
		}
	}
	
	private static PartyRelationship build(RoleTypeEnum roleFrom, RoleTypeEnum roleTo, PartyRelationshipTypeEnum partyRelationshipTypeEnum) {
		PartyRelationship partyRelationship = new PartyRelationship();
		partyRelationship.setRoleTypeIdFrom(roleFrom.toString());
		partyRelationship.setRoleTypeIdTo(roleTo.toString());
		partyRelationship.setRelationshipName(partyRelationshipTypeEnum.value);
		partyRelationship.setPartyRelationshipTypeId(partyRelationshipTypeEnum);
		partyRelationship.setStatusId(PartyRelationshipStatus.ENABLE);
		return partyRelationship;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 500152128340994090L;
	@ApiModelProperty(value = "from 会员ID")
	private String partyIdFrom;
	@ApiModelProperty(value = "to 会员ID")
	private String partyIdTo;
	@ApiModelProperty(value = "rom 会员的角色")
	private String roleTypeIdFrom;
	@ApiModelProperty(value = "to 会员的角色")
	private String roleTypeIdTo;
	@ApiModelProperty(value = "有效日期")
	private Date fromDate;
	@ApiModelProperty(value = "失效日期")
	private Date thruDate;
	@ApiModelProperty(value = "状态")
	private PartyRelationshipStatus statusId;
	@ApiModelProperty(value = "关系名称")
	private String relationshipName;
	@ApiModelProperty(value = "安全组名称")
	private String securityGroupId;
	@ApiModelProperty(value = "优先级类型")
	private String priorityTypeId;
	@ApiModelProperty(value = "关系类型")
	private PartyRelationshipTypeEnum partyRelationshipTypeId;
	@ApiModelProperty(value = "权限枚举")
	private String permissionsEnumId;
	@ApiModelProperty(value = "职位名称")
	private String positionTitle;
	@ApiModelProperty(value = "备注")
	private String comments;

	/**
	 * PartyRelationship表的所有关系类型定义枚举
	 * 
	 * @author 张伟
	 */
	public enum PartyRelationshipTypeEnum {
		/**
		 * 运营人员和供应商的代理关系
		 * <br>RoleTypeEnum.OPERATION_REP, RoleTypeEnum.SUPPLIER
		 */
		AGENT("代理"),
		/**
		 * 主账号用户和企业的关系
		 * <br>oleTypeEnum.MAIN_ROLE, RoleTypeEnum.CORPORATION
		 */
		COS_PLAY("职位代表"),
		/**
		 * 运营人员和供应商的开发关系
		 * <br>RoleTypeEnum.OPERATION_REP, RoleTypeEnum.SUPPLIER
		 */
		DEVELOPMENT_BY("开发"),
		/**
		 * 用户和企业的雇佣关系
		 * <br>RoleTypeEnum.EMPLOYEE, RoleTypeEnum.CORPORATION
		 */
		EMPLOYMENT("雇佣"),
		/**
		 * 供应商和创建者的授权关系
		 * <br>RoleTypeEnum.SUPPLIER, RoleTypeEnum.OPERATION_REP
		 */
		SUPPLIER_REL("授权"),
		/**
		 * 普通企业和认证企业的认证关系
		 * <br>RoleTypeEnum.CORPORATION, RoleTypeEnum.VIP_CORPORATION
		 */
		ENTERPRISE_CERTIFIED("认证"),
		/**
		 * 部门与公司的组织关系
		 * <br>RoleTypeEnum.ORGANIZATION_ROLE, RoleTypeEnum.CORPORATION
		 */
		DEPT_CORPORATION_REL("组织"),
		/**
		 * 部门主管和部门的管理关系
		 * <br>RoleTypeEnum.EXECUTIVE_DIRECTOR, RoleTypeEnum.ORGANIZATION_ROLE
		 */
		EXECUTIVE_DEPT_REL("管理"),
		/**
		 * 部门与部门的组织关系
		 * <br>RoleTypeEnum.ORGANIZATION_ROLE, RoleTypeEnum.ORGANIZATION_ROLE
		 */
		DEPT_DEPT_REL("从属"),
		/**
		 * 人与部门的雇佣关系
		 * <br>RoleTypeEnum.ENTERPRISE_CUST, RoleTypeEnum.ORGANIZATION_ROLE
		 */
		USER_DEPT_REL("隶属"),
		/**
		 * 角色和部门的关系
		 * <br>RoleTypeEnum.ROLE, RoleTypeEnum.ORGANIZATION_ROLE
		 */
		ROLE_DEPT_REL("角色代表"),
		/**
		 * 子账号和主账号的关系
		 * <br>RoleTypeEnum.ENTERPRISE_CUST, RoleTypeEnum.MAIN_ROLE
		 */
		REPORTS_TO("汇报"),
		/**
		 * 子供应商和主供应商的附属关系
		 * <br>RoleTypeEnum.SUPPLIER, RoleTypeEnum.SUPPLIER
		 */
		AFFILIATED_REL("附属"),
		
		/**
		 * 供应商和分管部门的附属关系
		 * <br>RoleTypeEnum.SUPPLIER, RoleTypeEnum.SUPPLIER
		 */
		VENDOR_DEPT_REL("供应商和分管部门"),
		/**
		 * 供应商和负责人的附属关系
		 * <br>RoleTypeEnum.SUPPLIER, RoleTypeEnum.SUPPLIER
		 */
		VENDOR_PRINCIPAL_REL("供应商和负责人"),
		/**
		 * 供应商和询价员的附属关系
		 * <br>RoleTypeEnum.SUPPLIER, RoleTypeEnum.SUPPLIER
		 */
		VENDOR_ENQUIRY_REL("供应商和询价员"),
		/**
		 * 供应商和报价员的附属关系
		 * <br>RoleTypeEnum.SUPPLIER, RoleTypeEnum.SUPPLIER
		 */
		VENDOR_OFFER_REL("供应商和报价员"),
		/**
		 * 供应商联系人和产品线Id的附属关系
		 * <br>RoleTypeEnum.SUPPLIER, RoleTypeEnum.SUPPLIER
		 */
		USER_PRODUCTLINE_REL("供应商联系人和产品线Id"),
		/**
		 * 供应商ID和审核Id附属关系
		 * <br>RoleTypeEnum.SUPPLIER, RoleTypeEnum.SUPPLIER
		 */
		VENDOR_CHECK_REL("供应商和审核Id");
		
		private String value = "";

		private PartyRelationshipTypeEnum(String val) {
			this.value = val;
		}

		public String getValue() {
			return value;
		}
	}
	
	public enum PartyRelationshipStatus{
		ENABLE,//有效
		DISABLED//无效
	}

	public String getPartyIdFrom() {
		return partyIdFrom;
	}

	public void setPartyIdFrom(String partyIdFrom) {
		this.partyIdFrom = partyIdFrom == null ? null : partyIdFrom.trim();
	}

	public String getPartyIdTo() {
		return partyIdTo;
	}

	public PartyRelationship setPartyIdTo(String partyIdTo) {
		this.partyIdTo = partyIdTo == null ? null : partyIdTo.trim();
		return this;
	}

	public String getRoleTypeIdFrom() {
		return roleTypeIdFrom;
	}

	public void setRoleTypeIdFrom(String roleTypeIdFrom) {
		this.roleTypeIdFrom = roleTypeIdFrom;
	}

	public String getRoleTypeIdTo() {
		return roleTypeIdTo;
	}

	public void setRoleTypeIdTo(String roleTypeIdTo) {
		this.roleTypeIdTo = roleTypeIdTo;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getThruDate() {
		return thruDate;
	}

	public void setThruDate(Date thruDate) {
		this.thruDate = thruDate;
	}

	public PartyRelationshipStatus getStatusId() {
		return statusId;
	}

	public void setStatusId(PartyRelationshipStatus statusId) {
		this.statusId = statusId;
	}

	public String getRelationshipName() {
		return relationshipName;
	}

	public void setRelationshipName(String relationshipName) {
		this.relationshipName = relationshipName == null ? null : relationshipName.trim();
	}

	public String getSecurityGroupId() {
		return securityGroupId;
	}

	public void setSecurityGroupId(String securityGroupId) {
		this.securityGroupId = securityGroupId == null ? null : securityGroupId.trim();
	}

	public String getPriorityTypeId() {
		return priorityTypeId;
	}

	public void setPriorityTypeId(String priorityTypeId) {
		this.priorityTypeId = priorityTypeId == null ? null : priorityTypeId.trim();
	}

	public PartyRelationshipTypeEnum getPartyRelationshipTypeId() {
		return partyRelationshipTypeId;
	}

	public void setPartyRelationshipTypeId(PartyRelationshipTypeEnum partyRelationshipTypeId) {
		this.partyRelationshipTypeId = partyRelationshipTypeId;
	}

	public String getPermissionsEnumId() {
		return permissionsEnumId;
	}

	public void setPermissionsEnumId(String permissionsEnumId) {
		this.permissionsEnumId = permissionsEnumId == null ? null : permissionsEnumId.trim();
	}

	public String getPositionTitle() {
		return positionTitle;
	}

	public void setPositionTitle(String positionTitle) {
		this.positionTitle = positionTitle == null ? null : positionTitle.trim();
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments == null ? null : comments.trim();
	}

}