package com.yikuyi.party.contact.vo;

import java.util.List;

import com.yikuyi.party.group.model.PartyGroup.AccountPeriodStatus;
import com.yikuyi.party.group.model.PartyGroup.AccountStatus;
import com.yikuyi.party.group.model.PartyGroup.ActiveStatus;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.person.model.Person.PersonTypeStatus;

import io.swagger.annotations.ApiModelProperty;

public class EnterpriseParamVo {
	
	private String entId;
	
	private String mail;
	
	private String name;
	
	private PartyStatus status;
	
	private ActiveStatus verifyStatus;
	
	private String registerStart;
	
	private String registerEnd;
	
	private String lastLoginStart;
	
	private String lastLoginEnd;
	
	private String phone;
	
	private String partyCode;
	private String corCategory;
	private PersonTypeStatus accountsStatus;
	
	private String industry;
	private String province;
	private String city;
	private String county;
	private String applyDateStart;
	private String applyDateEnd;
	private String orgLimitStart;
	private String orgLimitEnd;
	private String approvedDateStart;
	private String approvedDateEnd;
	//联系人
	private String contactUserName;
	
	//手机号码
	private String contactUserTel;
	
	@ApiModelProperty(value = "类型集合")
 	private  List<String> activeTypeList;
	
	@ApiModelProperty(value = "其他字段查询")
	private String industryOther;
	
	private AccountStatus accountStatus;
	
	@ApiModelProperty(value = "公司类型其他字段")
	private String corCategoryOther;
	

	@ApiModelProperty(value = "账期状态")
	private AccountPeriodStatus accountPeriodStatus;
	
	public String getCorCategoryOther() {
		return corCategoryOther;
	}

	public void setCorCategoryOther(String corCategoryOther) {
		this.corCategoryOther = corCategoryOther;
	}

	public String getIndustryOther() {
		return industryOther;
	}

	public void setIndustryOther(String industryOther) {
		this.industryOther = industryOther;
	}

	public String getContactUserTel() {
		return contactUserTel;
	}

	public void setContactUserTel(String contactUserTel) {
		this.contactUserTel = contactUserTel;
	}

	public String getContactUserName() {
		return contactUserName;
	}

	public void setContactUserName(String contactUserName) {
		this.contactUserName = contactUserName;
	}

	
	
	public List<String> getActiveTypeList() {
		return activeTypeList;
	}

	public void setActiveTypeList(List<String> activeTypeList) {
		this.activeTypeList = activeTypeList;
	}
	public String getApprovedDateStart() {
		return approvedDateStart;
	}

	public void setApprovedDateStart(String approvedDateStart) {
		this.approvedDateStart = approvedDateStart;
	}

	public String getApprovedDateEnd() {
		return approvedDateEnd;
	}

	public void setApprovedDateEnd(String approvedDateEnd) {
		this.approvedDateEnd = approvedDateEnd;
	}

	public String getPartyCode() {
		return partyCode;
	}

	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}


	public String getCorCategory() {
		return corCategory;
	}

	public void setCorCategory(String corCategory) {
		this.corCategory = corCategory;
	}



	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getApplyDateStart() {
		return applyDateStart;
	}

	public void setApplyDateStart(String applyDateStart) {
		this.applyDateStart = applyDateStart;
	}

	public String getApplyDateEnd() {
		return applyDateEnd;
	}

	public void setApplyDateEnd(String applyDateEnd) {
		this.applyDateEnd = applyDateEnd;
	}


	public String getOrgLimitStart() {
		return orgLimitStart;
	}

	public void setOrgLimitStart(String orgLimitStart) {
		this.orgLimitStart = orgLimitStart;
	}

	public String getOrgLimitEnd() {
		return orgLimitEnd;
	}

	public void setOrgLimitEnd(String orgLimitEnd) {
		this.orgLimitEnd = orgLimitEnd;
	}

	public String getEntId() {
		return entId;
	}

	public void setEntId(String entId) {
		this.entId = entId;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getRegisterStart() {
		return registerStart;
	}

	public void setRegisterStart(String registerStart) {
		this.registerStart = registerStart;
	}

	public String getRegisterEnd() {
		return registerEnd;
	}

	public void setRegisterEnd(String registerEnd) {
		this.registerEnd = registerEnd;
	}

	public String getLastLoginStart() {
		return lastLoginStart;
	}

	public void setLastLoginStart(String lastLoginStart) {
		this.lastLoginStart = lastLoginStart;
	}

	public String getLastLoginEnd() {
		return lastLoginEnd;
	}

	public void setLastLoginEnd(String lastLoginEnd) {
		this.lastLoginEnd = lastLoginEnd;
	}


	public PartyStatus getStatus() {
		return status;
	}

	public void setStatus(PartyStatus status) {
		this.status = status;
	}

	public ActiveStatus getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(ActiveStatus verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	

	public PersonTypeStatus getAccountsStatus() {
		return accountsStatus;
	}

	public void setAccountsStatus(PersonTypeStatus accountsStatus) {
		this.accountsStatus = accountsStatus;
	}

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public AccountPeriodStatus getAccountPeriodStatus() {
		return accountPeriodStatus;
	}

	public void setAccountPeriodStatus(AccountPeriodStatus accountPeriodStatus) {
		this.accountPeriodStatus = accountPeriodStatus;
	}
	
	
}
