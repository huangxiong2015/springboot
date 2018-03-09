package com.yikuyi.party.model;

import com.yikuyi.party.contact.model.ContactMech;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

public class PartyProfileDefault extends AbstractEntity {

	private static final long serialVersionUID = 7316055130562188560L;

	@ApiModelProperty(value = "用户partyId")
	private String partyId;
	@ApiModelProperty(value = "默认收货地址(不用)")
	private ContactMech defaultShipAddr;
	@ApiModelProperty(value = "默认地址发票收货地址")
	private ContactMech defaultBillAddr;
	@ApiModelProperty(value = "默认内地收货地址")
	private ContactMech defaultShipAddrCNY;
	@ApiModelProperty(value = "默认香港收货地址")
	private ContactMech defaultShipAddrHK;

	/**
	 * 默认类型
	 * 
	 * @author 张伟
	 *
	 */
	public enum DefaultAddressType {
		PRODUCT_STORE_ID,//会员店铺ID
		DEFAULT_SHIP_ADDR_CNY, // 默认国内收货地址
		DEFAULT_SHIP_ADDR_HK,//默认香港收货地址
		DEFAULT_BILL_ADDR, // 默认账单地址
		DEFAULT_PAY_METH, // 默认支付方式
		DEFAULT_SHIP_METH// 模式收货方式
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public ContactMech getDefaultShipAddr() {
		return defaultShipAddr;
	}

	public void setDefaultShipAddr(ContactMech defaultShipAddr) {
		this.defaultShipAddr = defaultShipAddr;
	}

	public ContactMech getDefaultBillAddr() {
		return defaultBillAddr;
	}

	public void setDefaultBillAddr(ContactMech defaultBillAddr) {
		this.defaultBillAddr = defaultBillAddr;
	}

	public ContactMech getDefaultShipAddrCNY() {
		return defaultShipAddrCNY;
	}

	public void setDefaultShipAddrCNY(ContactMech defaultShipAddrCNY) {
		this.defaultShipAddrCNY = defaultShipAddrCNY;
	}

	public ContactMech getDefaultShipAddrHK() {
		return defaultShipAddrHK;
	}

	public void setDefaultShipAddrHK(ContactMech defaultShipAddrHK) {
		this.defaultShipAddrHK = defaultShipAddrHK;
	}
	
	

}