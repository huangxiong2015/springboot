package com.yikuyi.party.model;

import java.util.Date;

import com.yikuyi.party.contact.model.ContactMech;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户地址
 * 
 * @author 1044867128@qq.com
 *
 */
@ApiModel("PartyContactMech")
public class PartyContactMech extends AbstractEntity {

	private static final long serialVersionUID = 322130883619554825L;
	@ApiModelProperty(value = "用户id")
	private String partyId;

	@ApiModelProperty(value = "地址")
	private ContactMech contactMech;

	@ApiModelProperty(value = "生效日期", example = "2016/01/01 08:00:00")
	private Date fromDate;

	@ApiModelProperty(value = "失效日期", example = "2016/01/01 08:00:00")
	private Date thruDate;

	@ApiModelProperty(value = "联系地址用途类型")
	private PurposeType purposeType;

	/**
	 * 地址用途类型
	 * 
	 * @author 1044867128@qq.com
	 *
	 */
	public enum PurposeType {
		BILLING_LOCATION, // 发票接受地址
		PICKUP_BYSELF, // PICKUP_BYSELF
		PRIMARY_LOCATION, // 主要地址（注册时候需要验证的地址）
		REGISTER_LOCATION, // 公司注册地址
		SHIPPING_LOCATION;// 收货地址
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId == null ? null : partyId.trim();
	}

	public ContactMech getContactMech() {
		return contactMech;
	}

	public void setContactMech(ContactMech contactMech) {
		this.contactMech = contactMech;
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

	public PurposeType getPurposeType() {
		return purposeType;
	}

	public void setPurposeType(PurposeType purposeType) {
		this.purposeType = purposeType;
	}
	
}
