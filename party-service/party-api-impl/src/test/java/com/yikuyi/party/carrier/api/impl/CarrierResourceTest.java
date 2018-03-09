package com.yikuyi.party.carrier.api.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
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
import com.github.pagehelper.PageInfo;
import com.yikuyi.party.carrier.bll.CarrierManager;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.vo.PartyCarrierParamVo;
import com.yikuyi.party.vo.PartyCarrierVo;

/**
 * 物流信息
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class CarrierResourceTest {
	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());

	@InjectMocks
	private CarrierResource carrierResource;
	
	private MockMvc mockMvc;
	
	@Mock
	private CarrierManager mockCarrierManager;  

	public CarrierResourceTest() {
		MockitoAnnotations.initMocks(this);
	}

	@Before
	public void setUpBefore() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(carrierResource).build();
	}
	
	LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

	
	/**
	 * 物流信息列表
	 * @throws Exception
	 * @since 2017年7月26日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testPartyCarrierList() throws Exception {
		PageInfo<PartyCarrierVo> pageInfo = new PageInfo<PartyCarrierVo>();
		List<PartyCarrierVo> list = new ArrayList<PartyCarrierVo>();
		PartyCarrierVo vo = new PartyCarrierVo();
		vo.setGroupName("联邦");
		vo.setPartyStatus(PartyStatus.PARTY_ENABLED);
		list.add(vo);
		pageInfo.setList(list);
		
		when(mockCarrierManager.getPartyCarrierList(Mockito.any(PartyCarrierParamVo.class), Mockito.any(RowBounds.class))).thenReturn(pageInfo);


		JavaType javaType = mapper.getTypeFactory().constructParametricType(PageInfo.class, PartyCarrierVo.class);
		pageInfo = mapper.readValue(mockMvc.perform(get("/v1/carrier"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(pageInfo.getList().get(0).getPartyStatus().name(),"PARTY_ENABLED");
		
	}
	
	
	/**
	 * 根据partyId查询相应物流信息
	 * @throws Exception
	 * @since 2017年7月26日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void testPartyCarrierVoInfo() throws Exception{
		//构造mock返回结构数据
		PartyCarrierVo mockVo = new PartyCarrierVo();
		mockVo.setPartyId("9999999901");
	    when(mockCarrierManager.getPartyCarriorVoInfo("9999999901")).thenReturn(mockVo);
	    PartyCarrierVo party =  mapper.readValue(mockMvc.perform(get("/v1/carrier/9999999901?id=889673708757581824").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
		.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), PartyCarrierVo.class);
	   Assert.assertEquals(party.getPartyId(),"9999999901");
	}
	
	
	
	@Test
	public void updateStateIdTest()throws Exception{
		//构造mock返回结构数据
		PartyCarrierVo carrierVo = new PartyCarrierVo();
		carrierVo.setPartyStatus(PartyStatus.PARTY_ENABLED);
		Mockito.doNothing().when(mockCarrierManager).updateStatusId("868034725866897408", carrierVo);

		mockMvc.perform(put("/v1/carrier/868034725866897408?partyId=868034725866897408").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(carrierVo))).andExpect(status().isOk());
		

	}
	
	
	
}
