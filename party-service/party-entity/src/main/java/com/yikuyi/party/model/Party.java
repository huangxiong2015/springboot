package com.yikuyi.party.model;

import java.util.List;

import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.person.model.Person;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author zr.aoxianbing@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("Party")
public class Party extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2618520416632935089L;
	@ApiModelProperty(value = "Spirit ID ,用于描述人、组织、团体的ID。")
	private String id;

	@ApiModelProperty(value = "用户 类型ID")
	private PartyType partyType;

	public enum PartyType {
		CORPORATION, // 公司
		PERSON, // 个人
		PARTY_GROUP, // 群组
		VIP_CORPORATION, // 认证企业
		DEPARTMENT, // 部门
		CARRIER, // 物流公司
		SUPPLIER // 供应商

	}
	
	@ApiModelProperty(value = "供应商编码")
	private String newPartyCode;

	@ApiModelProperty(value = "是否为系统保留数据，Y: 保留")
	private String isSystem;
	@ApiModelProperty(value = "业务编码")
	private String partyCode;
	@ApiModelProperty(value = "扩展ID")
	private String externalId;
	@ApiModelProperty(value = "描述")
	private String description;
	@ApiModelProperty(value = "状态")
	private PartyStatus partyStatus;
	@ApiModelProperty(value = "属性")
	private PartyAttributes partyAttributes;
	@ApiModelProperty(value = "人员")
	private Person person;
	@ApiModelProperty(value = "组织")
	private PartyGroup partyGroup;
	@ApiModelProperty(value = "角色")
	private List<String> roleTypeList;
	@ApiModelProperty(value = "用户与地址关系")
	private List<PartyContactMech> partyContactMechs;

	@ApiModelProperty(value = "企业ID，如果是个人用户这个字段为空")
	private String corporationId;

	@ApiModelProperty(value = "attrId属性自动增长")
	private String attrId;

	public enum PartyStatus {
		/**
		 * 未申请
		 */
		PARTY_NOT_VERIFIED,
		/**
		 * 启用
		 */
		PARTY_ENABLED,
		/**
		 * 停用
		 */
		PARTY_DISABLED
	}
	
	
	@ApiModelProperty(value = "是否核心Y/N")
	private String isCore;
	

	@ApiModelProperty(value = "供应商简称")
	private String groupName;
	
	@ApiModelProperty(value = "供应商全称")
	private String groupNameFull;
	
	
	
	public String getNewPartyCode() {
		return newPartyCode;
	}

	public void setNewPartyCode(String newPartyCode) {
		this.newPartyCode = newPartyCode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupNameFull() {
		return groupNameFull;
	}

	public void setGroupNameFull(String groupNameFull) {
		this.groupNameFull = groupNameFull;
	}

	public String getIsCore() {
		return isCore;
	}

	public void setIsCore(String isCore) {
		this.isCore = isCore;
	}

	public String getAttrId() {
		return attrId;
	}

	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}

	public String getCorporationId() {
		return corporationId;
	}

	public void setCorporationId(String corporationId) {
		this.corporationId = corporationId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPartyCode() {
		return partyCode;
	}

	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public PartyGroup getPartyGroup() {
		return partyGroup;
	}

	public void setPartyGroup(PartyGroup partyGroup) {
		this.partyGroup = partyGroup;
	}

	public PartyType getPartyType() {
		return partyType;
	}

	public void setPartyType(PartyType partyType) {
		this.partyType = partyType;
	}

	public PartyStatus getPartyStatus() {
		return partyStatus;
	}

	public void setPartyStatus(PartyStatus partyStatus) {
		this.partyStatus = partyStatus;
	}

	public PartyAttributes getPartyAttributes() {
		return partyAttributes;
	}

	public void setPartyAttributes(PartyAttributes partyAttributes) {
		this.partyAttributes = partyAttributes;
	}

	public List<String> getRoleTypeList() {
		return roleTypeList;
	}

	public void setRoleTypeList(List<String> roleTypeList) {
		this.roleTypeList = roleTypeList;
	}

	public String getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(String isSystem) {
		this.isSystem = isSystem;
	}

	public List<PartyContactMech> getPartyContactMechs() {
		return partyContactMechs;
	}

	public void setPartyContactMechs(List<PartyContactMech> partyContactMechs) {
		this.partyContactMechs = partyContactMechs;
	}

}