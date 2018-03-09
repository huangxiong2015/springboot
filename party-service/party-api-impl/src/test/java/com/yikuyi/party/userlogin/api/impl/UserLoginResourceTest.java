package com.yikuyi.party.userlogin.api.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Arrays;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.yikuyi.party.contact.vo.AccountVo;
import com.yikuyi.party.contact.vo.User;
import com.yikuyi.party.contact.vo.UserExtendVo;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.login.model.UserLogin;
import com.yikuyi.party.userLogin.api.impl.UserLoginResource;
import com.yikuyi.party.userLogin.bll.UserLoginManager;


/**
 * 
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class UserLoginResourceTest {
	

	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
	
	private MockMvc mockMvc;
	
	@InjectMocks
	private UserLoginResource userLoginResource;
	
	@Mock
	private UserLoginManager mockUserLoginManager;
	LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
	
	
	public UserLoginResourceTest() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Before
	public void setUpBefore() throws Exception {
	
		mockMvc = MockMvcBuilders.standaloneSetup(userLoginResource).build();
	}
	
	@Test
	public void isExistTest() throws Exception{
		when(mockUserLoginManager.isExist("bWFsYW4=")).thenReturn(true);
		mockMvc.perform(get("/v1/account/bWFsYW4=")).andExpect(status().isOk()).andExpect(content().string("true"));
	}
	
	@Test
	public void getAccountTest() throws Exception{
		when(mockUserLoginManager.getAccount("10000@qq.com")).thenReturn(true);
		mockMvc.perform(get("/v1/account/mail?account=10000@qq.com")).andExpect(status().isOk()).andExpect(content().string("true"));
	}
	
	@Test
	public void initPassWordTest() throws Exception{
	
		UserVo userVo = new UserVo();
		userVo.setMail("malan");
		userVo.setPassword("123456");
		when(mockUserLoginManager.initPassWord(userVo)).thenReturn(userVo);
		
	    mockMvc.perform(put("/v1/account/malan").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(userVo))).andExpect(status().isOk());	
	}
	
	@Test
	public void getAccountByIdTest() throws Exception{
		
		UserLogin userLogin = new UserLogin();
		userLogin.setId("malan");
		when(mockUserLoginManager.getAccountById("malan")).thenReturn(userLogin);
		UserLogin mockUserLogin = new UserLogin();
		mockUserLogin = mapper.readValue(mockMvc.perform(get("/v1/account/id?account=malan")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), UserLogin.class);
		Assert.assertEquals(mockUserLogin.getId(),"malan");
	}
	
	@Test
	public void getAccountByIdAndTypeTest() throws Exception{		
		when(mockUserLoginManager.getAccountByIdAndType("709035037445586944", "LOGIN_NAME")).thenReturn(String.valueOf("1"));
		mockMvc.perform(get("/v1/account/user?id=709035037445586944&type=LOGIN_NAME")).andExpect(status().isOk()).andExpect(content().string("1"));
	}
	@Test
	public void updateAccountTest() throws Exception{
		AccountVo accountVo = new AccountVo();
		accountVo.setAccount("11@qq.com");
		when(mockUserLoginManager.updateAccount(Mockito.any(AccountVo.class))).thenReturn("1");
	    mapper.readValue( mockMvc.perform(put("/v1/account/account").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(accountVo))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), String.class);	
	
	}
	
	@Test
	public void sendMailTest() throws Exception{
		UserVo userVo = new UserVo();
		userVo.setMail("11@qq.com");
		when(mockUserLoginManager.sendMail(Mockito.any(UserVo.class))).thenReturn("1");
	    mapper.readValue( mockMvc.perform(post("/v1/account/sendMail").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(userVo))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), String.class);	
	
	}
	
	@Test
	public void sendCreateMailTest() throws Exception{
		UserVo userVo = new UserVo();
		userVo.setMail("11@qq.com");
		when(mockUserLoginManager.sendCreateMail(Mockito.any(UserVo.class))).thenReturn("1");
	    mapper.readValue( mockMvc.perform(post("/v1/account/sendCreateMail").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(userVo))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), String.class);	
	
	}
	

	@Test
	public void updatePersonTest() throws Exception{
		UserExtendVo userExtendVo = new UserExtendVo();
		userExtendVo.setAccounttype("test");
	 	Mockito.doNothing().when(mockUserLoginManager).updatePerson(Mockito.any( UserExtendVo.class));
		mockMvc.perform(post("/v1/account/sendCreateMail").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(userExtendVo)))
				.andExpect(status().isOk()).andReturn().getResponse();
	
	}
	
	@Test
	public void userLoginListenerTest() throws Exception{
		User user = new User();
		user.setEnterpriseName("1");
	 	Mockito.doNothing().when(mockUserLoginManager).userLoginListener(Mockito.any( User.class));
		mockMvc.perform(post("/v1/account/userLoginListener").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(user)))
				.andExpect(status().isOk()).andReturn().getResponse();
	
	}
}
