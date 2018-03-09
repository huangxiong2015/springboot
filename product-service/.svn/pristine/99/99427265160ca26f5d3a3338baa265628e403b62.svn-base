package com.yikuyi.product.rule.delivery.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.yikuyi.product.base.ProductApplicationTestBase;
import com.yikuyi.rule.delivery.vo.ProductLeadTimeVo;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class LeadTimeResourceV2Test extends ProductApplicationTestBase{
	
	@Autowired
	private TestRestTemplate restTemplate; 
	
	@LocalServerPort
	private int port;
	
	private String host;
	
	@Before
	public void setUpBefore() throws Exception {
		this.host = "http://localhost" + ":" + this.getPort();
	}
	
	/**
	 * 查询实时交期(有原始交期)
	 * 
	 * @since 2017年3月17日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Test
	public void testGetProductLeadTime(){
		ProductLeadTimeVo info = new ProductLeadTimeVo();
		info.setVendorId("digikey");
		info.setQty(0L);
		info.setSourceId("digikey-100");
		List<ProductLeadTimeVo> infoList = new ArrayList<>();
        infoList.add(info);
        HttpEntity<List<ProductLeadTimeVo>> entity = new HttpEntity<List<ProductLeadTimeVo>>(infoList);
        this.mockPartyService();
		ResponseEntity<List<ProductLeadTimeVo>> responseLeadTime = restTemplate.exchange(host + "/v2/products/batch/leadtime", HttpMethod.POST, entity,new ParameterizedTypeReference<List<ProductLeadTimeVo>>(){});
		List<ProductLeadTimeVo> leadTime = responseLeadTime.getBody();
		//assertEquals("15-16",leadTime.get(0).getRealityList().get(0).getRealityLeadTime());
		//assertEquals("10-11",leadTime.get(0).getRealityList().get(1).getRealityLeadTime());
	}
}
