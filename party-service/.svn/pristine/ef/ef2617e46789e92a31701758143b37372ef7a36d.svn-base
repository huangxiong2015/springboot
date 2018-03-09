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

import java.util.Date;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel("CheckManage")
public class CheckManage extends AbstractEntity {
	
	/**
	 * 审核 记录表
	 */
	private static final long serialVersionUID = 1L;
	
	
	@ApiModelProperty(value = "主键ID")
	private String checkManageId;
	
	@ApiModelProperty(value = "供应商Id")
	private String vendorID;
	
	@ApiModelProperty(value = "供应商简称")
	private String vendorSimName;
	
	@ApiModelProperty(value = "供应商编码")
	private String vendorCode;
	
	@ApiModelProperty(value = "所属地区")
	private String region;
	
	@ApiModelProperty(value = "审核人ID")
	private String checkID;
	
	@ApiModelProperty(value = "审核人名字")
	private String checkName;
	
	@ApiModelProperty(value = "审核日期")
	private Date checkDate;
	
	@ApiModelProperty(value = "申请类型")
	private CheckManageState checkManageState;
	
	@ApiModelProperty(value = "申请备注")
	private String remark;
	
	@ApiModelProperty(value = "审核状态结果")
	private Checkresult checkresult;
	
	@ApiModelProperty(value = "审核结果备注")
	private String checkRemark;
		
	
	
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCheckRemark() {
		return checkRemark;
	}

	public void setCheckRemark(String checkRemark) {
		this.checkRemark = checkRemark;
	}


	public String getVendorSimName() {
		return vendorSimName;
	}

	public void setVendorSimName(String vendorSimName) {
		this.vendorSimName = vendorSimName;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCheckManageId() {
		return checkManageId;
	}

	public void setCheckManageId(String checkManageId) {
		this.checkManageId = checkManageId;
	}

	public String getVendorID() {
		return vendorID;
	}

	public void setVendorID(String vendorID) {
		this.vendorID = vendorID;
	}

	public String getCheckID() {
		return checkID;
	}

	public void setCheckID(String checkID) {
		this.checkID = checkID;
	}

	public String getCheckName() {
		return checkName;
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public CheckManageState getCheckManageState() {
		return checkManageState;
	}

	public void setCheckManageState(CheckManageState checkManageState) {
		this.checkManageState = checkManageState;
	}

	public Checkresult getCheckresult() {
		return checkresult;
	}

	public void setCheckresult(Checkresult checkresult) {
		this.checkresult = checkresult;
	}

	public enum CheckManageState {
		/**
		 * 启动
		 */
		START,
		/**
		 * 失效
		 */
		LOSE,
		/**
		 * 建档
		 */
		NEW,
		/**
		 * 变更
		 */
		EDIT;
		public static CheckManageState getCheckManageState(String checkManageState){
			for(CheckManageState value : CheckManageState.values()){
				if(value.name().equals(checkManageState)){
					return value;
				}
			}
			return null;
		}
	}
	
	public enum Checkresult {
		/**
		 * 待审核
		 */
		NOT_CHECK,
		/**
		 * 已审核   通过
		 */
		CHECK_PASS,
		/**
		 * 已审核  不 通过
		 */
		CHECK_NOT_PASS;
		public static Checkresult getCheckresult(String checkresult){
			for(Checkresult value : Checkresult.values()){
				if(value.name().equals(checkresult)){
					return value;
				}
			}
			return null;
		}
	}
}
