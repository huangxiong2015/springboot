package com.yikuyi.party.user.api.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.yikuyi.party.contact.vo.User;
import com.yikuyi.party.model.PartyAttribute;
import com.yikuyi.party.party.bll.PartyAttributeManager;
import com.yikuyi.party.user.bll.UserManager;

/**
 * 
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class UserResourceTest {

	
	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
	
	private MockMvc mockMvc;
	
	@InjectMocks
	private UserResource userResource;

	@Mock
	private UserManager mockUserManager;
	
	@Mock
	private PartyAttributeManager mockPartyAttributeManager;
	
	public UserResourceTest() {
		MockitoAnnotations.initMocks(this);
	}
	
	
	@Before
	public void setUpBefore() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(userResource).build();
	}
	
	@Test
	public void testGetContactMechList() throws Exception{
		User user = new User();
		user.setId("1111");
		when(mockUserManager.getUserInfo("1111")).thenReturn(user);
		User mockUser = new User();
		
		mockUser = mapper.readValue(mockMvc.perform(get("/v1/users/1111")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), User.class);
		Assert.assertEquals(mockUser.getId(),"1111");
		
	}
	
	@Test
	public void testGetUserByAccount() throws Exception{
		//待完成
		when(mockUserManager.getUserByAccount("1111")).thenReturn(String.valueOf("success"));
		mockMvc.perform(get("/v1/users/validated/1111")).andExpect(status().isOk());
		
	}
	
	@Test
	public void testGetUserAttributes()throws Exception{

		List<PartyAttribute> list = new ArrayList<>();
		PartyAttribute party = new PartyAttribute();
		party.setKey("111");
		party.setValue("111");
		when(mockPartyAttributeManager.getPartyAttributelist("111", "111")).thenReturn(list);
		

		PartyAttribute mockPartyAttr = mapper.readValue(mockMvc.perform(get("/v1/users/111/attributes/111")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),PartyAttribute.class);
		Assert.assertEquals(mockPartyAttr.getKey(),"SALES-COUPON-CODE");
		
		
	}

}
