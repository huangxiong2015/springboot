package com.yikuyi.party.group.model;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author zr.helinmei@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("SupplierLeadtimeRate")
public class SupplierLeadtimeRate extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1889597353651661083L;

	@ApiModelProperty(value = "partyId")
	private String partyId;
	
	@ApiModelProperty(value = "交期正确率")
	private String rate;
	
	@ApiModelProperty(value = "备注")
	private String comments;

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
