package com.yikuyi.activity.vo;

import java.io.Serializable;


public class SupplierRuleVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String supplierId;
	
	private String spuplierName;

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getSpuplierName() {
		return spuplierName;
	}

	public void setSpuplierName(String spuplierName) {
		this.spuplierName = spuplierName;
	}

	@Override
	public String toString() {
		return new StringBuilder(this.supplierId).append("_*").toString();
	}
}