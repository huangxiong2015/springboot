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

import java.util.Map;
import java.util.Set;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel("Vendor")
public class Vendor extends AbstractEntity {
	
	/**
	 * 供应商管理vo
	 */
	private static final long serialVersionUID = 1L;
	
	
	@ApiModelProperty(value = "供应商基础信息")
	private VendorInfoVo vendorInfoVo;
	
	@ApiModelProperty(value = "产品线信息")
	private Set<PartyProductLine> partyProductLineList;
	
	@ApiModelProperty(value = "供应商信用信息")
	private VendorCreditVo vendorCreditVo;
	
	@ApiModelProperty(value = "供应商销售信息")
	private VendorSaleInfoVo vendorSaleInfoVo;
	
	
	/*VENDOR_INFO_LEGALPERSON //公司法人
	VENDOR_INFO_REGPRICE //注册资金
	VENDOR_INFO_REGRADDRESS //注册地址
	VENDOR_INFO_EMPLOYEENUM //员工人数
	VENDOR_INFO_WEBSITE //供应商官网
	VENDOR_CREDIT_PURCHASEDEAL //采购协议
	VENDOR_CREDIT_PURCHASEDEALDATE //采购协议有效期
	VENDOR_CREDIT_SECRECYPROTOCOL //保密协议
	VENDOR_CREDIT_SECRECYPROTOCOLDATE //保密协议有效期
	VENDOR_SALE_INFOVO_FOCUSFIELDS //关注领域
	VENDOR_SALE_INFOVO_PRODUCTCATEGORYS //优势产品类别
	VENDOR_SALE_INFOVO_MAJORCLIENTS //主要客户
*/
	@ApiModelProperty(value = "属性key与value的值")
	private Map<String,String> map;
	
	@ApiModelProperty(value = "描述说明")
	private String describe;
	
	@ApiModelProperty(value = "申请人名字")
	private String applyName;
	
	@ApiModelProperty(value = "申请人邮箱")
	private String applyMail;
	
	@ApiModelProperty(value = "列表供应商编码")
	private String partyCode;
	
	@ApiModelProperty(value = "所属地区")
	private String region;
	
	@ApiModelProperty(value = "供应商ID")
	private String partyId;
	
	@ApiModelProperty(value = "公司名称  （审核专用）")
	private String name;
	
	@ApiModelProperty(value = "申请人名称")
	private String contactUserName;
	
	@ApiModelProperty(value = "审核人名称")
	private String approvePartyName;
	
	
	

	public String getApprovePartyName() {
		return approvePartyName;
	}

	public void setApprovePartyName(String approvePartyName) {
		this.approvePartyName = approvePartyName;
	}

	public String getContactUserName() {
		return contactUserName;
	}

	public void setContactUserName(String contactUserName) {
		this.contactUserName = contactUserName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getPartyCode() {
		return partyCode;
	}

	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public VendorInfoVo getVendorInfoVo() {
		return vendorInfoVo;
	}

	public void setVendorInfoVo(VendorInfoVo vendorInfoVo) {
		this.vendorInfoVo = vendorInfoVo;
	}

	public Set<PartyProductLine> getPartyProductLineList() {
		return partyProductLineList;
	}

	public void setPartyProductLineList(Set<PartyProductLine> partyProductLineList) {
		this.partyProductLineList = partyProductLineList;
	}

	public VendorCreditVo getVendorCreditVo() {
		return vendorCreditVo;
	}

	public void setVendorCreditVo(VendorCreditVo vendorCreditVo) {
		this.vendorCreditVo = vendorCreditVo;
	}

	public VendorSaleInfoVo getVendorSaleInfoVo() {
		return vendorSaleInfoVo;
	}

	public void setVendorSaleInfoVo(VendorSaleInfoVo vendorSaleInfoVo) {
		this.vendorSaleInfoVo = vendorSaleInfoVo;
	}
	
	

	

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public String getApplyMail() {
		return applyMail;
	}

	public void setApplyMail(String applyMail) {
		this.applyMail = applyMail;
	}





	public enum PurchaseProtocol {
		/**
		 * 已签
		 */
		ALREADY_SIGN,
		/**
		 * 未签already
		 */
		NOT_SIGN,
		/**
		 * 已特批
		 */
		ALREADY_SPE_SIGN;
		public static PurchaseProtocol getCurrency(String purchaseProtocol){
			for(PurchaseProtocol value : PurchaseProtocol.values()){
				if(value.name().equals(purchaseProtocol)){
					return value;
				}
			}
			return null;
		}
	}
	
	public enum SecrecyProtocol {
		/**
		 * 已签
		 */
		ALREADY_SIGN,
		/**
		 * 未签already
		 */
		NOT_SIGN,
		/**
		 * 已特批
		 */
		ALREADY_SPE_SIGN;
		public static SecrecyProtocol getCurrency(String secrecyProtocol ){
			for(SecrecyProtocol value : SecrecyProtocol.values()){
				if(value.name().equals(secrecyProtocol)){
					return value;
				}
			}
			return null;
		}
	}
	
	public enum SupportCurrency {
		/**
		 * 人民币
		 */
		CNY,
		/**
		 * 美元
		 */
		USD,
		/**
		 * 不限
		 */
		XXX;
		public static SupportCurrency getSupportCurrency(String supportCurrency){
			for(SupportCurrency value : SupportCurrency.values()){
				if(value.name().equals(supportCurrency)){
					return value;
				}
			}
			return null;
		}
	}
	
	public enum VendorState {
		/**
		 * 审核中
		 */
		CHECK_IN,
		/**
		 * 通过
		 */
		PASS,
		/**
		 * 不通过
		 */
		NOT_PASS,
		/**
		 * 失效
		 */
		LOSE;
		public static VendorState getVendorState(String vendorState){
			for(VendorState value : VendorState.values()){
				if(value.name().equals(vendorState)){
					return value;
				}
			}
			return null;
		}
	}
	
	public enum Currency {
		/**
		 * 人民币
		 */
		CNY,
		/**
		 * 美元
		 */
		USD
	}
	
	public enum VendorApplyType {
		/**
		 *  建档申请
		 */
		ORG_SUPPLIER_ARCHIVES_REVIEW,
		/**
		 * 基本申请
		 */
		ORG_SUPPLIER_INFO_CHANGE_REVIEW,
		/**
		 * 产品线变更申请
		 */
		ORG_SUPPLIER_PRODUCTLINE_CHANGE_REVIEW,
		/**
		 * 启用申请
		 */
		ORG_SUPPLIER_ENABLED_REVIEW,
		/**
		 * 失效申请
		 */
		ORG_SUPPLIER_INVALID_REVIEW;		
	}
	
	public enum SupplierStatus {
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
		public static SupplierStatus getSupplierStatus(String supplierStatus){
			for(SupplierStatus value : SupplierStatus.values()){
				if(value.name().equals(supplierStatus)){
					return value;
				}
			}
			return null;
		}
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
	
	
}
