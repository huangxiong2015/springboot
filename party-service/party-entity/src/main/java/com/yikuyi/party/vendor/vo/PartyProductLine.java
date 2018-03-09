/*
 * Created: 2017年3月23日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.vendor.vo;

import com.yikuyi.party.vendor.vo.PartyProductLineVo.Type;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 供应商产品
 * @author zr.chenxuemin@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("PartyProductLine")
public class PartyProductLine extends AbstractEntity {

	/**
	 * 供应商 产品线
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "产品线主键ID")
	private String partyProductLineId;

	@ApiModelProperty(value = "供应商外键ID")
	private String partyId;

	@ApiModelProperty(value = "品牌ID")
	private String brandId;

	@ApiModelProperty(value = "品牌名称")
	private String brandName;
	
	@ApiModelProperty(value = "判断页面是否显示红色(Y显示，N不显示)，红色代表没有匹配到")
	private String brandSign;

	@ApiModelProperty(value = "大类ID")
	private String category1Id;

	@ApiModelProperty(value = "大类名称")
	private String category1Name;
	
	@ApiModelProperty(value = "判断页面是否显示红色(Y显示，N不显示)，红色代表没有匹配到")
	private String category1Sign;

	@ApiModelProperty(value = "小类ID")
	private String category2Id;

	@ApiModelProperty(value = "小类名称")
	private String category2Name;
	
	@ApiModelProperty(value = "判断页面是否显示红色(Y显示，N不显示)，红色代表没有匹配到")
	private String category2Sign;

	@ApiModelProperty(value = "次小类ID")
	private String category3Id;

	@ApiModelProperty(value = "次小类名称")
	private String category3Name;
	
	@ApiModelProperty(value = "判断页面是否显示红色(Y显示，N不显示)，红色代表没有匹配到")
	private String category3Sign;

	@ApiModelProperty(value = "状态(ENABLE/UNABLE/DELETE)")
	private Status status;
	
	@ApiModelProperty(value = "审核时使用，新增或删除的：NEW/DEL")
	private Select select;
	
	@ApiModelProperty(value = "代理线类型(PROXY/NOT_PROXY)")
	private Type type;
	
	public String getPartyProductLineId() {
		return partyProductLineId;
	}

	public void setPartyProductLineId(String partyProductLineId) {
		this.partyProductLineId = partyProductLineId;
	}

	public String getPartyId() {
		return partyId;
	}

	public PartyProductLine setPartyId(String partyId) {
		this.partyId = partyId;
		return this;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCategory1Id() {
		return category1Id;
	}

	public void setCategory1Id(String category1Id) {
		this.category1Id = category1Id;
	}

	public String getCategory1Name() {
		return category1Name;
	}

	public void setCategory1Name(String category1Name) {
		this.category1Name = category1Name;
	}

	public String getCategory2Id() {
		return category2Id;
	}

	public void setCategory2Id(String category2Id) {
		this.category2Id = category2Id;
	}

	public String getCategory2Name() {
		return category2Name;
	}

	public void setCategory2Name(String category2Name) {
		this.category2Name = category2Name;
	}
	
	public String getCategory1Sign() {
		return category1Sign==null?"N":category1Sign;
	}

	public void setCategory1Sign(String category1Sign) {
		this.category1Sign = category1Sign;
	}
	
	

	public String getBrandSign() {
		return brandSign==null?"N":brandSign;
	}

	public void setBrandSign(String brandSign) {
		this.brandSign = brandSign;
	}

	public String getCategory2Sign() {
		return category2Sign==null?"N":category2Sign;
	}

	public void setCategory2Sign(String category2Sign) {
		this.category2Sign = category2Sign;
	}

	public String getCategory3Sign() {
		return category3Sign==null?"N":category3Sign;
	}

	public void setCategory3Sign(String category3Sign) {
		this.category3Sign = category3Sign;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brandName == null) ? 0 : brandName.hashCode());
		result = prime * result + ((category1Name == null) ? 0 : category1Name.hashCode());
		result = prime * result + ((category2Name == null) ? 0 : category2Name.hashCode());
		result = prime * result + ((category3Name == null) ? 0 : category3Name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartyProductLine other = (PartyProductLine) obj;
		if (brandName == null) {
			if (other.brandName != null)
				return false;
		} else if (!brandName.equals(other.brandName))
			return false;
		if (category1Name == null) {
			if (other.category1Name != null)
				return false;
		} else if (!category1Name.equals(other.category1Name))
			return false;
		if (category2Name == null) {
			if (other.category2Name != null)
				return false;
		} else if (!category2Name.equals(other.category2Name))
			return false;
		if (category3Name == null) {
			if (other.category3Name != null)
				return false;
		} else if (!category3Name.equals(other.category3Name))
			return false;
		else if (!type.equals(other.type))
			return false;
		return true;
	}

	public String getCategory3Id() {
		return category3Id;
	}

	public void setCategory3Id(String category3Id) {
		this.category3Id = category3Id;
	}

	public String getCategory3Name() {
		return category3Name;
	}

	public void setCategory3Name(String category3Name) {
		this.category3Name = category3Name;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public enum Status {
		/**
		 * 有效
		 */
		ENABLE,
		/**
		 * 无效
		 */
		UNABLE,
		/**
		 * 删除
		 */
		DELETE;
		public static Status getStatus(String status) {
			for (Status value : Status.values()) {
				if (value.name().equals(status)) {
					return value;
				}
			}
			return null;
		}
	}

	public enum Select {
		/**
		 * 新增
		 */
		NEW,
		/**
		 * 删除
		 */
		DEL;
		public static Select getSelect(String select) {
			for (Select value : Select.values()) {
				if (value.name().equals(select)) {
					return value;
				}
			}
			return null;
		}
	}

	public Select getSelect() {
		return select;
	}

	public void setSelect(Select select) {
		this.select = select;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
}