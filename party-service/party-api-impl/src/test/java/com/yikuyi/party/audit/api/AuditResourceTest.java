/*
 * Created: 2016年12月9日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
/**
 * 
 */
package com.yikuyi.party.audit.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.github.pagehelper.PageInfo;
import com.yikuyi.party.audit.api.impl.AuditResource;
import com.yikuyi.party.audit.bll.AuditManager;
import com.yikuyi.party.audit.vo.AuditVo;

/**
 * 
 * @author zr.shuzuo@yikuyi.com
 * @version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class AuditResourceTest {
	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());

	@Mock
	private AuditManager mockAuditManager;

	private MockMvc mockMvc;
	
	@InjectMocks
	private AuditResource auditResource;

	public AuditResourceTest() {
		MockitoAnnotations.initMocks(this);
	}
	@Before
	public void setUpBefore() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(auditResource).build();
	}


	/**
	 * 根据条件查询审计日志
	 * @throws Exception
	 * @since 2017年8月24日
	 */
	@Test
	public void testQuery() throws Exception{
		AuditVo vo = new AuditVo();
		vo.setActionDesc("111");
		PageInfo<AuditVo> pageAudit = new PageInfo<AuditVo>();
		List<AuditVo> listAudit = new ArrayList<>();
		listAudit.add(vo);
		pageAudit.setList(listAudit);
		when(mockAuditManager.getAduitListByEntity(Mockito.any(AuditVo.class), Mockito.any(RowBounds.class))).thenReturn(pageAudit);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(PageInfo.class, AuditVo.class);
		PageInfo<AuditVo> resultPage = mapper.readValue(mockMvc.perform(get("/v1/audit")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),javaType);
		Assert.assertEquals(resultPage.getList().get(0).getActionDesc(),"111");


	}
}