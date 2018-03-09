package com.yikuyi.party.shipAddress.bll;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.yikuyi.basedata.common.model.Currency;
import com.yikuyi.party.contact.model.ContactMech;
import com.yikuyi.party.contact.model.ContactMech.MechType;
import com.yikuyi.party.contact.model.PostalAddress;
import com.yikuyi.party.contact.model.TelecomNumber;
import com.yikuyi.party.contact.model.TelecomNumber.MobileTelecomNumber;
import com.yikuyi.party.contact.model.TelecomNumber.PhoneTelecomNumber;
import com.yikuyi.party.contact.model.TelecomNumber.QqTelecomNumber;
import com.yikuyi.party.model.PartyContactMech;
import com.yikuyi.party.model.PartyContactMech.PurposeType;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
public class PartyContactMechManagerTest {

	@Autowired
	private PartyContactMechManager partyContactMechManager;
	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));;
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser, RequestAttributes.SCOPE_REQUEST);
	}
	
	/**
	 * 测试插入
	 * 
	 * @since 2016年10月18日
	 * @author zr.tongkun@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = { "party_contact_mech_bll_sample_result.xml" })
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "party_contact_mech_bll_insert_result3.xml")
	public void testInsert() {

		PartyContactMech partyContactMech = new PartyContactMech();
		partyContactMech.setPartyId("888888");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			partyContactMech.setFromDate(formatter.parse("2016-12-05"));
			partyContactMech.setThruDate(formatter.parse("2016-12-05"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		partyContactMech.setPurposeType(PurposeType.BILLING_LOCATION);

		ContactMech contactMech = new ContactMech();
		// 邮箱地址
		contactMech.setId("837220512026329088");
		contactMech.setAlias("易库易公司地址");
		contactMech.setEmail("1044867128@qq.com");
		contactMech.setVerified("Y");

		PostalAddress postalAddress = new PostalAddress();
		postalAddress.setToName("张伟");
		postalAddress.setAttnName("前台代收");
		postalAddress.setAddress1("中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼");
		postalAddress.setAddress2("中国深圳市南山区高新技术产业园麻雀岭工业区M-8栋4楼");
		postalAddress.setPostalCode("0755425");
		postalAddress.setCountryGeoName("中国");
		postalAddress.setCountryGeoId("china");
		postalAddress.setProvinceGeoName("1001");
		postalAddress.setProvinceGeoId("广东省");
		postalAddress.setCountyGeoName("深圳");
		postalAddress.setCountyGeoId("2001");
		postalAddress.setCityGeoName("宝安区");
		postalAddress.setCityGeoId("3001");
		postalAddress.setVerified("Y");
		contactMech.setPostalAddress(postalAddress);

		TelecomNumber telecomNumber = new TelecomNumber();

		MobileTelecomNumber mobileTelecomNumber = new MobileTelecomNumber();
		mobileTelecomNumber.setAreaCode("45");
		mobileTelecomNumber.setCountryCode("86");
		mobileTelecomNumber.setAreaCode("1614655");
		mobileTelecomNumber.setContactNumber("15813723723");
		mobileTelecomNumber.setAskForName("张伟");
		mobileTelecomNumber.setVerified("Y");
		mobileTelecomNumber.setMechType(MechType.MOBILE);
		telecomNumber.setMobileTelecomNumber(mobileTelecomNumber);

		PhoneTelecomNumber phoneTelecomNumber = new PhoneTelecomNumber();
		phoneTelecomNumber.setAreaCode("45");
		phoneTelecomNumber.setCountryCode("86");
		phoneTelecomNumber.setAreaCode("1614655");
		phoneTelecomNumber.setContactNumber("13066939619");
		phoneTelecomNumber.setAskForName("张伟");
		phoneTelecomNumber.setVerified("Y");
		phoneTelecomNumber.setMechType(MechType.TELEPHONE);

		QqTelecomNumber qqTelecomNumber = new QqTelecomNumber();
		qqTelecomNumber.setContactNumber("1044867128");
		qqTelecomNumber.setAskForName("张伟");
		qqTelecomNumber.setVerified("Y");
		qqTelecomNumber.setMechType(MechType.QQ);
		telecomNumber.setQqTelecomNumber(qqTelecomNumber);

		telecomNumber.setPhoneTelecomNumber(phoneTelecomNumber);

		contactMech.setTelecomNumber(telecomNumber);

		partyContactMech.setContactMech(contactMech);
		
		
		partyContactMechManager.insert(partyContactMech);


	}
	/**
	 * 测试插入
	 * 
	 * @since 2016年10月18日
	 * @author zr.tongkun@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "party_contact_mech_bll_insert_result.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "party_contact_mech_bll_insert_result2.xml")
	public void testUpdateDueTime() {
		PartyContactMech partyContactMech = new PartyContactMech();
		partyContactMech.setPartyId("77777");
		ContactMech contactMech = new ContactMech();
		// 邮箱地址
		contactMech.setId("837220512026329088");
		contactMech.setAlias("易库易公司地址");
		contactMech.setEmail("1044867128@qq.com");
		contactMech.setVerified("Y");
		partyContactMech.setContactMech(contactMech);
		partyContactMechManager.updateDueTime("77777",partyContactMech);
	}
	

	
	/**
	 * 测试插入
	 * 
	 * @since 2016年10月18日
	 * @author zr.tongkun@yikuyi.com
	 * @throws IOException 
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = { "party_contact_sample.xml" })
	public void testSelectPartyContactMechByIdAndType() throws IOException {
		partyContactMechManager.selectPartyContactMechByIdAndType("9999999901",PurposeType.BILLING_LOCATION,Currency.CNY);
	}
	
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = { "party_profile_default_sample.xml" })
	public void testDeletePartyContactMech(){
		partyContactMechManager.deletePartyContactMech("10001");
		partyContactMechManager.deletePartyContactMech("20002");
	}
	
	
	/**
	 * 物流列表信息
	 * @since 2017年7月26日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT , value={"contact_mech_list.xml"})
	public void testBatchAddressList(){
		List<String> list = new ArrayList<>();
		list.add("829579635091046401");
		List<ContactMech> mechlist = partyContactMechManager.selectPartyContactMechList(list);
		assertEquals(1,mechlist.size());
		ContactMech e = mechlist.get(0);
		assertEquals("hahahh",e.getAlias());
		assertEquals("zhenggaochun2@21cn.com", e.getEmail());
	}
	
}