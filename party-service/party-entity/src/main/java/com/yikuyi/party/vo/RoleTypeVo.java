package com.yikuyi.party.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("RoleTypeVo")
public class RoleTypeVo {
	
	@ApiModelProperty(value = "角色类型Id")
	private String roleTypeId;
	
	@ApiModelProperty(value = "操作角色类型")
	private String parentTypeId;
	
	@ApiModelProperty(value = "描述")
	private String description;

	public String getRoleTypeId() {
		return roleTypeId;
	}

	public void setRoleTypeId(String roleTypeId) {
		this.roleTypeId = roleTypeId;
	}

	public String getParentTypeId() {
		return parentTypeId;
	}

	public void setParentTypeId(String parentTypeId) {
		this.parentTypeId = parentTypeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
