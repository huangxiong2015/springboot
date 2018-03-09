package com.yikuyi.party.notice.api.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.yikuyi.party.notice.bll.NoticeManager;
import com.yikuyi.party.partyExpand.model.PartyExpand;
import com.ykyframework.exception.BusinessException;

@RunWith(SpringJUnit4ClassRunner.class)
public class NoticeResourceTest {
	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
	private MockMvc mockMvc;
	
	@Mock
	private NoticeManager noticeManager;
	
	@InjectMocks
	private NoticeResource noticeResource;

	public NoticeResourceTest() {
		MockitoAnnotations.initMocks(this);
	}
	@Before
	public void setUpBefore() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(noticeResource).build();
	}

	LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901",
			Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
	/**
	 * 查询用户通知
	 * @param partyId
	 * @return
	 * @since 2017年11月19日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void getPartyExpandListTest() throws Exception {
		// 构造mock返回结构数据
		List<PartyExpand> mockList = new ArrayList<>();
		PartyExpand partyExpand = new PartyExpand();
		partyExpand.setUserName("1");
		mockList.add(partyExpand);
		when(noticeManager.getPartyExpandList(Mockito.anyString())).thenReturn(mockList);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, PartyExpand.class);
		List<PartyExpand> mockListRst = mapper.readValue(mockMvc.perform(get("/v1/notice?partyId=1"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(mockListRst.get(0).getUserName(), "1");
		
	}
	

	/**
	 * 新增通知
	 * @param partyExpand
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 * @throws BusinessException 
	 */
	@Test
	public void insertTest() throws Exception {
		// 构造mock返回结构数据
		PartyExpand partyExpand = new PartyExpand();
		partyExpand.setUserName("1");
		partyExpand.setPartyId("1");
		partyExpand.setEmail("11@qq.com");
		// 构造mock返回结构数据
		mockMvc.perform(post("/v1/notice").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(partyExpand))
				.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk());
		Mockito.verify(noticeManager).insert(Mockito.any(PartyExpand.class));
	}
	
	
	/**
	 * 修改通知
	 * @param partyExpand
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void updateTest() throws Exception {
		// 构造mock返回结构数据
		PartyExpand partyExpand = new PartyExpand();
		partyExpand.setUserName("1");
		partyExpand.setPartyId("1");
		partyExpand.setEmail("11@qq.com");
		partyExpand.setId("1");
		// 构造mock返回结构数据
		mockMvc.perform(put("/v1/notice").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(partyExpand))
				.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk());
		Mockito.verify(noticeManager).update(Mockito.any(PartyExpand.class));
	}
	

	/**
	 * 删除通知
	 * @param partyExpand
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void deleteNoticeTest() throws Exception {
		noticeManager.deleteNotice("1");
	}
	
	/**
	 * 修改通知
	 * @param partyExpand
	 * @since 2017年11月29日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void isExistMailTest() throws Exception {
		// 构造mock返回结构数据
		List<PartyExpand> mockList = new ArrayList<>();
		PartyExpand partyExpand = new PartyExpand();
		partyExpand.setUserName("1");
		mockList.add(partyExpand);
		when(noticeManager.isExistMail(Mockito.anyString())).thenReturn(partyExpand);
		PartyExpand mockListRst = mapper.readValue(mockMvc.perform(get("/v1/notice/isExistMail?email=1111@qq.com"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), PartyExpand.class);
		Assert.assertEquals(mockListRst.getUserName(), "1");
		
	}
}
