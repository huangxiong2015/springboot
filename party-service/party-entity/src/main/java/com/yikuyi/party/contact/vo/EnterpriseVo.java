/*
 * Created: 2017年1月13日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.yikuyi.party.contact.vo;


import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.group.model.PartyGroup.AccountPeriodStatus;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.PartyAttribute;
import com.yikuyi.party.person.model.Person.PersonTypeStatus;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("EnterpriseVo")
public class EnterpriseVo {
	
	@ApiModelProperty(value = "企业id")
	private String id;
	@ApiModelProperty(value = "父企业id")
	private String corporationId;

	@ApiModelProperty(value = "关联企业id")
	private String relationEnterpriseId;
	
	@ApiModelProperty(value = "企业邮箱")
	private String mail;
	
	@ApiModelProperty(value = "企业名称")
    private String name;

	@ApiModelProperty(value = "企业LOGO")
    private String logo;
	
	@ApiModelProperty(value = "省份id")
    private String province;
	
	@ApiModelProperty(value = "省份")
    private String provinceName;
 	
 	@ApiModelProperty(value = "市Id")
    private String city;
 	
 	@ApiModelProperty(value = "市")
    private String cityName;
 	
	@ApiModelProperty(value = "县Id")
    private String country;
 	
 	@ApiModelProperty(value = "县")
    private String countryName;
 	
 	@ApiModelProperty(value = "详细地址")
    private String address;
 	
 	@ApiModelProperty(value = "公司类型")
    private String corCategory;
 	
 	@ApiModelProperty(value = "所属行业")
    private String industryCategory;
 	
	@ApiModelProperty(value = "企业官网")
    private String webSite;
 	
	@ApiModelProperty(value = "公司简介")
	private String description;
	
	@ApiModelProperty(value = "联系人")
	private String contactUserName;
	
	@ApiModelProperty(value = "联系电话")
	private String contactUserTel;
	
	@ApiModelProperty(value = "电子传真")
	private String fax;
	
	@ApiModelProperty(value = "联系QQ")
	private String contactUserQQ;
	
	@ApiModelProperty(value = "状态：启用、停用")
	private Party.PartyStatus partyStatus;
	
	@ApiModelProperty(value = "激活状态")
	private PartyGroup.ActiveStatus activeStatus;
	
	@ApiModelProperty(value = "账号状态")
	private PartyGroup.AccountStatus accountStatus;
	
	@ApiModelProperty(value = "子账号状态")
	private PersonTypeStatus accountsStatus;
	@ApiModelProperty(value = "公司属性集合")
	private Map<String,String> map;


	@ApiModelProperty(value = "有效状态")
 	private String status;
 	
 	@ApiModelProperty(value = "联系人")
 	private String contacts;

 	@ApiModelProperty(value = "登入次数")
 	private Long loginCount;
 	
 	@ApiModelProperty(value = "注册时间")
 	private Date regTime;
 	
 	@ApiModelProperty(value = "最后登录时间")
 	private Date lastLoginTime;

	@ApiModelProperty(value = "公司资质")
 	private List<PartyAttribute> corporationQualDataList;
 	
	@ApiModelProperty(value = "注册地址")
 	private String registeAddr;
	
	@ApiModelProperty(value = "营业执照")
 	private String busiLicPic;
	
	@ApiModelProperty(value = "税务登记")
 	private String taxRegPic;
	
	@ApiModelProperty(value = "组织机构")
 	private String occPic;
	
	@ApiModelProperty(value = "企业授权委托书")
 	private String loa;
	
	@ApiModelProperty(value = "邓氏编码")
 	private String dCode;
	
	@ApiModelProperty(value = "资料完善度COMPLETE(完善)/NOT-COMPLETE(不完善)")
 	private String infoLevel;
	
	@ApiModelProperty(value = "原因")
 	private String reason;
	
	@ApiModelProperty(value = "是否是管理员")
 	private Boolean isAdmin;
	
	@ApiModelProperty(value = "是否已经激活")
 	private Boolean isActive;
	
	@ApiModelProperty(value = "营业执照类型 普通营业执照  企业三证合一  香港")
 	private String busiLisType;
	
	@ApiModelProperty(value="其它拓展属性")
	private List<Map<String, String>> otherAttrs;
	
	@ApiModelProperty(value = "其它属性")
 	private String otherAttr;

	@ApiModelProperty(value = "企业授权pdf判断")
 	private String loapicType;

	@ApiModelProperty(value = "企业登录id")
	private String loginId;

	@ApiModelProperty(value="资质信息集合")
	private List<Map<String, String>> certificateAttrs;
	
	@ApiModelProperty(value = "用来判断是否是申请主账号的，flag=1为提交申请")
	private String flag;
	
	@ApiModelProperty(value = "企业授权书pdf名字")
 	private String loaPdfName;
	
	@ApiModelProperty(value = "YKY客户编码")
	private String partyCode;
	
	
	@ApiModelProperty(value = "营业执照attrId")
	private String busiAttrId;
	@ApiModelProperty(value = "税务登记attrId")
	private String faxAttrId;
	@ApiModelProperty(value = "组织机构attrId")
	private String occAttrId;
	
	@ApiModelProperty(value = "审核时间")
	private Date approvedDate;
	
	@ApiModelProperty(value = "申请时间")
	private Date applyDate;
	

	@ApiModelProperty(value = "备注")
	private String comments;
	
	@ApiModelProperty(value = "组织机构代码")
 	private String occCode;
	
	@ApiModelProperty(value = "营业期限")
 	private String orgLimit;
	
	@ApiModelProperty(value = "个人升级企业")
 	private String isEnt;

	@ApiModelProperty(value = "职位")
 	private String personalTitle;
	
	@ApiModelProperty(value = "流程id")
	private String applyId;
	
	@ApiModelProperty(value = "子账号流程id")
	private String accountApplyId;
	
	@ApiModelProperty(value = "用来判断是从会员中心来的还是从operation来的")
	private String isVipCenter;
	
	
	@ApiModelProperty(value = "企业用户id")
	private String entUserId;
	
	
	@ApiModelProperty(value = "省份id")
    private String personProvince;
	
	@ApiModelProperty(value = "省份")
    private String personProvinceName;
 	
 	@ApiModelProperty(value = "市Id")
    private String personCity;
 	
 	@ApiModelProperty(value = "市")
    private String personCityName;
 	
	@ApiModelProperty(value = "县Id")
    private String personCountry;
 	
 	@ApiModelProperty(value = "县")
    private String personCountryName;
 	
 	@ApiModelProperty(value = "详细地址")
    private String personAddress;
 	
 	@ApiModelProperty(value = "固定电话")
    private String fixedTel;
 	
	@ApiModelProperty(value = "公司类型其他")
    private String corCategoryOther;
 	
	@ApiModelProperty(value = "信用备注")
    private String creditComments;
	
	@ApiModelProperty(value = "账期状态")
	private AccountPeriodStatus accountPeriodStatus;
	
	@ApiModelProperty(value="用户来源")
	private String roleType;
	
	@ApiModelProperty(value="信用额度")
	private Integer creditQuota;
	
	
	@ApiModelProperty(value = "申请人")
	private String applyUser;
	
	@ApiModelProperty(value = "个人用户名")
	private String personName;
	
	@ApiModelProperty(value = "是否进行审核操作:1申请审核，0没有申请审核")
	private String isApply;
	
	

	public String getIsApply() {
		return isApply;
	}

	public void setIsApply(String isApply) {
		this.isApply = isApply;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}
	
	
	public String getApplyUser() {
		return applyUser;
	}

	public void setApplyUser(String applyUser) {
		this.applyUser = applyUser;
	}

	@ApiModelProperty(value = "企业vipId")
	private String vipId;
	
	
	public String getVipId() {
		return vipId;
	}

	public void setVipId(String vipId) {
		this.vipId = vipId;
	}

	public String getCorCategoryOther() {
		return corCategoryOther;
	}

	public void setCorCategoryOther(String corCategoryOther) {
		this.corCategoryOther = corCategoryOther;
	}

	public String getFixedTel() {
		return fixedTel;
	}

	public void setFixedTel(String fixedTel) {
		this.fixedTel = fixedTel;
	}

	public String getPersonProvince() {
		return personProvince;
	}

	public void setPersonProvince(String personProvince) {
		this.personProvince = personProvince;
	}

	public String getPersonProvinceName() {
		return personProvinceName;
	}

	public void setPersonProvinceName(String personProvinceName) {
		this.personProvinceName = personProvinceName;
	}

	public String getPersonCity() {
		return personCity;
	}

	public void setPersonCity(String personCity) {
		this.personCity = personCity;
	}

	public String getPersonCityName() {
		return personCityName;
	}

	public void setPersonCityName(String personCityName) {
		this.personCityName = personCityName;
	}

	public String getPersonCountry() {
		return personCountry;
	}

	public void setPersonCountry(String personCountry) {
		this.personCountry = personCountry;
	}

	public String getPersonCountryName() {
		return personCountryName;
	}

	public void setPersonCountryName(String personCountryName) {
		this.personCountryName = personCountryName;
	}

	public String getPersonAddress() {
		return personAddress;
	}

	public void setPersonAddress(String personAddress) {
		this.personAddress = personAddress;
	}

	public String getAccountApplyId() {
		return accountApplyId;
	}

	public void setAccountApplyId(String accountApplyId) {
		this.accountApplyId = accountApplyId;
	}


	
	
	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getEntUserId() {
		return entUserId;
	}

	public void setEntUserId(String entUserId) {
		this.entUserId = entUserId;
	}

	public String getIsVipCenter() {
		return isVipCenter;
	}

	public void setIsVipCenter(String isVipCenter) {
		this.isVipCenter = isVipCenter;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getPersonalTitle() {
		return personalTitle;
	}

	public void setPersonalTitle(String personalTitle) {
		this.personalTitle = personalTitle;
	}

	public String getIsEnt() {
		return isEnt;
	}

	public void setIsEnt(String isEnt) {
		this.isEnt = isEnt;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	public String getCorporationId() {
		return corporationId;
	}

	public void setCorporationId(String corporationId) {
		this.corporationId = corporationId;
	}
	
	public PersonTypeStatus getAccountsStatus() {
		return accountsStatus;
	}

	public void setAccountsStatus(PersonTypeStatus accountsStatus) {
		this.accountsStatus = accountsStatus;
	}


	public String getOrgLimit() {
		return orgLimit;
	}

	public void setOrgLimit(String orgLimit) {
		this.orgLimit = orgLimit;
	}

	public String getOccCode() {
		return occCode;
	}

	public void setOccCode(String occCode) {
		this.occCode = occCode;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getBusiAttrId() {
		return busiAttrId;
	}

	public void setBusiAttrId(String busiAttrId) {
		this.busiAttrId = busiAttrId;
	}

	public String getFaxAttrId() {
		return faxAttrId;
	}

	public void setFaxAttrId(String faxAttrId) {
		this.faxAttrId = faxAttrId;
	}

	public String getOccAttrId() {
		return occAttrId;
	}

	public void setOccAttrId(String occAttrId) {
		this.occAttrId = occAttrId;
	}

	public String getPartyCode() {
		return partyCode;
	}

	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}

	public Party.PartyStatus getPartyStatus() {
		return partyStatus;
	}

	public void setPartyStatus(Party.PartyStatus partyStatus) {
		this.partyStatus = partyStatus;
	}

	public PartyGroup.ActiveStatus getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(PartyGroup.ActiveStatus activeStatus) {
		this.activeStatus = activeStatus;
	}

	public PartyGroup.AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(PartyGroup.AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}
	
	public String getLoaPdfName() {
		return loaPdfName;
	}

	public void setLoaPdfName(String loaPdfName) {
		this.loaPdfName = loaPdfName;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public List<Map<String, String>> getCertificateAttrs() {
		return certificateAttrs;
	}

	public void setCertificateAttrs(List<Map<String, String>> certificateAttrs) {
		this.certificateAttrs = certificateAttrs;
	}

	

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoapicType() {
		return loapicType;
	}

	public void setLoapicType(String loapicType) {
		this.loapicType = loapicType;
	}

	public String getBusiLisType() {
		return busiLisType;
	}

	public void setBusiLisType(String busiLisType) {
		this.busiLisType = busiLisType;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getdCode() {
		return dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public String getRegisteAddr() {
		return registeAddr;
	}

	public void setRegisteAddr(String registeAddr) {
		this.registeAddr = registeAddr;
	}

	public String getBusiLicPic() {
		return busiLicPic;
	}

	public void setBusiLicPic(String busiLicPic) {
		this.busiLicPic = busiLicPic;
	}

	public String getTaxRegPic() {
		return taxRegPic;
	}

	public void setTaxRegPic(String taxRegPic) {
		this.taxRegPic = taxRegPic;
	}

	public String getOccPic() {
		return occPic;
	}

	public void setOccPic(String occPic) {
		this.occPic = occPic;
	}

	
	public String getLoa() {
		return loa;
	}

	public void setLoa(String loa) {
		this.loa = loa;
	}

	public List<PartyAttribute> getCorporationQualDataList() {
		return corporationQualDataList;
	}

	public void setCorporationQualDataList(List<PartyAttribute> corporationQualDataList) {
		this.corporationQualDataList = corporationQualDataList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContactUserName() {
		return contactUserName;
	}

	public void setContactUserName(String contactUserName) {
		this.contactUserName = contactUserName;
	}

	public String getContactUserTel() {
		return contactUserTel;
	}

	public void setContactUserTel(String contactUserTel) {
		this.contactUserTel = contactUserTel;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getContactUserQQ() {
		return contactUserQQ;
	}

	public void setContactUserQQ(String contactUserQQ) {
		this.contactUserQQ = contactUserQQ;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public Long getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(Long loginCount) {
		this.loginCount = loginCount;
	}

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
    
	public String getCorCategory() {
		return corCategory;
	}

	public void setCorCategory(String corCategory) {
		this.corCategory = corCategory;
	}

	public String getIndustryCategory() {
		return industryCategory;
	}

	public void setIndustryCategory(String industryCategory) {
		this.industryCategory = industryCategory;
	}

	public String getInfoLevel() {
		return infoLevel;
	}

	public void setInfoLevel(String infoLevel) {
		this.infoLevel = infoLevel;
	}

	public String getRelationEnterpriseId() {
		return relationEnterpriseId;
	}

	public void setRelationEnterpriseId(String relationEnterpriseId) {
		this.relationEnterpriseId = relationEnterpriseId;
	}

	public List<Map<String, String>> getOtherAttrs() {
		return otherAttrs;
	}

	public void setOtherAttrs(List<Map<String, String>> otherAttrs) {
		this.otherAttrs = otherAttrs;
	}

	public String getOtherAttr() {
		return otherAttr;
	}

	public void setOtherAttr(String otherAttr) {
		this.otherAttr = otherAttr;
	}

	public String getCreditComments() {
		return creditComments;
	}

	public void setCreditComments(String creditComments) {
		this.creditComments = creditComments;
	}

	public AccountPeriodStatus getAccountPeriodStatus() {
		return accountPeriodStatus;
	}

	public void setAccountPeriodStatus(AccountPeriodStatus accountPeriodStatus) {
		this.accountPeriodStatus = accountPeriodStatus;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public Integer getCreditQuota() {
		return creditQuota;
	}

	public void setCreditQuota(Integer creditQuota) {
		this.creditQuota = creditQuota;
	}
	
	

}
