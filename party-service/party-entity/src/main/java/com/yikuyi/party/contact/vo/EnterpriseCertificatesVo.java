package com.yikuyi.party.contact.vo;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel("EnterpriseCertificatesVo")
public class EnterpriseCertificatesVo {
	
	@ApiModelProperty(value = "企业id")
	private String id;
	
	@ApiModelProperty(value = "注册地")
	private String registeAddr;
	
	@ApiModelProperty(value = "资质类型")
	private String busiLisType;
	
	@ApiModelProperty(value = "统一社会信用代码")
	private String socialCode;
	@ApiModelProperty(value = "企业名称（全称）")
	private String entName;
	@ApiModelProperty(value = "经营范围")
	private String busiRange;
	@ApiModelProperty(value = "住所")
	private String location;
	
	@ApiModelProperty(value = "纳税人识别号")
	private String faxCode;
	@ApiModelProperty(value = "纳税人名称")
	private String faxName;
	
	
	@ApiModelProperty(value = "机构代码")
	private String orgCode;
	
	@ApiModelProperty(value = "机构名称")
	private String orgName;
	
	@ApiModelProperty(value = "机构地址")
	private String orgLocation;
	
	@ApiModelProperty(value = "成立日期")
	private String orgCdate;
	
	@ApiModelProperty(value = "营业期限")
	private String orgLimit;
	
	
	@ApiModelProperty(value = "签发日期")
	private String hkSignCdate;
	
	@ApiModelProperty(value = "业务所用名称")
	private String hkBusiName;
	
	@ApiModelProperty(value = "地址")
	private String hkAddr;
	
	@ApiModelProperty(value = "生效日期")
	private String hkEffctiveDate;
	
	@ApiModelProperty(value = "营业执照")
 	private String busiLicPic;
	
	@ApiModelProperty(value = "税务登记")
 	private String taxRegPic;
	
	@ApiModelProperty(value = "组织机构")
 	private String occPic;
	
	@ApiModelProperty(value = "企业授权委托书")
 	private String loaPic;
	
	@ApiModelProperty(value = "资质attrID")
 	private String attrId;

	@ApiModelProperty(value="其它拓展属性")
	private List<Map<String, String>> otherAttrs;
	
	public List<Map<String, String>> getOtherAttrs() {
		return otherAttrs;
	}

	public void setOtherAttrs(List<Map<String, String>> otherAttrs) {
		this.otherAttrs = otherAttrs;
	}

	public String getAttrId() {
		return attrId;
	}

	public void setAttrId(String attrId) {
		this.attrId = attrId;
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

	public String getLoaPic() {
		return loaPic;
	}

	public void setLoaPic(String loaPic) {
		this.loaPic = loaPic;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRegisteAddr() {
		return registeAddr;
	}

	public void setRegisteAddr(String registeAddr) {
		this.registeAddr = registeAddr;
	}

	public String getBusiLisType() {
		return busiLisType;
	}

	public void setBusiLisType(String busiLisType) {
		this.busiLisType = busiLisType;
	}

	public String getSocialCode() {
		return socialCode;
	}

	public void setSocialCode(String socialCode) {
		this.socialCode = socialCode;
	}

	public String getEntName() {
		return entName;
	}

	public void setEntName(String entName) {
		this.entName = entName;
	}

	public String getBusiRange() {
		return busiRange;
	}

	public void setBusiRange(String busiRange) {
		this.busiRange = busiRange;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getFaxCode() {
		return faxCode;
	}

	public void setFaxCode(String faxCode) {
		this.faxCode = faxCode;
	}

	public String getFaxName() {
		return faxName;
	}

	public void setFaxName(String faxName) {
		this.faxName = faxName;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgLocation() {
		return orgLocation;
	}

	public void setOrgLocation(String orgLocation) {
		this.orgLocation = orgLocation;
	}

	public String getOrgCdate() {
		return orgCdate;
	}

	public void setOrgCdate(String orgCdate) {
		this.orgCdate = orgCdate;
	}

	public String getOrgLimit() {
		return orgLimit;
	}

	public void setOrgLimit(String orgLimit) {
		this.orgLimit = orgLimit;
	}

	public String getHkSignCdate() {
		return hkSignCdate;
	}

	public void setHkSignCdate(String hkSignCdate) {
		this.hkSignCdate = hkSignCdate;
	}

	public String getHkBusiName() {
		return hkBusiName;
	}

	public void setHkBusiName(String hkBusiName) {
		this.hkBusiName = hkBusiName;
	}

	public String getHkAddr() {
		return hkAddr;
	}

	public void setHkAddr(String hkAddr) {
		this.hkAddr = hkAddr;
	}

	public String getHkEffctiveDate() {
		return hkEffctiveDate;
	}

	public void setHkEffctiveDate(String hkEffctiveDate) {
		this.hkEffctiveDate = hkEffctiveDate;
	}
}
