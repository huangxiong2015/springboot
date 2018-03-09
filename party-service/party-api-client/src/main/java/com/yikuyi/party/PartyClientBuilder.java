package com.yikuyi.party;

import com.yikuyi.party.config.BaseClient;
import com.yikuyi.party.resource.AclClient;
import com.yikuyi.party.resource.CarrierClient;
import com.yikuyi.party.resource.ContactMechClient;
import com.yikuyi.party.resource.CustomerSummeryClient;
import com.yikuyi.party.resource.DeptClient;
import com.yikuyi.party.resource.EnterpriseClient;
import com.yikuyi.party.resource.FacilityClient;
import com.yikuyi.party.resource.FindPasswordClient;
import com.yikuyi.party.resource.NoticeClient;
import com.yikuyi.party.resource.PartyAddressClient;
import com.yikuyi.party.resource.PartyGroupClient;
import com.yikuyi.party.resource.PersonClient;
import com.yikuyi.party.resource.RegisterClient;
import com.yikuyi.party.resource.SupplierClient;
import com.yikuyi.party.resource.UserClient;
import com.yikuyi.party.resource.UserLoginClient;
import com.yikuyi.party.resource.VendorsClient;

public class PartyClientBuilder extends BaseClient {

	public PartyClientBuilder(String baseUrl) {
		this.setBaseUrl(baseUrl);
	}

	public AclClient aclClient() {
		return super.builder(AclClient.class);
	}

	public ContactMechClient contactMechClient() {
		return super.builder(ContactMechClient.class);
	}

	/**
	 * 适用方法返回参数是复杂对象
	 * 
	 * @return
	 */
	public PersonClient personClient() {
		return super.builder(PersonClient.class);
	}

	/**
	 * 适用方法返回参数是String对象
	 * 
	 * @param javaClass
	 * @return
	 */
	public PersonClient personClient(Class<String> javaClass) {
		return super.builder(PersonClient.class, javaClass);
	}

	public PartyAddressClient partyAddressClient() {
		return super.builder(PartyAddressClient.class);
	}

	public UserLoginClient userLoginClient() {
		return super.builder(UserLoginClient.class);
	}

	/**
	 * 适用方法返回参数是复杂对象
	 * 
	 * @return
	 */
	public RegisterClient registerClient() {
		return super.builder(RegisterClient.class);
	}

	public RegisterClient registerClient(Class<String> javaClass) {
		return super.builder(RegisterClient.class, javaClass);
	}

	public FindPasswordClient findPasswordClient() {
		return super.builder(FindPasswordClient.class);
	}

	public PartyGroupClient partyGroupClient() {
		return super.builder(PartyGroupClient.class);
	}

	public EnterpriseClient enterpriseClient() {
		return super.builder(EnterpriseClient.class);
	}

	public UserClient userClient() {
		return super.builder(UserClient.class);
	}

	public UserClient userClient(Class<String> javaClass) {
		return super.builder(UserClient.class, javaClass);
	}

	public DeptClient deptClient() {
		return super.builder(DeptClient.class);
	}

	public CustomerSummeryClient customerSummeryClient() {
		return super.builder(CustomerSummeryClient.class);
	}

	public CarrierClient carrierClient() {
		return super.builder(CarrierClient.class);
	}

	public VendorsClient vendorsClient() {
		return super.builder(VendorsClient.class);
	}

	/**
	 * @return
	 */
	public FacilityClient facilityResource() {
		return super.builder(FacilityClient.class);
	}

	public NoticeClient noticeClient() {
		return super.builder(NoticeClient.class);
	}
	
	/**
	 * 定义了供应商的SDK接口
	 * @return
	 * @since 2018年1月19日
	 * @author jik.shu@yikuyi.com
	 */
	public SupplierClient supplierClient() {
		return super.builder(SupplierClient.class);
	}

}