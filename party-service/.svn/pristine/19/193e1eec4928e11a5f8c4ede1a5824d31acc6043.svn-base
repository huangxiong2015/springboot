
package com.yikuyi.party.vendors.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.springboot.config.ObjectMapperHelper;
import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.LoginUserInjectionInterceptor;
import com.github.pagehelper.PageInfo;
import com.yikuyi.party.PartyApplication;
import com.yikuyi.party.contact.vo.UserVo;
import com.yikuyi.party.group.model.PartyGroup;
import com.yikuyi.party.group.vo.PartyGroupVo;
import com.yikuyi.party.model.Party;
import com.yikuyi.party.model.Party.PartyStatus;
import com.yikuyi.party.model.PartyRelationship;
import com.yikuyi.party.model.PartyRelationship.PartyRelationshipTypeEnum;
import com.yikuyi.party.partygroup.bll.PartyGroupManager;
import com.yikuyi.party.role.model.RoleTypeEnum;
import com.yikuyi.party.vendor.vo.PartyProductLine;
import com.yikuyi.party.vendor.vo.PartyProductLineModel;
import com.yikuyi.party.vendor.vo.PartyProductLineModel.Status;
import com.yikuyi.party.vendor.vo.PartyProductLineVo;
import com.yikuyi.party.vendor.vo.VendorCreditVo;
import com.yikuyi.party.vendor.vo.VendorInfoVo;
import com.yikuyi.party.vendor.vo.VendorSaleInfoVo;
import com.yikuyi.party.vendors.api.impl.VendorsResource;
import com.yikuyi.party.vendors.bll.PartyProductLineManager;
import com.yikuyi.party.vendors.bll.VendorManager;
import com.yikuyi.party.vendors.bll.VendorsManager;
import com.yikuyi.party.vo.FacilityVo;
import com.yikuyi.party.vo.PartyVo;
import com.yikuyi.party.vo.SupplierVo;
import com.yikuyi.party.vo.VendorVo;

/**
 * 
 * @author zr.wuxiansheng@yikuyi.com
 * @version 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = { PartyApplication.class })
public class VendorsResourceTest {

	
	private ObjectMapper mapper = ObjectMapperHelper.configeObjectMapper(new ObjectMapper());
	
	private MockMvc mockMvc;
	
	LoginUser loginUser = new LoginUser("9999999901", "admin", "9999999901", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
	
	@InjectMocks
	private VendorsResource vendorsResource;
	
	@Mock
	private VendorsManager mockVendorsManager;
	
	@Mock
	private PartyGroupManager mockPartyGroupManager;
	
	@Mock
	private PartyProductLineManager partyProductLineManager;
	
	
	@Mock
	private VendorManager vendorManager;
	
	public VendorsResourceTest() {
		MockitoAnnotations.initMocks(this);
	}

	
	@Before
	public void setUpBefore() throws Exception {
		
		mockMvc = MockMvcBuilders.standaloneSetup(vendorsResource).build();
	}

	@Test
	public void testGetPartyList() throws Exception{		
		
		PageInfo<PartyVo> pageInfo = new PageInfo<>();
		List<PartyVo> list = new ArrayList<>();
		PartyVo party = new PartyVo();
		party.setPartyCode("10000");
		list.add(party);
		pageInfo.setList(list);
		
		PartyGroupVo param = new PartyGroupVo();
		param.setPartyIdFrom("9999999901");
		param.setStatus(PartyStatus.PARTY_ENABLED);//默认启用
		param.setRoleTypeIdTo(RoleTypeEnum.SUPPLIER.toString());
		when(mockVendorsManager.getPartyList(Mockito.any(PartyGroupVo.class),Mockito.any(RowBounds.class) )).thenReturn(pageInfo);
		
		JavaType javaType = mapper.getTypeFactory().constructParametricType(PageInfo.class, PartyVo.class);
		
		pageInfo = mapper.readValue(mockMvc.perform(get("/v1/vendors").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(pageInfo.getList().get(0).getPartyCode(),"10000");
		
	}
	

	@Test
	public void testGetPartyByPartyId() throws Exception{
		Party party = new Party();
		party.setId("digikey");
		when(mockVendorsManager.getPartyByPartyId("digikey", "9999999901")).thenReturn(party);
		
		Party mockParty = mapper.readValue(mockMvc.perform(get("/v1/vendors/digikey").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), Party.class);
		Assert.assertEquals(mockParty.getId(),"digikey");
	}


	
	@Test
	public void testSave() throws Exception{
		VendorVo ven = new VendorVo();
		ven.setVendorId("9999999901");
		ven.setCode("10000");
		Party partyVen = new Party();
		when(mockPartyGroupManager.getPartyGroupByGroupId("9999999901")).thenReturn(partyVen);
	    mockMvc.perform(post("/v1/vendors").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(ven)).requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk());

		
	    VendorVo venname = new VendorVo();
	    venname.setVendorName("sz_junit_save");
	    venname.setCode("10001");
		Party partyVenname = new Party();
		List<Party> list = new ArrayList<>();
		list.add(partyVenname);
		when(mockPartyGroupManager.findPartyGroupByNameFull("sz_junit_save")).thenReturn(list);
	    mockMvc.perform(post("/v1/vendors").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(venname)).requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk());

		
		VendorVo vo = new VendorVo();
		vo.setIsShowName("Y");
		vo.setVendorName("sz_junit_save");
		vo.setLogoUrl("www.test-log.com");
		List<FacilityVo> fList = new ArrayList<>();
		FacilityVo fvo1 = new FacilityVo();
		fvo1.setName("sz_junit_save_ffacility");
		fList.add(fvo1);
		vo.setItem(fList);
		

		Party party = new Party();
		party = mockVendorsManager.save(vo, "9999999901");
		when(mockVendorsManager.save(vo, "9999999901")).thenReturn(party);
		
	    mockMvc.perform(post("/v1/vendors").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(vo)).requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser)).andExpect(status().isOk());
	}

	@Test
	public void testUpdate() throws Exception{
		try {
			VendorVo vo = new VendorVo();
			vo.setVendorId("9999999901");
			vo.setIsShowName("Y");
			vo.setVendorName("sz_junit_save");
			vo.setLogoUrl("www.test-log.com");
			List<FacilityVo> fList = new ArrayList<>();
			FacilityVo fvo1 = new FacilityVo();
			fvo1.setName("sz_junit_save_ffacility");
			fList.add(fvo1);
			vo.setItem(fList);
			
			Party party = new Party();
			party.setPartyCode("1");
			when(mockVendorsManager.update(vo, "9999999901")).thenReturn(party);
			mockMvc.perform(put("/v1/vendors").requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(vo))).andExpect(status().isOk());
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	@Test
	public void testSaveSupplierShare() throws Exception{
		UserVo vo = new UserVo();
		vo.setId("testSharId");
		when(mockVendorsManager.saveSupplierShare("testShar", vo)).thenReturn("success");
		
	    mockMvc.perform(post("/v1/vendors/testShar/save").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(vo))).andExpect(status().isOk());

	}

	@Test
	public void testFindSupplierShare() throws Exception{
		
		List<PartyRelationship> partyRelaList = new ArrayList<PartyRelationship>();
		PartyRelationship relationship = PartyRelationship.build(PartyRelationshipTypeEnum.EMPLOYMENT);
		relationship.setPartyIdFrom("1001");
		partyRelaList.add(relationship);
		when(mockVendorsManager.findSupplierShare("testShar")).thenReturn(partyRelaList);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, PartyRelationship.class);
		List<PartyRelationship> mockList = mapper.readValue(mockMvc.perform(get("/v1/vendors/testShar/relation"))
						.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(mockList.get(0).getPartyIdFrom(), "1001");
		
	}
	
	@Test
	public void testGetVendorList() throws Exception{
		SupplierVo supplierVo = new SupplierVo();
		supplierVo.setVendorName("京东110");;
		PageInfo<SupplierVo> pageInfo = new PageInfo<SupplierVo>();
		List<SupplierVo> supplierVoList = new ArrayList<>();
		supplierVoList.add(supplierVo);
		pageInfo.setList(supplierVoList);
		when(mockVendorsManager.getVendorList("9999999901",null,null, 1, 10)).thenReturn(pageInfo);
		
		JavaType javaType = mapper.getTypeFactory().constructParametricType(PageInfo.class, SupplierVo.class);
		PageInfo<SupplierVo> resultPage = mapper
				.readValue(mockMvc.perform(get("/v1/vendors/list?id=9999999901"))
						.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(resultPage.getList().get(0).getVendorName(), "京东110");
		
	}
	
	/**
	 * 查询【供应商管理】产品线去掉重复的数据
	 * @throws Exception
	 * @since 2017年11月1日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void onlyProductLineTest() throws Exception{
		List<PartyProductLineVo> list = new ArrayList<>();
		PartyProductLineVo lineVo = new PartyProductLineVo();
		lineVo.setBrandName("AMT");
		lineVo.setPartyId("899559114013671424");
		list.add(lineVo);
		PartyProductLine partyProductLine= new PartyProductLine();
		partyProductLine.setPartyId("899559114013671424");
		when(partyProductLineManager.onlyProductLine(partyProductLine)).thenReturn(list);
		
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, PartyProductLineVo.class);
		List<PartyProductLineVo> mockList =  mapper.readValue(mockMvc.perform(get("/v1/vendors/onlyProductLine/899559114013671424"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(mockList.get(0).getBrandName(),"AMT");
	}
	
	
	/**
	 * 查询【分销商/供应商】产品线类别去掉重复的数据
	 * @since 2017年11月1日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void onlyProductLineCategoryListTest() throws Exception{
		List<PartyProductLineModel> list = new ArrayList<>();
		PartyProductLineModel lineModel = new PartyProductLineModel();
		lineModel.setPartyId("901009269527150592");
		lineModel.setCategory1Id("627");
		lineModel.setCategory1Name("电线电缆");
		lineModel.setCategory2Id("389");
		lineModel.setCategory2Name("连接器");
		lineModel.setCategory3Id("669");
		lineModel.setCategory3Name("电源供应与电路保护");
		lineModel.setStatus(Status.ENABLE);
		list.add(lineModel);
		PartyProductLineModel partyProduct= new PartyProductLineModel();
		partyProduct.setPartyId("901009269527150592");
		when(partyProductLineManager.onlyProductCategoryList(partyProduct)).thenReturn(list);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, PartyProductLineModel.class);
		List<PartyProductLineModel> mockList =  mapper.readValue(mockMvc.perform(get("/v1/vendors/productLine/category/901009269527150592"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		if(mockList!=null && CollectionUtils.isNotEmpty(mockList)){
			Assert.assertEquals(mockList.get(0).getCategory1Name(),"电线电缆");
		}
	}
	
	
	/**
	 * 根据制造商id,查询所有分销商过滤重复
	 * @throws Exception
	 * @since 2017年11月1日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void listSupplierByBrandIdTest() throws Exception{
		List<PartyProductLineVo> list = new ArrayList<>();
		PartyProductLineVo lineVo = new PartyProductLineVo();
		lineVo.setBrandName("AMT");
		lineVo.setBrandId("589");
		list.add(lineVo);
		when(partyProductLineManager.listSupplierByBrandId("589")).thenReturn(list);
		
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, PartyProductLineVo.class);
		List<PartyProductLineVo> mockList =  mapper.readValue(mockMvc.perform(get("/v1/vendors/productLine/supplier/589"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(mockList.get(0).getBrandName(),"AMT");
	}
	
	/**
	 * 根据制造商id,查询所有分类过滤重复
	 * @throws Exception
	 * @since 2017年11月1日
	 * @author zr.wuxiansheng@yikuyi.com
	 */
	@Test
	public void listCategoryByBrandIdTest() throws Exception{
		List<PartyProductLineVo> list = new ArrayList<>();
		PartyProductLineVo lineVo = new PartyProductLineVo();
		lineVo.setBrandName("AMT");
		lineVo.setBrandId("589");
		list.add(lineVo);
		when(partyProductLineManager.listCategoryByBrandId("589")).thenReturn(list);
		
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, PartyProductLineVo.class);
		List<PartyProductLineVo> mockList =  mapper.readValue(mockMvc.perform(get("/v1/vendors/productLine/supplier/category/589"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		Assert.assertEquals(mockList.get(0).getBrandName(),"AMT");
	}
	
	/**
	 * 根据供应商id获取单条供应商详情
	 * @since 2017年11月15日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void getVendorNameListByIdsTest() throws Exception{
		List<PartyVo> list = new ArrayList<>();
		when(mockVendorsManager.getVendorNameListByIds(Arrays.asList("1"))).thenReturn(list);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, PartyProductLineModel.class);
		List<PartyProductLineModel> mockList =  mapper.readValue(mockMvc.perform(post("/v1/vendors/name/batch").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(list)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
		if(mockList!=null && CollectionUtils.isNotEmpty(mockList)){
			Assert.assertEquals(mockList.get(0).getCategory1Name(),"电线电缆");
		}
	}
	
	/**
	 * 查询供应商
	 * @since 2017年11月20日
	 * @author zr.helinmei@yikuyi.com
	 */
	@Test
	public void testGetVendorDetail() throws Exception{
		Party party = new Party();
		party.setPartyCode("111");
		when(mockVendorsManager.getVendorDetail("9999999901")).thenReturn(party);
		
		Party result = mapper
				.readValue(mockMvc.perform(get("/v1/vendors/detail?id=9999999901"))
						.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), Party.class);
		Assert.assertEquals(result.getPartyCode(), "111");
		
	}
	
	@Test
	public void testVendorInfo() throws Exception{
		
		VendorInfoVo vo = new VendorInfoVo();
		vo.setCategory("1");
		when(vendorManager.getVendorInfo("999999999901")).thenReturn(vo);
		//JavaType javaType = mapper.getTypeFactory().constructParametricType(Set.class, String.class);
		VendorInfoVo result = mapper.readValue(mockMvc.perform(get("/v1/vendors/vendorInfo/999999999901"))
						.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), VendorInfoVo.class);
		Assert.assertEquals(result.getCategory(), "1");
		
	}
	
	
	@Test
	public void testProductLine() throws Exception{
		
		List<PartyProductLine> partyList = new ArrayList<>();
		PartyProductLine partyProductLine= new PartyProductLine();
		partyProductLine.setPartyId("999999999901");
		partyList.add(partyProductLine);
		when(partyProductLineManager.findByEntity(partyProductLine)).thenReturn(partyList);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, PartyProductLine.class);
		List<PartyProductLine> result = mapper.readValue(mockMvc.perform(get("/v1/vendors/productLine/999999999901"))
						.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),javaType);
		Assert.assertEquals(result.get(0).getPartyId(), "999999999901");
		
	}
	
	
	@Test
	public void testVendorCredit() throws Exception{
		VendorCreditVo vendorCreditVo = new VendorCreditVo();
		vendorCreditVo.setPartyId("999999999901");
		when(vendorManager.getVendorCredit("999999999901")).thenReturn(vendorCreditVo);
		//JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, PartyProductLine.class);
		VendorCreditVo result = mapper.readValue(mockMvc.perform(get("/v1/vendors/vendorCredit/999999999901"))
						.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),VendorCreditVo.class);
		Assert.assertEquals(result.getPartyId(), "999999999901");
		
	}
	
	

	@Test
	public void testVendorSaleInfo() throws Exception{
		VendorSaleInfoVo vendorSaleInfoVo = new VendorSaleInfoVo();
		vendorSaleInfoVo.setPartyId("999999999901");
		when(vendorManager.getVendorSaleInfoVo("999999999901")).thenReturn(vendorSaleInfoVo);
		//JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, PartyProductLine.class);
		VendorSaleInfoVo result = mapper.readValue(mockMvc.perform(get("/v1/vendors/vendorSaleInfo/999999999901"))
						.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),VendorSaleInfoVo.class);
		Assert.assertEquals(result.getPartyId(), "999999999901");
		
	}
	

	@Test
	public void testGetParentRelationInfo() throws Exception{
		/*PartyRelationship partyRelationship = new PartyRelationship();
		partyRelationship.setPartyIdTo("999999999901");*/
		try{
		when(mockVendorsManager.getParentRelationInfo("999999999901",PartyRelationshipTypeEnum.AFFILIATED_REL));
		PartyRelationship result = mapper.readValue(mockMvc.perform(get("/v1/vendors/getParentRelationInfo/999999999901/AFFILIATED_REL"))
						.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),PartyRelationship.class);
		}catch(Exception e){
			
		}
		
	}
	
	/*public void testGetPartySupplierList() throws Exception{
		PageInfo<SupplierVo> page = new PageInfo<SupplierVo>();
		List<SupplierVo> list = new ArrayList<>();
		SupplierVo vo=new SupplierVo();
		vo.setId("1");
		vo.setComments("11");
		list.add(vo);
		page.setList(list);
		when(mockVendorsManager.getPartySupplierList(Mockito.anyString(),Mockito.anyString(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(page);
		JavaType javaType = mapper.getTypeFactory().constructParametricType(PageInfo.class, SupplierVo.class);
		PageInfo<SupplierVo> result = mapper.readValue(mockMvc.perform(get("/v1/vendors/supplier/list?vendorName=1&orderSq=1&page=1&size=1"))
						.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),javaType);
		Assert.assertEquals(result.getList().get(0).getId(), "1");
		
		
	}*/
	
	@Test
	public void updateLeadTimeTest() throws Exception {
		PartyGroup partyGroup=new PartyGroup();
		Mockito.doNothing().when(mockVendorsManager).updateLeadTime(Mockito.any(PartyGroup.class));
		mockMvc.perform(put("/v1/vendors/updateLeadTime").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(partyGroup))).andExpect(status().isOk());
	}
	
	@Test
	public void testSupplierLeadTimejob() throws Exception{
		Mockito.doNothing().when(mockVendorsManager).supplierLeadTimejob();
		mockMvc.perform(get("/v1/vendors/supplierLeadTimejob").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isOk());

	}
	
	@Test
	public void testVendorBatchList() throws Exception{
		List<VendorInfoVo> list = new ArrayList<>();
		VendorInfoVo vendorInfoVo = new VendorInfoVo();
		vendorInfoVo.setApplyMail("111@qq.com");
		when(vendorManager.vendorBatchList(Mockito.anyList())).thenReturn(list);
		List<String> listStr = new ArrayList<>();
		listStr.add("1");
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, VendorInfoVo.class);
		List<VendorInfoVo> result = mapper
				.readValue(mockMvc.perform(post("/v1/vendors/batch").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(mapper.writeValueAsString(listStr))
						.requestAttr(LoginUserInjectionInterceptor.LOGIN_USER_KEY, loginUser))
						.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), javaType);
	}
}