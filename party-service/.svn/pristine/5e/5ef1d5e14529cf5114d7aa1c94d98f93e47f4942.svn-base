package com.yikuyi.party.register.api.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.framework.springboot.utils.AuthorizationUtil;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.register.bll.RegisterManager;
import com.yikuyi.pay.PayClientBuilder;
import com.yikuyi.pay.api.base.ResponseMsg;
import com.yikuyi.pay.resource.CouponResource;
import com.ykyframework.exception.BusinessException;

/**
 * 
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class RegisterResourceTest {

	
	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
	
	@InjectMocks
	private RegisterResource registerResource;
	
	@Mock
	private RegisterManager mockRegisterManager;
	
	@Mock
	private PayClientBuilder payClientbuilder;
	@Mock
	private AuthorizationUtil authorizationUtil;
	private MockMvc mockMvc;

	public RegisterResourceTest() {
		MockitoAnnotations.initMocks(this);
	}
	
	
	@Before
	public void setUpBefore() throws Exception {
		
		mockMvc = MockMvcBuilders.standaloneSetup(registerResource).build();
	}
	
	@Test
	public void testSave() throws Exception{
		
		UserVo vo = new UserVo();
		vo.setId("1");
		vo.setUuid("122222222222");
		vo.setImgCode("1666666666666666666");
		vo.setPassword("123456");
		vo.setMobile("13512345678");
		when(mockRegisterManager.save(Mockito.any())).thenReturn(String.valueOf("1"));
		ResponseMsg msg = new ResponseMsg();
		msg.setMsg("test");
		CouponResource mockApplyResource = Mockito.mock(CouponResource.class);
		Mockito.when(payClientbuilder.couponResource()).thenReturn(mockApplyResource);
		Mockito.when(mockApplyResource.regOrUpSendCoupon(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(msg);
		
		mockMvc.perform(post("/v1/customer/person").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(vo))).andExpect(status().isOk());

		
	}
	
	@Test
	public void testSaveEnt() throws Exception{
		
		UserVo userVo = new UserVo();
		userVo.setId("1");
		userVo.setUuid("122222222222");
		userVo.setImgCode("1666666666666666666");
		
		when(mockRegisterManager.saveEnt(userVo)).thenReturn(String.valueOf("1"));
		ResponseMsg msg = new ResponseMsg();
		msg.setMsg("test");
		CouponResource mockApplyResource = Mockito.mock(CouponResource.class);
		Mockito.when(payClientbuilder.couponResource()).thenReturn(mockApplyResource);
		Mockito.when(mockApplyResource.regOrUpSendCoupon(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(msg);
		
		mockMvc.perform(post("/v1/customer/enterprise").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(userVo))).andExpect(status().isOk());

		
		
	}
	/**
	 * 生成登陆账号（根据账号）
	 * @param partyId
	 * @param account
	 * @return
	 * @since 2017年7月28日
	 * @author zr.aoxianbing@yikuyi.com
	 * @throws BusinessException 
	 */
	@Test
	public void testUpgrade() throws Exception{
		Mockito.doNothing().when(mockRegisterManager).upgrade(Mockito.anyString(), Mockito.anyString());

		mockMvc.perform(post("/v1/customer/upgrade").param("partyId", "9999999901")
				.param("account", "axb@126.com")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isOk());
		
	}
	/**
	 * 生成登陆账号（根据账号）
	 * @param partyId
	 * @param account
	 * @return
	 * @since 2017年7月28日
	 * @author zr.aoxianbing@yikuyi.com
	 * @throws BusinessException 
	 */
	@Test
	public void testJoinMainAccount() throws Exception{
		when(mockRegisterManager.joinMainAccount(Mockito.anyString(),Mockito.anyString(), Mockito.anyString())).thenReturn("test");
		mockMvc.perform(post("/v1/customer/join").param("entId", "9999999901")
				.param("account", "axb@126.com").param("applyId", "1111")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isOk());
		
	}
	
	
	@Test
	public void testReSend() throws Exception{
		UserVo userVo = new UserVo();
		when(mockRegisterManager.reSend(Mockito.any(UserVo.class))).thenReturn("test");
		mockMvc.perform(post("/v1/customer/reSend").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(userVo))).andExpect(status().isOk());
		
	}
	
	@Test
	public void testSaveAccout() throws Exception{
		UserVo userVo = new UserVo();
		when(mockRegisterManager.saveAccout(Mockito.any(UserVo.class))).thenReturn("test");
		mockMvc.perform(post("/v1/customer/saveAccout").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(userVo))).andExpect(status().isOk());
		
	}
	
}
