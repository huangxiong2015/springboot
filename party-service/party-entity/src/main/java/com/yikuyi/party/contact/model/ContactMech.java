package com.yikuyi.party.contact.model;

import javax.validation.constraints.Size;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 地址信息定义表
 * 
 * @author 1044867128@qq.com
 *
 */
@ApiModel("ContactMech")
public class ContactMech extends AbstractEntity {
	private static final long serialVersionUID = 6771634151016306310L;
	@ApiModelProperty(value = "地址ID")
	private String id;

	@ApiModelProperty(value = "地址别名")
	@Size(max = 50, message = "地址别名不能超过50个字符")
	private String alias;

	@ApiModelProperty(value = "电子邮件地址")
	@Size(max = 255, message = "电子邮件地址不能超过255个字符")
	private String email;

	@ApiModelProperty(value = "邮箱是否验证通过")
	@Size(max = 1, message = "邮箱是否验证通过，Y:代表验证通过")
	private String verified;

	@ApiModelProperty(value = "邮件地址信息")
	private PostalAddress postalAddress;

	@ApiModelProperty(value = "联系电话")
	private TelecomNumber telecomNumber;
	
	@ApiModelProperty(value = "属性")
	private ContactMechAttributes contactMechAttributes;
	
	
	/**
	 * 联系方式类型
	 * 
	 * @author 1044867128@qq.com
	 *
	 */
	public enum MechType {
		MOBILE, // 手机
		QQ, // qq
		TELEPHONE,// 座机
		FAX//传真
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias == null ? null : alias.trim();
	}

	public PostalAddress getPostalAddress() {
		return postalAddress;
	}

	public void setPostalAddress(PostalAddress postalAddress) {
		this.postalAddress = postalAddress == null ? null : postalAddress;
	}

	public TelecomNumber getTelecomNumber() {
		return telecomNumber;
	}

	public void setTelecomNumber(TelecomNumber telecomNumber) {
		this.telecomNumber = telecomNumber == null ? null : telecomNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public String getVerified() {
		return verified;
	}

	public void setVerified(String verified) {
		this.verified = verified == null ? null : verified.trim();
	}

	public ContactMechAttributes getContactMechAttributes() {
		return contactMechAttributes;
	}

	public void setContactMechAttributes(ContactMechAttributes contactMechAttributes) {
		this.contactMechAttributes = contactMechAttributes;
	}

	@Override
	public String toString() {
		return "ContactMech [id=" + id + ", alias=" + alias + ", email=" + email + ", verified=" + verified
				+ ", postalAddress=" + postalAddress + ", telecomNumber=" + telecomNumber + ", contactMechAttributes="
				+ contactMechAttributes + "]";
	}
	
}