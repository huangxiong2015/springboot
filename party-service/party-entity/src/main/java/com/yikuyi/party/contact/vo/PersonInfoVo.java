package com.yikuyi.party.contact.vo;


import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

public class PersonInfoVo extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "联系人Id")
	private String contactId;

	@ApiModelProperty(value = "姓名")
	private String name;

	@ApiModelProperty(value = "手机号")
	private String tel;

	@ApiModelProperty(value = "联系人邮箱")
	private String mail;

	@ApiModelProperty(value = "固定电话")
	private String extension;

	@ApiModelProperty(value = "地址")
	private String address;

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
