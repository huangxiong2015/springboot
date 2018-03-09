package com.yikuyi.party.customerssync.api.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.yikuyi.party.customerssync.bll.CustomersSyncManager;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringJUnit4ClassRunner.class)
public class CustomersSyncResourceTest {
	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
	private MockMvc mockMvc;
	
	@InjectMocks
	private CustomersSyncResource customersSyncResource;

	@Mock
	private CustomersSyncManager customeSyncManager;

	public CustomersSyncResourceTest() {
		MockitoAnnotations.initMocks(this);
	}

	@Before
	public void setUpBefore() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(customersSyncResource).build();
	}

	LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901",Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

	/**
	 * 全量查询企业信息全量同步数据方法
	 * @param rowBounds
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void getAllEntListTest() throws Exception {
		// 构造mock返回结构数据
		long num = 10;
		when(customeSyncManager.getAllEntList(Mockito.anyInt(), Mockito.anyInt())).thenReturn(num);
		long result = mapper.readValue(mockMvc
				.perform(post("/v1/customer/enterprise/fullsync?page=1&size=10")
						.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), long.class);
		Assert.assertEquals(result, 10);
	}

	/**
	 * 查询企业数据增量同步数据方法
	 * @param selectStart
	 * @param selectEnd
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void getIncrementEntListTest() throws Exception {
		// 构造mock返回结构数据
		long num = 10;
		when(customeSyncManager.getIncrementEntList(Mockito.anyString(),Mockito.anyString(),Mockito.anyInt(), Mockito.anyInt())).thenReturn(num);
		long result = mapper.readValue(mockMvc
				.perform(post("/v1/customer/enterprise/incrsync?createStart='2017-12-21'&createEnd='2017-12-21'&page=1&size=10")
						.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), long.class);
		Assert.assertEquals(result, 10);
	}
	

	/**
	 * 全量查询个人信息全量同步数据方法
	 * @param rowBounds
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void getAllPersonalListTest() throws Exception {
		// 构造mock返回结构数据
		long num = 10;
		when(customeSyncManager.getAllPersonalList(Mockito.anyInt(), Mockito.anyInt())).thenReturn(num);
		long result = mapper.readValue(mockMvc
				.perform(post("/v1/customer/individual/fullsync?page=1&size=10")
						.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), long.class);
		Assert.assertEquals(result, 10);
	}
	
	/**
	 * 查询个人数据增量同步数据方法
	 * @param selectStart
	 * @param selectEnd
	 * @return List<CustomersSyncVo>
	 * @since 2017年12月13日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void getIncrementPersonalListTest() throws Exception {
		// 构造mock返回结构数据
		long num = 10;
		when(customeSyncManager.getIncrementPersonalList(Mockito.anyString(),Mockito.anyString(),Mockito.anyInt(), Mockito.anyInt())).thenReturn(num);
		long result = mapper.readValue(mockMvc
				.perform(post("/v1/customer/individual/incrsync?createStart='2017-12-21'&createEnd='2017-12-21'&page=1&size=10")
						.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), long.class);
		Assert.assertEquals(result, 10);
	}
	
}