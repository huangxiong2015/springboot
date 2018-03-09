package com.yikuyi.party.contact.vo;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel("ContactsVo")
public class ContactVo {
	@ApiModelProperty(value = "联系人Id")
	private String contactId;
	
	@ApiModelProperty(value = "职位")
	private String position;
	
	@ApiModelProperty(value = "姓名")
	private String name;
	
	@ApiModelProperty(value = "部门")
	private String department;
	
	@ApiModelProperty(value = "性别")
	private String sex;
	
	@ApiModelProperty(value = "手机号")
	private String tel;
	
	@ApiModelProperty(value = "联系人邮箱")
	private String mail;
	
	@ApiModelProperty(value = "固定电话")
	private String extension;
	@ApiModelProperty(value = "微信")
	private String wexin;

	@ApiModelProperty(value = "QQ")
 	private String qq;
 	
 	@ApiModelProperty(value = "账户状态")
 	private String status;

 	@ApiModelProperty(value = "优惠劵数量")
 	private String couponQty;
 	
 	@ApiModelProperty(value = "优惠劵金额")
 	private String couponAmountCny;
 	
 	@ApiModelProperty(value = "最后登录时间")
 	private Date lastLoginTime;

	@ApiModelProperty(value = "订单数量")
 	private String orderQty;
 	
	@ApiModelProperty(value = "修改人")
 	private String lastModifyUser;
	
	@ApiModelProperty(value = "常用地址")
 	private String defaultAddress;
	
	@ApiModelProperty(value = "注册时间")
    private Date registerTime;

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getWexin() {
		return wexin;
	}

	public void setWexin(String wexin) {
		this.wexin = wexin;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCouponQty() {
		return couponQty;
	}

	public void setCouponQty(String couponQty) {
		this.couponQty = couponQty;
	}

	public String getCouponAmountCny() {
		return couponAmountCny;
	}

	public void setCouponAmountCny(String couponAmountCny) {
		this.couponAmountCny = couponAmountCny;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(String orderQty) {
		this.orderQty = orderQty;
	}

	public String getLastModifyUser() {
		return lastModifyUser;
	}

	public void setLastModifyUser(String lastModifyUser) {
		this.lastModifyUser = lastModifyUser;
	}

	public String getDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(String defaultAddress) {
		this.defaultAddress = defaultAddress;
	}
}
