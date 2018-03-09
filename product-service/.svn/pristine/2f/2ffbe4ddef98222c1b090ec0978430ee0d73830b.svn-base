/*
 * Created: 2017年8月11日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.product.model;

import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModelProperty;

/**
 * 物料审核表
 * @author injor.huang@yikuyi.com
 * @version 1.0.0
 */
@Document(collection="product_stand_audit")
public class ProductStandAudit extends ProductStand {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value="审核状态 0:待审核 1:未通过	2:通过")
	private Integer auditStatus;
	@ApiModelProperty(value="原因")
	private String reason;
	
	public ProductStandAudit(){}
	
	public Integer getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}



	public enum AuditStatus{
		/**
		 *待审核
		 */
		WAIT_AUDIT("待审核",0),
		/**
		 * 未通过
		 */
		NO_PASS("未通过",1),
		/**
		 * 通过
		 */
		PASS("通过",2);
		
		private String name;
		private int value;
		private AuditStatus(String name, int value) {
			this.name = name;
			this.value = value;
		}
		// 普通方法
        public static String getName(Integer value) {
        	if(value == null){
        		return null;
        	}
            for (AuditStatus c : AuditStatus.values()) {
                if (c.getValue() == value) {
                    return c.name;
                }
            }
            return null;
        }

		public String getName() {
			return name;
		}

		public int getValue() {
			return value;
		}
		
	}
	
}
