package com.yikuyi.party.contact.model;

import javax.validation.constraints.Size;

import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 邮件地址信息数据表
 * @author 1044867128@qq.com
 *
 */
@ApiModel("PostalAddress")
public class PostalAddress extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1267805416730942686L;

	@ApiModelProperty(value = "收件人")
	@Size(max = 100)
	private String toName;

	@ApiModelProperty(value = "经办人姓名")
	@Size(max = 100)
	private String attnName;

	@ApiModelProperty(value = "详细地址一")
	@Size(max = 255)
	private String address1;

	@ApiModelProperty(value = "详细地址二")
	@Size(max = 255)
	private String address2;

	@ApiModelProperty(value = "邮政编码")
	@Size(max = 60)
	private String postalCode;

	@ApiModelProperty(value = "国际名称")
	@Size(max = 50)
	private String countryGeoName;

	@ApiModelProperty(value = "国别ID")
	@Size(max = 20)
	private String countryGeoId;

	@ApiModelProperty(value = "省的名称")
	@Size(max = 50)
	private String provinceGeoName;


	@ApiModelProperty(value = "州、省ID")
	@Size(max = 20)
	private String provinceGeoId;

	@ApiModelProperty(value = "县/区名称")
	@Size(max = 50)
	private String countyGeoName;

	@ApiModelProperty(value = "县/区ID")
	@Size(max = 20)
	private String countyGeoId;

	@ApiModelProperty(value = "省名称，在省的下一级")
	@Size(max = 50)
	private String cityGeoName;

	@ApiModelProperty(value = "省ID")
	@Size(max = 20)
	private String cityGeoId;

	@ApiModelProperty(value = "是否经过验证")
	@Size(max = 1, message = "是否经过验证 ,Y:验证通过")
	private String verified;
	
	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName == null ? null : toName.trim();
	}

	public String getAttnName() {
		return attnName;
	}

	public void setAttnName(String attnName) {
		this.attnName = attnName == null ? null : attnName.trim();
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1 == null ? null : address1.trim();
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2 == null ? null : address2.trim();
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode == null ? null : postalCode.trim();
	}

	public String getCountryGeoName() {
		return countryGeoName;
	}

	public void setCountryGeoName(String countryGeoName) {
		this.countryGeoName = countryGeoName == null ? null : countryGeoName.trim();
	}

	public String getCountryGeoId() {
		return countryGeoId;
	}

	public void setCountryGeoId(String countryGeoId) {
		this.countryGeoId = countryGeoId == null ? null : countryGeoId.trim();
	}

	public String getProvinceGeoName() {
		return provinceGeoName;
	}

	public void setProvinceGeoName(String provinceGeoName) {
		this.provinceGeoName = provinceGeoName == null ? null : provinceGeoName.trim();
	}

	public String getProvinceGeoId() {
		return provinceGeoId;
	}

	public void setProvinceGeoId(String provinceGeoId) {
		this.provinceGeoId = provinceGeoId == null ? null : provinceGeoId.trim();
	}

	public String getCountyGeoName() {
		return countyGeoName;
	}

	public void setCountyGeoName(String countyGeoName) {
		this.countyGeoName = countyGeoName == null ? null : countyGeoName.trim();
	}

	public String getCountyGeoId() {
		return countyGeoId;
	}

	public void setCountyGeoId(String countyGeoId) {
		this.countyGeoId = countyGeoId == null ? null : countyGeoId.trim();
	}

	public String getCityGeoName() {
		return cityGeoName;
	}

	public void setCityGeoName(String cityGeoName) {
		this.cityGeoName = cityGeoName == null ? null : cityGeoName.trim();
	}

	public String getCityGeoId() {
		return cityGeoId;
	}

	public void setCityGeoId(String cityGeoId) {
		this.cityGeoId = cityGeoId == null ? null : cityGeoId.trim();
	}

	public String getVerified() {
		return verified;
	}

	public void setVerified(String verified) {
		this.verified = verified == null ? null : verified.trim();
	}

}