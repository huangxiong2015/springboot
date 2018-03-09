package com.yikuyi.activity.vo;

import java.util.List;

import com.yikuyi.activity.model.ActivityProductDraft;

public class ActivityProductDraftVo extends ActivityProductDraft{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5022518196708309171L;
	
	private List<String> qtyBreak;
	
	private List<String> priceBreak;

	public List<String> getQtyBreak() {
		return qtyBreak;
	}

	public void setQtyBreak(List<String> qtyBreak) {
		this.qtyBreak = qtyBreak;
	}

	public List<String> getPriceBreak() {
		return priceBreak;
	}

	public void setPriceBreak(List<String> priceBreak) {
		this.priceBreak = priceBreak;
	}

}
