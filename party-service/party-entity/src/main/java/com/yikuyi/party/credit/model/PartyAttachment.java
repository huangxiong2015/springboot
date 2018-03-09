package com.yikuyi.party.credit.model;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author zr.helinmei@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("PartyAttachment")
public class PartyAttachment extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6977688736647416948L;
	
	
	@ApiModelProperty(value = "附件主键")
    private String partyAttachmentId;

	@ApiModelProperty(value = "外键关联(账期ID，银行ID，供应商ID)")
    private String partyId;

	@ApiModelProperty(value = "附件名称")
	private String attachmentName;
	
	@ApiModelProperty(value = "附件地址")
	private String attachmentUrl;
	
	@ApiModelProperty(value = "账期附件类型")
	private AttachmentType attachmentType;
	

	public String getPartyAttachmentId() {
		return partyAttachmentId;
	}

	public void setPartyAttachmentId(String partyAttachmentId) {
		this.partyAttachmentId = partyAttachmentId;
	}
	
	public AttachmentType getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(AttachmentType attachmentType) {
		this.attachmentType = attachmentType;
	}

	public enum AttachmentType{
		/**
		 *  账期附件类型
		 */
		CREDIT_APPLY,
		/**
		 *  供应商信用
		 */
		VENDOR_CREDIT
	}
	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public String getAttachmentUrl() {
		return attachmentUrl;
	}

	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}


	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	
}
