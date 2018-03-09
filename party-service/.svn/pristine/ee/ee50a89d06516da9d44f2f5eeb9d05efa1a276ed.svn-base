/*package com.yikuyi.party.facility.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yikuyi.party.base.BasedataApplicationTestBase;
import com.yikuyi.party.facility.api.impl.FacilityResource;
import com.yikuyi.party.facility.model.Facility;
@RunWith(SpringRunner.class)
public class FacilityResourceTest extends BasedataApplicationTestBase {
	
	@Autowired
	FacilityResource facilityResource;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private TestRestTemplate restTemplate; 
	@LocalServerPort
	private int port;
	
	private String host;
	
	@Before
	public void setUpBefore() throws Exception {
		this.host = "http://localhost" + ":" + this.port;
		
		RestTemplate rt = this.restTemplate.getRestTemplate();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		ObjectMapper om = new ObjectMapper();
		om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		converter.setObjectMapper(om);
		rt.setMessageConverters(Arrays.asList(new HttpMessageConverter[]{converter}));
		rt.setRequestFactory(new SimpleClientHttpRequestFactory() {
			@Override
        	public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
        		ClientHttpRequest request = super.createRequest(uri, httpMethod);
        		HttpHeaders header = request.getHeaders();
        		header.add("Authorization", "Basic " + Base64Utils.encodeToString(("admin"+":"+"9999999901").getBytes()));
        		return request;
        	}
		});
	}
	
	@Test
	public void testGetFacilityList() {
		this.mockPartyService();
		ResponseEntity<List<Facility>> response = restTemplate.exchange(host + "/v1/facility?id=mouser", HttpMethod.GET, null, new ParameterizedTypeReference<List<Facility>>(){});
		List<Facility> list =  response.getBody();
		System.out.println(list.size());
	}
	
	@Test
	public void testFacilityBatch() {
		 List<String> ids= new  ArrayList<String>();
		 ids.add("mouser-100");
		 HttpEntity<List<String>> entity = new HttpEntity<List<String>>(ids);
		 this.mockPartyService();
		ResponseEntity<List<Facility>> response = restTemplate.exchange(host + "/v1/facility/batch?id=mouser", HttpMethod.POST, entity, new ParameterizedTypeReference<List<Facility>>(){});
		List<Facility> list =  response.getBody();
		System.out.println(list.size());
	}
	
	@Test
	public void testAddFacility() {
		Facility facility= new Facility();
		facility.setOwnerPartyId("111");
		facility.setFromDate(new Date());
		facility.setOwnerPartyId("11111");
		 HttpEntity<Facility> entity = new HttpEntity<Facility>(facility);
		 this.mockPartyService();
		ResponseEntity<Facility> response = restTemplate.exchange(host + "/v1/facility", HttpMethod.POST, entity, new ParameterizedTypeReference<Facility>(){});
		Facility fa =  response.getBody();
		assertEquals(null, fa.getFacilityName());
	}
	
	@Test
	public void testAddFacilityFromLeadMaterial() {
		Facility facility= new Facility();
		facility.setOwnerPartyId("111");
		facility.setFromDate(new Date());
		facility.setOwnerPartyId("11111");
		 HttpEntity<Facility> entity = new HttpEntity<Facility>(facility);
		 this.mockPartyService();
		ResponseEntity<Facility> response = restTemplate.exchange(host + "/v1/facility/leadMaterial", HttpMethod.POST, entity, new ParameterizedTypeReference<Facility>(){});
		Facility fa =  response.getBody();
		assertEquals(null, fa.getFacilityName());
		 
		 //facilityResource.addFacilityFromLeadMaterial(facility);
	}
	
	@Test
	public void delFacilityById() {
		this.mockPartyService();
		ResponseEntity<Boolean> response = restTemplate.exchange(host + "/v1/facility/11", HttpMethod.DELETE, null, new ParameterizedTypeReference<Boolean>(){});
		Boolean fa =  response.getBody();
		assertEquals(true, fa);
	}
	
	@Test
	public void delFacilityByPartyId() {
		this.mockPartyService();
		ResponseEntity<Boolean> response = restTemplate.exchange(host + "/v1/facility/ownerParty/11", HttpMethod.DELETE, null, new ParameterizedTypeReference<Boolean>(){});
		Boolean fa =  response.getBody();
		assertEquals(true, fa);
	}
	
	@Test
	public void testUpFacilityById() {
		Facility facility= new Facility();
		facility.setOwnerPartyId("111");
		 HttpEntity<Facility> entity = new HttpEntity<Facility>(facility);
		 this.mockPartyService();
		ResponseEntity<Boolean> response = restTemplate.exchange(host + "/v1/facility/111", HttpMethod.PUT, entity, new ParameterizedTypeReference<Boolean>(){});
		Boolean fa =  response.getBody();
		assertEquals(true, fa);
	}

	


	
}*/