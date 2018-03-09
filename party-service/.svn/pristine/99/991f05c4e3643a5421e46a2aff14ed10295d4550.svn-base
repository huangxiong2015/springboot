package com.yikuyi.party.person.model;


import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author zr.aoxianbing@yikuyi.com
 * @version 1.0.0
 */
@ApiModel("Person")
public class Person extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5700601759583307976L;
	@ApiModelProperty(value = "称呼")
	private String salutation;
	@ApiModelProperty(value = "姓")
	private String firstName;
	@ApiModelProperty(value = "名")
	private String lastName;
	@ApiModelProperty(value = "职位、头衔")
	private String personalTitle;
	@ApiModelProperty(value = "本地姓")
	private String firstNameLocal;
	@ApiModelProperty(value = "本地名")
	private String lastNameLocal;
	@ApiModelProperty(value = "性别")
	private Integer gender;
	@ApiModelProperty(value = "备注")
	private String comments;
	@ApiModelProperty(value = "职业")
	private String occupation;
	
	@ApiModelProperty(value = "图片地址大图")
	private String logoImageUrl;
	
	@ApiModelProperty(value = "图片地址小图")
	private String logoImageUrlSmall;
	
	@ApiModelProperty(value = "用户关联状态")
	private RelationSratus relationSratus;

	@ApiModelProperty(value = "电话")
	private String tel;
	@ApiModelProperty(value = "邮箱")
	private String mail;
	@ApiModelProperty(value = "手机是否验证 N：未验证，Y：已验证")
	private String telStatus;
	@ApiModelProperty(value = "邮箱是否验证N：未验证，Y：已验证")
	private String mailStatus;
	@ApiModelProperty(value = "子账号状态")
	private PersonTypeStatus personTypeStatus;
	@ApiModelProperty(value = "固定电话")
	private String fixedTel;
	
	
	public String getFixedTel() {
		return fixedTel;
	}
	public void setFixedTel(String fixedTel) {
		this.fixedTel = fixedTel;
	}
	public enum RelationSratus{
		/**
		 * 表示已经关联
		 */
		RELATED ,
		/**
		 * 表示未关联
		 */
		NOT_RELATED,
		/**
		 * 表示待审核
		 */
		WAIT_APPROVE , 
		/**
		 * 表示驳回
		 */
		REJECTED
	}
	public enum PersonTypeStatus{
		/**
		 * 主账号
		 */
		MAIN ,
		/**
		 * 子账号
		 */
		SON,
		/**
		 * 普通账户
		 */
		COMMON 
		
	}
	public PersonTypeStatus getPersonTypeStatus() {
		return personTypeStatus;
	}
	public void setPersonTypeStatus(PersonTypeStatus personTypeStatus) {
		this.personTypeStatus = personTypeStatus;
	}
	public String getSalutation() {
		return salutation;
	}
	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPersonalTitle() {
		return personalTitle;
	}
	public void setPersonalTitle(String personalTitle) {
		this.personalTitle = personalTitle;
	}
	public String getFirstNameLocal() {
		return firstNameLocal;
	}
	public void setFirstNameLocal(String firstNameLocal) {
		this.firstNameLocal = firstNameLocal;
	}
	public String getLastNameLocal() {
		return lastNameLocal;
	}
	public void setLastNameLocal(String lastNameLocal) {
		this.lastNameLocal = lastNameLocal;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getLogoImageUrl() {
		return logoImageUrl;
	}
	public void setLogoImageUrl(String logoImageUrl) {
		this.logoImageUrl = logoImageUrl;
	}
	public String getLogoImageUrlSmall() {
		return logoImageUrlSmall;
	}
	public void setLogoImageUrlSmall(String logoImageUrlSmall) {
		this.logoImageUrlSmall = logoImageUrlSmall;
	}
	public RelationSratus getRelationSratus() {
		return relationSratus;
	}
	public void setRelationSratus(RelationSratus relationSratus) {
		this.relationSratus = relationSratus;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
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
	public String getTelStatus() {
		return telStatus;
	}
	public void setTelStatus(String telStatus) {
		this.telStatus = telStatus;
	}
	public String getMailStatus() {
		return mailStatus;
	}
	public void setMailStatus(String mailStatus) {
		this.mailStatus = mailStatus;
	}
	
	
}
