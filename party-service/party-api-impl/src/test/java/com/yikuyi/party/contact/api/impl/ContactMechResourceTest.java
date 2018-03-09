package com.yikuyi.party.contact.api.impl;

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

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.yikuyi.party.contact.bll.ContactMechManager;
import com.yikuyi.party.contact.model.ContactMech;
import com.yikuyi.party.model.Party;
@RunWith(SpringJUnit4ClassRunner.class)
public class ContactMechResourceTest {
	
	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());

	@InjectMocks
	private ContactMechResource contactMechResource;
	
	private MockMvc mockMvc;
	
	@Mock
	private ContactMechManager contactMechManager;  

	public ContactMechResourceTest() {
		MockitoAnnotations.initMocks(this);
	}

	@Before
	public void setUpBefore() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(contactMechResource).build();
	}
	
	/**
	 * 根据地址ID查询地址详情
	 * @throws Exception
	 * @since 2017年8月24日
	 */
	@Test
	public void testGetContactMechList() throws Exception{
		   String ids = "10002";
		// 构造mock返回结构数据
			List<ContactMech> mockList = new ArrayList<>();
			ContactMech contaceMech = new ContactMech();
			contaceMech.setEmail("123456@qq.com");
			contaceMech.setId("10000");
			mockList.add(contaceMech);
			when(contactMechManager.getContactMechByIds(ids)).thenReturn(mockList);

			JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, Party.class);
			
			List<Party> mockListRst = mapper.readValue(mockMvc.perform(get("/v1/contactMechs?ids="+ids)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),javaType);
			Assert.assertEquals(mockListRst.get(0).getId(),"10000");
	}
}