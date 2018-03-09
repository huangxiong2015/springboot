package com.yikuyi.party.person;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.yikuyi.party.config.BaseTest;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.credit.model.PartyCredit;
import com.yikuyi.party.credit.vo.PartyCreditVo;
import com.yikuyi.party.login.model.UserLogin;
import com.yikuyi.party.person.model.Person;

public class PersonResourceTest extends BaseTest {

	/**
	 * 根据用户Id获取person信息
	 * 
	 * @since 2017年6月29日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	public void findPersonInfoByPartyIdTest() {
		Person person = super.partyClient.personClient().findPersonInfoByPartyId("9999999901", "YWRtaW46OTk5OTk5OTkwMQ==");
		boolean personExit = true;
		if(person!=null){
			personExit = true;
		}else{
			personExit=false;
		}
		Assert.assertTrue(personExit);
	}
	/**
	 * 根据用户账户查找用户信息
	 * @param account
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	public void getAccountById() {
		UserLogin userLogin = super.partyClient.personClient().getAccountById("admin","YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertEquals(userLogin.getId(),"admin");
	}
	/**
	 * 批量查询用户
	 * @param ids
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * 
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	public void getPartyByIdsTest() {
		List<String> ids = new ArrayList<String>();
		ids.add("9999999901");
		List<UserVo> list = super.partyClient.personClient().getPartyByIds(ids, "YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertTrue(list.size() > 0);
	}
	/**
	 * 根据partyid和类型查询用户
	 * @param id
	 * @param type
	 * @param authToken
	 * @return
	 * @since 2017年6月29日
	 * @author zr.aoxianbing@yikuyi.com
	 */
	@Test
	public void getAccountByIdAndTypeTest() {
		String account = super.partyClient.personClient(String.class).getAccountByIdAndType("833946339057336320", "EMAIL");
		Assert.assertEquals("aoxianbing@yikuyi.com",account);
	}
	
	
	/**
	 * 获取用户详情
	 * 
	 * @since 2017年6月29日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void getBaseInfoDetailTest() {
		UserExtendVo userVo = super.partyClient.personClient().getBaseInfoDetail("709035037445586944", "malan", "YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertEquals("709035037445586944", userVo.getPartyId());

	}
	
	
	/**
	 * 
	 * 根据用户名称查询用户列表（精确查询）
	 * @since 2017年6月29日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void getUserByNameListTest() {
		List<UserVo> list = super.partyClient.personClient().getUserByName("叶良辰", "YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertTrue(list.size()>0);
	}	
	
	/**
	 * 
	 * 根据用户名称查询用户列表（精确查询）
	 * @since 2017年6月29日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void getUserByNameTest() {
		UserVo userVo = super.partyClient.personClient().getUsersByName("叶良辰", "YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertEquals("叶良辰", userVo.getName());
	}	
	
	
	/**
	 * 根据用户ID获取登录密码，登录ID
	 * 
	 * @since 2017年6月29日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	public void findUserLoginTest() {
		UserLogin userLogin = super.partyClient.personClient().findUserLogin("709035037445586944", "YWRtaW46OTk5OTk5OTkwMQ==");
		boolean userLoginExit = true;
		if(null != userLogin){
			userLoginExit = true;
		}else{
			userLoginExit=false;
		}
		Assert.assertTrue(userLoginExit);
	}	
	
	/**
	 * 根据角色类型获取所有个人用户的邮箱
	 * @param roleType
	 * @return
	 * @since 2017年3月23日
	 * @author gongtianyu@yikuyi.com
	 */
	@Test
	public void getEmailListByRoleTypeTest() {
		List<Person> list = super.partyClient.personClient().getEmailListByRoleType("ENTERPRISE_CUST", "YWRtaW46OTk5OTk5OTkwMQ==");
		Assert.assertTrue(list.size()>0);
	}
	
	
	/**
	 * 
	 *  根据partyId查询账期信息
	 * @since 2017年8月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void getPartyCreditInfoTest(){
		PartyCreditVo partyCreditVo = super.partyClient.personClient().getPartyCreditInfo("897361616775938048", null, "YWRtaW46OTk5OTk5OTkwMQ==");
		if(partyCreditVo!=null){
			Assert.assertEquals("9999999901",partyCreditVo.getCreator());
		}
	}
	
	
	/**
	 * 根据partyId修改用户的账期信息
	 * @since 2017年8月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void updatePartyCreditTest(){
		PartyCredit partyCredit = new PartyCredit();
		partyCredit.setPartyId("897361616775938048");
		partyCredit.setRealtimeCreditQuota(1000.00);
		super.partyClient.personClient().updatePartyCredit(partyCredit,"YWRtaW46OTk5OTk5OTkwMQ==");
	}
	
	
	/**
	 * 根据partyId修改用户的账期信息
	 * @since 2017年8月17日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void getCustomersByIds(){
		List<String> strList = new ArrayList<>();
		strList.add("111");
		super.partyClient.personClient().getCustomersByIds(strList,"YWRtaW46OTk5OTk5OTkwMQ==");
	}
}