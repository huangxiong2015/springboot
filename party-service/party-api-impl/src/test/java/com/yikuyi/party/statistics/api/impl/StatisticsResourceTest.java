package com.yikuyi.party.statistics.api.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.yikuyi.party.statistics.bll.StatisticsManager;
import com.yikuyi.party.statistics.vo.StatisticsVo.StatisticsType;

/**
 * 
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class StatisticsResourceTest {

	private MockMvc mockMvc;

	@InjectMocks
	private StatisticsResource statisticsResource;

	@Mock
	private StatisticsManager mockStatisticsManager;

	public StatisticsResourceTest() {
		MockitoAnnotations.initMocks(this);
	}

	
	@Before
	public void setUpBefore() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(statisticsResource).build();
	}

	@Test
	public void testGetStatisticsNumByType() throws Exception {

		when(mockStatisticsManager.getStatisticsNumByType(StatisticsType.LOGIN_NUM)).thenReturn(Long.valueOf("1"));

		mockMvc.perform(get("/v1/statistics?type=LOGIN_NUM")).andExpect(status().isOk()).andExpect(content().string("1"));
	}
}
