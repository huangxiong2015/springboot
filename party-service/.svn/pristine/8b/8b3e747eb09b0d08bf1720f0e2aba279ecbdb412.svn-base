package com.yikuyi.party.contact.model;

import com.yikuyi.party.contact.model.ContactMech.MechType;
import com.ykyframework.model.AbstractEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 联系电话
 * 
 * @author 1044867128@qq.com
 *
 */
@ApiModel("TelecomNumber")
public class TelecomNumber extends AbstractEntity {

	private static final long serialVersionUID = -173534793893745844L;
	
	@ApiModelProperty(value = "座机")
	private PhoneTelecomNumber phoneTelecomNumber;
	
	@ApiModelProperty(value = "手机")
	private MobileTelecomNumber mobileTelecomNumber;
	
	@ApiModelProperty(value = "qq")
	private QqTelecomNumber qqTelecomNumber;
	
	@ApiModelProperty(value = "传真")
	private FaxTelecomNumber faxTelecomNumber;
	

	public FaxTelecomNumber getFaxTelecomNumber() {
		return faxTelecomNumber;
	}

	public void setFaxTelecomNumber(FaxTelecomNumber faxTelecomNumber) {
		this.faxTelecomNumber = faxTelecomNumber;
	}

	public PhoneTelecomNumber getPhoneTelecomNumber() {
		return phoneTelecomNumber;
	}

	public void setPhoneTelecomNumber(PhoneTelecomNumber phoneTelecomNumber) {
		this.phoneTelecomNumber = phoneTelecomNumber;
	}

	public MobileTelecomNumber getMobileTelecomNumber() {
		return mobileTelecomNumber;
	}

	public void setMobileTelecomNumber(MobileTelecomNumber mobileTelecomNumber) {
		this.mobileTelecomNumber = mobileTelecomNumber;
	}

	public QqTelecomNumber getQqTelecomNumber() {
		return qqTelecomNumber;
	}

	public void setQqTelecomNumber(QqTelecomNumber qqTelecomNumber) {
		this.qqTelecomNumber = qqTelecomNumber;
	}
	
	@ApiModel("QqTelecomNumber")
	public static class QqTelecomNumber extends PhoneTelecomNumber {
		private static final long serialVersionUID = -337002130506796377L;
	}

	@ApiModel("MobileTelecomNumber")
	public static class MobileTelecomNumber extends PhoneTelecomNumber {
		private static final long serialVersionUID = -337002130506796377L;
	}

	@ApiModel("FaxTelecomNumber")
	public static class FaxTelecomNumber extends PhoneTelecomNumber {
		private static final long serialVersionUID = -337002130506796377L;
	}
	
	@ApiModel("PhoneTelecomNumber")
	public static class PhoneTelecomNumber extends AbstractEntity {

		private static final long serialVersionUID = 3537138499714393085L;
		
		@ApiModelProperty(value = "国家编码")
		private String countryCode;

		@ApiModelProperty(value = "区号")
		private String areaCode;

		@ApiModelProperty(value = "电话号码")
		private String contactNumber;

		@ApiModelProperty(value = "姓名")
		private String askForName;

		@ApiModelProperty(value = "是否已经验证过，Y: 验证通过")
		private String verified;
		
		@ApiModelProperty(value = "电话类型 MOBILE, 手机QQ, qq TELEPHONE 座机")
		private MechType mechType;

		public String getCountryCode() {
			return countryCode;
		}

		public void setCountryCode(String countryCode) {
			this.countryCode = countryCode == null ? null : countryCode.trim();
		}

		public String getAreaCode() {
			return areaCode;
		}

		public void setAreaCode(String areaCode) {
			this.areaCode = areaCode == null ? null : areaCode.trim();
		}

		public String getContactNumber() {
			return contactNumber;
		}

		public void setContactNumber(String contactNumber) {
			this.contactNumber = contactNumber == null ? null : contactNumber.trim();
		}

		public String getAskForName() {
			return askForName;
		}

		public void setAskForName(String askForName) {
			this.askForName = askForName == null ? null : askForName.trim();
		}

		public String getVerified() {
			return verified;
		}

		public void setVerified(String verified) {
			this.verified = verified == null ? null : verified.trim();
		}

		public MechType getMechType() {
			return mechType;
		}

		public void setMechType(MechType mechType) {
			this.mechType = mechType;
		}

	}
}