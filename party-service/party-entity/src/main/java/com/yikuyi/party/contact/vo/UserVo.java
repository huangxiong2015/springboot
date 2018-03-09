package com.yikuyi.party.contact.vo;

import java.util.List;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;

public class UserVo extends AbstractEntity{

	private static final long serialVersionUID = -77398925248759253L;

	@ApiModelProperty(value = "id")
	private String id;

	@ApiModelProperty(value = "uuid")
	private String uuid;
	
	@ApiModelProperty(value = "用户名称")
    private String name;
    
 	@ApiModelProperty(value = "手机号码")
    private String mobile;
 	
 	@ApiModelProperty(value = "邮箱")
 	private String mail;
 	
 	@ApiModelProperty(value = "电话")
    private String telNumber;
 	
 	@ApiModelProperty(value = "性别")
    private String sex;
 	
 	@ApiModelProperty(value = "密码")
	private String password;

 	@ApiModelProperty(value = "新密码")
	private String newPassword;
	
	@ApiModelProperty(value = "手机/邮箱验证码")
	private String validateCode;
	
	@ApiModelProperty(value = "图片验证码")
	private String imgCode;
	
	@ApiModelProperty(value = "关联时间")
	private String relationDate;
	
	@ApiModelProperty(value = "企业ID")
	private String enterpriseId;
	
	@ApiModelProperty(value = "公司名称")
	private String companyName;
	
	@ApiModelProperty(value = "重新发送  是：Y")
	private String reSend;
	
	@ApiModelProperty(value = "状态")
	private String statusId;
	
	@ApiModelProperty(value = "用户id")
	private String partyId;
	
	@ApiModelProperty(value = "是否为企业用户 ：0：不是，1 ：是")
	private String isEnterprise;
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
	@ApiModelProperty(value = "地址")
	private String address;
	
	@ApiModelProperty(value = "职位")
	private String personalTitle;
	@ApiModelProperty(value = "公司类型")
	private String corCategory;
	@ApiModelProperty(value = "公司类型（其他）")
	private String corCategoryOther;
	
	@ApiModelProperty(value = "公司行业")
	private String industryCategory;
	@ApiModelProperty(value = "其他行业")
	private String otherAttr;
	@ApiModelProperty(value = "类型 1：修改手机，2：修改邮箱，3.验证邮箱")
	private String type;

	@ApiModelProperty(value = "所属部门编号")
	private String deptId;
	@ApiModelProperty(value = "是否是部门主管")
	private String managerRole;
	
	@ApiModelProperty(value = "是否是部门主管")
	private boolean manager;
	@ApiModelProperty(value = "用户角色ID集合")
	private List<String> roleList;
	
	@ApiModelProperty(value = "邓氏编码")
 	private String dCode;
	@ApiModelProperty(value = "企业官网")
    private String webSite;
	@ApiModelProperty(value = "qq")
    private String qq;
	@ApiModelProperty(value = "原因")
    private String reason;
	@ApiModelProperty(value = "备注")
	private String comments;
	@ApiModelProperty(value = "优惠卷金额")
	private String couponMoney;
	@ApiModelProperty(value="优惠券的币种")
	private String couponCurrency;
	
	@ApiModelProperty(value = "区分前后台")
	private String opreationSystem;
	
	private EnterpriseVo enterpriseVo;

	@ApiModelProperty(value = "登陆邮箱")
	private String userLoginId;
	
	
	public String getUserLoginId() {
		return userLoginId;
	}

	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}

	
	
	
	public String getOpreationSystem() {
		return opreationSystem;
	}

	public void setOpreationSystem(String opreationSystem) {
		this.opreationSystem = opreationSystem;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getImgCode() {
		return imgCode;
	}

	public void setImgCode(String imgCode) {
		this.imgCode = imgCode;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getRelationDate() {
		return relationDate;
	}

	public void setRelationDate(String relationDate) {
		this.relationDate = relationDate;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getReSend() {
		return reSend;
	}

	public void setReSend(String reSend) {
		this.reSend = reSend;
	}

	public String getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getStatusId() {
		return statusId;
	}


	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getIsEnterprise() {
		return isEnterprise;
	}

	public void setIsEnterprise(String isEnterprise) {
		this.isEnterprise = isEnterprise;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getPersonalTitle() {
		return personalTitle;
	}

	public void setPersonalTitle(String personalTitle) {
		this.personalTitle = personalTitle;
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

	public String getOtherAttr() {
		return otherAttr;
	}

	public void setOtherAttr(String otherAttr) {
		this.otherAttr = otherAttr;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getManagerRole() {
		return managerRole;
	}

	public void setManagerRole(String managerRole) {
		this.managerRole = managerRole;
	}

	public List<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}


	public String getdCode() {
		return dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public EnterpriseVo getEnterpriseVo() {
		return enterpriseVo;
	}

	public void setEnterpriseVo(EnterpriseVo enterpriseVo) {
		this.enterpriseVo = enterpriseVo;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean isManager() {
		return manager;
	}

	public void setManager(boolean manager) {
		this.manager = manager;
	}

	public String getCorCategoryOther() {
		return corCategoryOther;
	}

	public void setCorCategoryOther(String corCategoryOther) {
		this.corCategoryOther = corCategoryOther;
	}

	public String getCouponMoney() {
		return couponMoney;
	}

	public void setCouponMoney(String couponMoney) {
		this.couponMoney = couponMoney;
	}

	public String getCouponCurrency() {
		return couponCurrency;
	}

	public void setCouponCurrency(String couponCurrency) {
		this.couponCurrency = couponCurrency;
	}	
	
	
	
}