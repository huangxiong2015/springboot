package com.yikuyi.party.partygroup.bll;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
import com.yikuyi.info.InfoClientBuilder;
import com.yikuyi.info.resource.NewsClient;
import com.yikuyi.news.model.News;
import com.yikuyi.party.group.vo.PartyGroupVo;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.vo.PartyVo;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class, MockitoTestExecutionListener.class })
@Transactional
@Rollback
public class PartyGroupManagerTest {

	@Autowired
	private PartyGroupManager partyGroupManager;
	@SpyBean
	private InfoClientBuilder infoClientBuilder;

	public PartyGroupManagerTest() {
		MockitoAnnotations.initMocks(this);
	}

	@Before
	public void init() {
		LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
		;
		RequestContextHolder.currentRequestAttributes().setAttribute(LoginUserInjectionInterceptor.LOGIN_USER_KEY,
				loginUser, RequestAttributes.SCOPE_REQUEST);
	}

	/**
	 * 查询供应商
	 * 
	 * @since 2016年11月10日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = { "party_partygroup_sample.xml" })
	public void testGetAllPartyGroupList() {
		PartyGroupVo param = new PartyGroupVo();
		List<PartyVo> list = partyGroupManager.getAllPartyGroupList(param, RowBounds.DEFAULT);
		List<String> list1 = new ArrayList<>();
		list1.add("1");
		News news = new News();
		news.setNewsIds(list1);
		news.setStatus("PUBLISHED");
		news.setCategoryTypeId("VENDOR");
		List<News> newList = new ArrayList<>();
		NewsClient newsClient = Mockito.mock(NewsClient.class);
		Mockito.when(infoClientBuilder.builderNewsClient()).thenReturn(newsClient);
		Mockito.when(newsClient.newsBatch(news)).thenReturn(newList);
		assertEquals("1", list.get(0).getId());

	}

	/**
	 * 根据名称查询数据
	 * 
	 * @since 2017年11月10日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = { "party_partygroup_sample.xml" })
	public void testFindPartyGroupByName() {
		partyGroupManager.findPartyGroupByName("深圳市新蕾电子有限公司test");

	}

	/**
	 * 根据名称查询数据
	 * 
	 * @since 2017年11月10日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = { "party_partygroup_sample.xml" })
	public void testFindPartyGroupByNameFull() {
		partyGroupManager.findPartyGroupByNameFull("深圳市新蕾电子有限公司test");

	}

	/**
	 * 根据名称查询数据
	 * 
	 * @since 2017年11月10日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = { "party_partygroup_sample.xml" })
	public void testGetPartyGroupByGroupId() {
		Party party = partyGroupManager.getPartyGroupByGroupId("1");
		assertEquals("1", party.getId());
	}

	/**
	 * 根据名称查询数据
	 * 
	 * @since 2017年11月10日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.REFRESH, value = { "party_partygroup_sample.xml" })
	public void testOrderPermissions() {
		partyGroupManager.orderPermissions("1");
	}

	/**
	 * 新增物流信息
	 * 
	 * @since 2017年6月7日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "partygroup_info_sample.xml")
	public void insertLogisticsCompanyTest() {
		PartyGroupVo vo = new PartyGroupVo();
		vo.setGroupName("aaa");
		partyGroupManager.insertLogisticsCompany(vo);
	}

	/**
	 * 编辑物流信息
	 * 
	 * @since 2017年6月7日
	 * @author tb.yumu@yikuyi.com
	 */
	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "partygroup_info_sample.xml")
	public void updateLogisticsCompanyTest() {
		PartyGroupVo vo = new PartyGroupVo();
		vo.setPartyId("123456789");
		vo.setGroupName("aaa");
		partyGroupManager.updateLogisticsCompany(vo);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "partygroup_info_sample.xml")
	public void changeLogisticsCompanyStatusTest() {
		PartyGroupVo vo = new PartyGroupVo();
		vo.setPartyId("123456789");
		vo.setStatus(PartyStatus.PARTY_ENABLED);
		partyGroupManager.changeLogisticsCompanyStatus(vo);
	}

}