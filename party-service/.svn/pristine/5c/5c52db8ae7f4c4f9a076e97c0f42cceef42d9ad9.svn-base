package com.yikuyi.party.credit.model;

import java.util.Date;
import java.util.List;

import com.yikuyi.party.vendor.vo.Vendor.Currency;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author zr.helinmei@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("PartyCredit")
public class PartyCredit extends AbstractEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8364093998046623082L;

	@ApiModelProperty(value = "账期主键ID")
	private String partyCreditId;
	
	@ApiModelProperty(value = "认证企业PARTY_ID")
	private String partyId;
	
	@ApiModelProperty(value = "币种")
	private Currency currency;
	
	@ApiModelProperty(value = "授信额度")
	private double  creditQuota;

	@ApiModelProperty(value = "授信余额")
	private double  realtimeCreditQuota;
	
	@ApiModelProperty(value = "授信期限（月结xxx天）")
	private String  creditDeadline;
	
	@ApiModelProperty(value = "对账日期（纯数字，代表每月几号）")
	private String  checkDate;
	
	@ApiModelProperty(value = "对账周期")
	private String  checkCycle;
	
	@ApiModelProperty(value = "付款方式")
	private String  paymentTerms;
	
	@ApiModelProperty(value = "结算方式")
	private String  settlementMethod;
	
	
	@ApiModelProperty(value = "付款日期（纯数字，代表每月几号）")
	private String  payDate;
	
	@ApiModelProperty(value = "描述说明")
	private String  common;
	
	@ApiModelProperty(value = "附件类")
	private List<PartyAttachment> creditAttachmentList;
	
	

	@ApiModelProperty(value = "申请人")
	private String  applyUser;
	
	@ApiModelProperty(value = "申请人邮箱")
	private String  applyMail;
	
	@ApiModelProperty(value = "申请人联系方式")
	private String  applyInformation;
	
	@ApiModelProperty(value = "有效日期")
	private Date  fromDate;
	
	@ApiModelProperty(value = "失效日期")
	private Date  thruDate;
	
	public List<PartyAttachment> getCreditAttachmentList() {
		return creditAttachmentList;
	}

	public void setCreditAttachmentList(List<PartyAttachment> creditAttachmentList) {
		this.creditAttachmentList = creditAttachmentList;
	}
	

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getThruDate() {
		return thruDate;
	}

	public void setThruDate(Date thruDate) {
		this.thruDate = thruDate;
	}

	public String getApplyUser() {
		return applyUser;
	}

	public void setApplyUser(String applyUser) {
		this.applyUser = applyUser;
	}

	public String getApplyMail() {
		return applyMail;
	}

	public void setApplyMail(String applyMail) {
		this.applyMail = applyMail;
	}

	public String getApplyInformation() {
		return applyInformation;
	}

	public void setApplyInformation(String applyInformation) {
		this.applyInformation = applyInformation;
	}



	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	

	public String getPartyCreditId() {
		return partyCreditId;
	}

	public void setPartyCreditId(String partyCreditId) {
		this.partyCreditId = partyCreditId;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}


	public double getRealtimeCreditQuota() {
		return realtimeCreditQuota;
	}

	public void setRealtimeCreditQuota(double realtimeCreditQuota) {
		this.realtimeCreditQuota = realtimeCreditQuota;
	}

	public String getCreditDeadline() {
		return creditDeadline;
	}

	public void setCreditDeadline(String creditDeadline) {
		this.creditDeadline = creditDeadline;
	}

	public String getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}

	public String getCheckCycle() {
		return checkCycle;
	}

	public void setCheckCycle(String checkCycle) {
		this.checkCycle = checkCycle;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String getCommon() {
		return common;
	}

	public void setCommon(String common) {
		this.common = common;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getSettlementMethod() {
		return settlementMethod;
	}

	public void setSettlementMethod(String settlementMethod) {
		this.settlementMethod = settlementMethod;
	}
	
	public double getCreditQuota() {
		return creditQuota;
	}

	public void setCreditQuota(double creditQuota) {
		this.creditQuota = creditQuota;
	}
}
